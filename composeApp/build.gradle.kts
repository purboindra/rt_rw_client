
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.google.services)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.composeHotReload)
}

kotlin {

//    androidLibrary {
//        androidResources.enable = true
//    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11) }
    }

    if (HostManager.hostIsMac) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            /// FIREBASE
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.cloud.messaging)
            implementation(libs.firebase.messaging.directboot)

            /// COROUTINE
            implementation(libs.kotlinx.coroutines.android)

            /// KOIN
            implementation(libs.koin.android)

            /// COIL
            implementation(libs.coil.network.okhttp)

            /// KTOR
            implementation(libs.ktor.client.android)

            /// ROOM
            implementation(libs.androidx.room.paging)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            /// KTOR
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.auth)

            /// NAVIGATION
            implementation(libs.navigation.compose)

            /// COROUTINES
            implementation(libs.kotlinx.coroutines.core)

            /// KOIN
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            /// COIL
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)


            /// DATASTORE
            implementation(libs.datastore.preferences)
            implementation(libs.datastore)

            /// ROOM
            implementation(libs.androidx.room.runtime)

            /// COMPOSE ICONS
            implementation(libs.composeIcons.feather)

            /// KOTLINX DATETIME
            implementation(libs.kotlinx.datetime)

            /// KERMIT
            implementation(libs.kermit)

            /// IMAGE PICKER KMP
            implementation(libs.imagepickerkmp)
        }

        if (HostManager.hostIsMac) {
            iosMain.dependencies {
                implementation(libs.ktor.client.darwin)

            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}

buildkonfig {
    packageName = "org.purboyndradev.rt_rw.config"

    defaultConfigs {
        buildConfigField(
            STRING,
            "BASE_URL",
//            "https://rtrwbackend-production.up.railway.app"
            "http://192.168.1.7:3000"
        )

        buildConfigField(
            STRING,
            "ENVIRONMENT",
            "RELEASE"
        )
    }

    targetConfigs {
        create("ios") {
            buildConfigField(
                STRING,
                "BASE_URL",
                "http://192.168.1.7:3000"
//                "https://rtrwbackend-production.up.railway.app"
            )
            buildConfigField(STRING, "ENVIRONMENT", "DEBUG")
        }
    }
}

android {
    namespace = "org.purboyndradev.rt_rw"
    compileSdk = 36
    buildFeatures { buildConfig = true }
    defaultConfig {
        applicationId = "org.purboyndradev.rt_rw"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
//        getByName("debug") {
//            buildConfigField("String", "BASE_URL", "\"https://rtrwbackend-production.up.railway.app/api/v1/\"")
//            buildConfigField("String", "ENVIRONMENT", "\"DEBUG\"")
//        }
//        create("staging") {
//            initWith(getByName("debug"))
//            buildConfigField("String", "BASE_URL", "\"https://rtrwbackend-production.up.railway.app/api/v1/\"")
//            buildConfigField("String", "ENVIRONMENT", "\"STAGING\"")
//        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            initWith(getByName("release"))
//            buildConfigField("String", "BASE_URL", "\"https://rtrwbackend-production.up.railway.app/api/v1/\"")
//            buildConfigField("String", "ENVIRONMENT", "\"RELEASE\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)

    if (HostManager.hostIsMac) {
        add("kspIosArm64", libs.androidx.room.compiler)
        add("kspIosX64", libs.androidx.room.compiler)
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    }

    debugImplementation(compose.uiTooling)

//    implementation(":shared")
}

room {
    schemaDirectory("$projectDir/schemas")
}

