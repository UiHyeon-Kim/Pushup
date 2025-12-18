// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("$rootDir/config/detekt/detekt-common.yml"))  // 커스텀 룰셋 파일 경로
        buildUponDefaultConfig = true   // detekt 기본 룰 + 커스텀 룰
        allRules = false                // 모든 룰셋 적용 - 명시적 false 시킴
    }

    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {

        android.set(true)           // Android 공식 Kotlin 스타일 가이드 적용
        outputToConsole.set(true)   // CI 로그 남김
        ignoreFailures.set(false)   // 규칙 강제

        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
        }
    }
}
