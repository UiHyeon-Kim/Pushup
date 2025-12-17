plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("io.gitlab.arturbosch.detekt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

detekt {
    config = files(
        "$rootDir/config/detekt/detekt-common.yml",
        "$rootDir/config/detekt/detekt-compose.yml",
    ) // 커스텀 룰셋 파일 경로
    buildUponDefaultConfig = true   // detekt 기본 룰 + 커스텀 룰
    allRules = false    // 모든 룰셋 적용 - 명시적 false 시킴
}

android {
    namespace = "com.hanhyo.presentation"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hanhyo.presentation"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    lint {
        abortOnError = true         // CI에서 실패 처리
        checkDependencies = true    // 모듈 포함 검사
        ignoreTestSources = true    // 테스트 코드 무시
        warningsAsErrors = false    // 경고를 에러로 처리

        lintConfig = file("$rootDir/config/lint/lint.xml")
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    detektPlugins(libs.detekt.compose)

    // 테스트
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)

    // UI 테스트
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}
