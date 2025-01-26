{
    description = "Kotlin Android";

    outputs = { nixpkgs, ... }:
        let
            system = "x86_64-linux";

            pkgs = import nixpkgs {
                inherit system;

                config = {
                    allowUnfree = true;
                    android_sdk.accept_license = true;
                };
            };

            android = import ./default.nix { inherit pkgs; };
        in
        {
            devShells.${system}.default = android.env;
            apps.${system}.default = {
                type = "app";
                program = "${android.emulator}/bin/run-test-emulator";
            };
        };
}
