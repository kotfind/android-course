val packageName = env<String>("CFG_APP_PACKAGE")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = packageName
    compileSdk = env("CFG_VERSIONS_SDK_COMPILE")

    defaultConfig {
        applicationId = packageName
        versionName = "1.0"
        versionCode = 1

        minSdk = env("CFG_VERSIONS_SDK_MIN")
        targetSdk = env("CFG_VERSIONS_SDK_TARGET")

        vectorDrawables { useSupportLibrary = true }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDirs("res")
        }
    }

    buildTypes { release { isMinifyEnabled = false } }

    compileOptions {
        sourceCompatibility = env("CFG_VERSIONS_JVM_TARGET")
        targetCompatibility = env("CFG_VERSIONS_JVM_TARGET")
    }

    kotlinOptions { jvmTarget = env("CFG_VERSIONS_JVM_TARGET") }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.1" }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }

    buildToolsVersion = env("CFG_VERSIONS_BUILD_TOOLS")
    ndkVersion = env("CFG_VERSIONS_NDK")
    externalNativeBuild { cmake { version = env("CFG_VERSIONS_CMAKE") } }

    flavorDimensions += "default"
    productFlavors {
        val flavor = env<String>("CFG_APP_FLAVOR")

        create(flavor) {
            dimension = "default"
            applicationIdSuffix = ".${flavor}"
            versionNameSuffix = "-${flavor}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
}

// Checks if all source files have right package name
tasks.register("checkSourceFilesPackage") {
    group = "verification"

    doLast {
        fileTree("src")
            .filter { it.isFile && it.extension == "kt" }
            .forEach { file ->
                val packageRegex = Regex("^\\s*package\\s*([a-zA-Z0-9_\\.]+)\\s*$")
                file.useLines { lines ->
                    var packageIsDefined = false
                    for (line in lines) {
                        val match = packageRegex.find(line)
                        if (match == null) {
                            continue
                        }
                        val filePackage = match.groupValues[1]
                        if (filePackage != packageName &&
                            !filePackage.startsWith(packageName + ".")) {
                            throw GradleException(
                                "Package '$filePackage' (defined in '$file') is not a subpackage of main package '$packageName' (defined in flake.nix)")
                        }
                        packageIsDefined = true
                        break
                    }
                    if (!packageIsDefined) {
                        throw GradleException("File '$file' has no package definition")
                    }
                }
            }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("checkSourceFilesPackage")
}

inline fun <reified T> env(env_name: String): T {
    val env_val = System.getenv(env_name)

    if (env_val == null) {
        throw GradleException("$env_name not set: did you forget to enter devShell?")
    }

    try {
        return when (T::class) {
            Int::class -> env_val.toInt() as T
            String::class -> env_val as T
            JavaVersion::class -> JavaVersion.valueOf("VERSION_" + env_val.replace(".", "_")) as T

            else -> throw GradleException("Unsupported env type ${T::class.simpleName}")
        }
    } catch (e: GradleException) {
        throw e
    } catch (e: Exception) {
        throw GradleException(
            "$env_name is set to value '$env_val', which " +
                "cannot be parsed to ${T::class.simpleName}",
            e)
    }
}
