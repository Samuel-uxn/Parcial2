plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.aplications.parcial2"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.aplications.parcial2"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //Dependencias de retrrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Convierte el JSON a tus data class (y al revés)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Imprime en Logcat las peticiones, muy útil para depurar
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewModel dentro de Compose (viewModel(), collectAsStateWithLifecycle)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Navegar entre las 4 pantallas
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // Para la prueba unitaria con corrutinas (más adelante)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

}