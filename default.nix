{ pkgs, ... }:
let
    buildToolsVersion = "34.0.0";
    platformVersion = "34";
    cmakeVersion = "3.10.2";
    abiVersion = "x86_64"; # "arm64-v8a";
    systemImageType = "google_apis_playstore";

    androidComposition = pkgs.androidenv.composeAndroidPackages {
        cmdLineToolsVersion = "8.0";
        toolsVersion = "26.1.1";
        platformToolsVersion = "33.0.3";
        buildToolsVersions = [ buildToolsVersion ];
        includeEmulator = true;
        emulatorVersion = "35.2.5";
        platformVersions = [ platformVersion ];
        includeSources = false;
        includeSystemImages = false;
        systemImageTypes = [ systemImageType ];
        abiVersions = [ abiVersion ];
        cmakeVersions = [ cmakeVersion ];
        includeNDK = true;
        ndkVersions = ["22.0.7026061"];
        useGoogleAPIs = false;
        useGoogleTVAddOns = false;
        includeExtras = [];
    };

    sdkPath = "${androidComposition.androidsdk}/libexec/android-sdk";

    extraPath = pkgs.lib.concatMapStringsSep
        ":"
        (path: "${sdkPath}/${path}")
        [
            "platform-tools"
            "tools/bin"
            "emulator"
        ];

    emulator = pkgs.androidenv.emulateApp {
        name = "MyAndroidApp-emulator";
        inherit platformVersion abiVersion systemImageType;
    };
in {
    env = (pkgs.buildFHSEnv {
        name = "Android Kotlin App";

        targetPkgs = pkgs: with pkgs; [
            gradle
            # emulator
        ];

        profile = ''
            export ANDROID_HOME="${sdkPath}";
            export ANDROID_SDK_HOME="${sdkPath}";
            export ANDROID_SDK_ROOT="${sdkPath}";
            export ANDROID_NDK_ROOT="${sdkPath}/ndk-bundle";

            # export GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${sdkPath}/build-tools/${buildToolsVersion}/aapt2";

            export PATH="${extraPath}:$PATH"
        '';
    }).env;

    inherit emulator;
}
