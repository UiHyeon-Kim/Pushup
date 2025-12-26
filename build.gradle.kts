// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("$rootDir/config/detekt/detekt-common.yml"))  // 커스텀 룰셋 파일 경로
        buildUponDefaultConfig = true   // detekt 기본 룰 + 커스텀 룰
        allRules = false                // 모든 룰셋 적용 - 명시적 false 시킴

        // 빌드 시 자동 실행 방지
        ignoreFailures = true
    }

    // detekt를 별도 태스크로만 실행 설정
    tasks.matching {
        it.name.contains("detekt")
    }.configureEach {
        mustRunAfter("assemble", "build")
    }
}
