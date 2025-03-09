{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = {nixpkgs, ...}: let
    system = "x86_64-linux";
    pkgs = import nixpkgs {
      inherit system;

      config = {
        allowUnfree = true;
        android_sdk.accept_license = true;
      };
    };
    lib = pkgs.lib;

    # To check available versions use
    #     sdkmanager --list
    # or
    #     just specify random one and check the error message
    cfg = {
      app = {
        # will be passed as a project flavor:
        #     https://developer.android.com/build/build-variants#product-flavors
        flavor = "master";
        package = "org.kotfind.android_course";
      };

      system-image-type = "default";

      versions = {
        # SDK to Android version mapping:
        #     https://developer.android.com/tools/releases/platforms
        # Difference between compileSdk, targetSdk and minSdk:
        #     https://stackoverflow.com/a/47269079
        sdk = rec {
          compile = "35";
          target = "34";
          min = "30";

          # Setting it to compile-sdk is the easiest way, as compile-sdk
          # is the only one, that is installed anyway.
          emulator = compile;
        };

        # x86_64 allows running emulator on x86_64 machine,
        # but is likely incompable with your physical device
        abi = "x86_64";

        jvm-target = "1.8";

        # just set those to the last available version (I guess?)
        build-tools = "35.0.1";
        cmdline-tools = "9.0";
        platform-tools = "35.0.2";
        cmake = "3.31.5";
        emulator = "35.5.2";
        ndk = "28.0.13004108";
      };
    };

    androidComposition = pkgs.androidenv.composeAndroidPackages (with cfg.versions; {
      cmdLineToolsVersion = cmdline-tools;
      toolsVersion = null;
      platformToolsVersion = platform-tools;
      buildToolsVersions = [build-tools];
      includeEmulator = true;
      emulatorVersion = emulator;
      # platformVersions seems to misbehave if two same versions are specified
      platformVersions =
        if sdk.compile == sdk.emulator
        then [sdk.compile]
        else [sdk.emulator sdk.compile];
      includeSources = false;
      includeSystemImages = true;
      systemImageTypes = [cfg.system-image-type];
      abiVersions = [abi];
      cmakeVersions = [cmake];
      includeNDK = true;
      ndkVersions = [ndk];
      useGoogleAPIs = false;
      useGoogleTVAddOns = false;
      includeExtras = [];
    });

    envActivationCode = let
      sdkDir = "${androidComposition.androidsdk}/libexec/android-sdk";
      ndkDir = "${sdkDir}/ndk-bundle";

      cfgExportMap =
        lib.attrsets.mapAttrsRecursive
        (
          path: value: let
            envName =
              lib.strings.concatMapStringsSep "_" (
                pathFrag: builtins.replaceStrings ["-"] ["_"] (lib.strings.toUpper pathFrag)
              )
              path;
            envVal = lib.escapeShellArg (
              builtins.toString value
            );
          in "export ${envName}=${envVal}"
        )
        {inherit cfg;};

      cfgExportMapVals = lib.attrsets.collect builtins.isString cfgExportMap;

      cfgExport = lib.strings.concatStringsSep "\n" cfgExportMapVals;
    in
      /*
      bash
      */
      ''
        export ANDROID_HOME="${sdkDir}"
        export ANDROID_NDK_ROOT="${ndkDir}"
        export ANDROID_SDK_ROOT="${sdkDir}"

        export GRADLE_OPTS="-Dorg.gradle.project.android.aapt2FromMavenOverride=${sdkDir}/build-tools/${cfg.versions.build-tools}/aapt2"
        export GRADLE_USER_HOME="$(realpath .)/.gradle-home"

        ${cfgExport}
      '';

    fhsEnv =
      (pkgs.buildFHSEnv {
        name = "Android Development Environment";

        targetPkgs = pkgs:
          [
            pkgs.gradle
          ]
          ++ (with androidComposition; [
            androidsdk
            platform-tools
          ]);

        profile = envActivationCode;

        runScript = "bash";
      })
      .env;

    emulatorScript = let
      # Note: in `./.avd/device.avd/config.ini` set
      # `hw.keyboard` and `hw.mainKeys` to `yes`.
      # source: https://stackoverflow.com/a/64877532
      emulatorPkg = pkgs.androidenv.emulateApp ({
          name = "emulator";
          avdHomeDir = "./.avd";
        }
        // (with cfg.versions; {
          platformVersion = sdk.emulator;
          abiVersion = abi;
          systemImageType = cfg.system-image-type;
        }));
    in
      pkgs.writeShellScriptBin "emulator" ''
        ${envActivationCode}
        ${lib.getExe' emulatorPkg "run-test-emulator"}
      '';

    genRunScript = {
      # name of generated script
      scriptName,
      # gradle subcommand like "install" or "assemble"
      gradleAction,
      # build type: "debug" or "release"
      buildType ? "Debug",
      # whatever or not to run app after installation
      doRun ? true,
      # "emulator", "device" or "none"
      deviceType ? "emulator",
    }: let
      firstLetterToUpper = str:
        lib.strings.toUpper (builtins.substring 0 1 str)
        + builtins.substring 1 ((builtins.stringLength str) - 1) str;

      fullPackageName = with cfg.app; package + "." + flavor;

      gradleSubcommand = "${gradleAction}${
        lib.escapeShellArg (firstLetterToUpper cfg.app.flavor)
      }${
        lib.escapeShellArg (firstLetterToUpper buildType)
      }";

      adb = lib.getExe' androidComposition.platform-tools "adb";
      awk = lib.getExe pkgs.gawk;
      gradle = lib.getExe pkgs.gradle;
      echo = lib.getExe' pkgs.toybox "echo";
      head = lib.getExe' pkgs.toybox "head";
      tail = lib.getExe' pkgs.toybox "tail";

      awkDeviceNameFilter =
        (
          if deviceType == "emulator"
          then ""
          else "!"
        )
        + "/emulator/";

      notFoundErrorMsg = "error: no ${
        if deviceType == "emulator"
        then "emulators"
        else "devices"
      } found";

      setAndroidSerialCmd =
        if deviceType == "emulator" || deviceType == "device"
        then
          /*
          bash
          */
          ''
            ANDROID_SERIAL="$( \
              ${adb} devices | \
              ${tail} -n +2 | \
              ${awk} '${awkDeviceNameFilter} { print $1; }' | \
              ${head} -n 1)"
            export ANDROID_SERIAL

            if [ -z "$ANDROID_SERIAL" ]; then
              ${echo} ${lib.escapeShellArg notFoundErrorMsg}
              exit 1
            fi
          ''
        else "";

      runCommand =
        if doRun
        then "${adb} shell monkey -p ${fullPackageName} -c android.intent.category.LAUNCHER 1"
        else "";
    in
      pkgs.writeShellScriptBin scriptName ''
        set -euo pipefail
        set -o

        ${setAndroidSerialCmd}
        ${gradle} ${gradleSubcommand}
        ${runCommand}
      '';
  in {
    devShells.${system}.default = fhsEnv;

    apps.${system} = {
      emulator = {
        type = "app";
        program = lib.getExe emulatorScript;
      };

      run = {
        type = "app";
        program = lib.getExe (genRunScript {
          scriptName = "run";
          gradleAction = "install";
          buildType = "debug";
          deviceType = "emulator";
        });
      };

      install = {
        type = "app";
        program = lib.getExe (genRunScript {
          scriptName = "install";
          gradleAction = "install";
          buildType = "debug";
          deviceType = "device";
        });
      };

      assemble = {
        type = "app";
        program = lib.getExe (genRunScript {
          scriptName = "assemble";
          gradleAction = "assemble";
          buildType = "debug";
          deviceType = "none";
          doRun = false;
        });
      };
    };
  };
}
