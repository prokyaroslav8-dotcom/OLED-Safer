plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.oledsafer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.oledsafer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ДОБАВЬ ЭТОТ БЛОК. Он связывает Kotlin и Compose напрямую
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" 
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // ОБНОВИ ЭТУ СТРОКУ (версия 2024.05.00)
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
}

kotlin {
    jvmToolchain(17)
}
