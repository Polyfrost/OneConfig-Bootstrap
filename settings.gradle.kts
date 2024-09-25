pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.org/releases")
    }
    plugins {
        val pgtVersion = "0.6.7"
        id("org.polyfrost.multi-version.root") version pgtVersion
    }
}

val mod_name: String by settings

rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9-forge",
    "1.8.9-fabric",
    "1.12.2-fabric",
    "1.12.2-forge",
    "1.16.5-forge",
    "1.16.5-fabric",
    "1.17.1-forge",
    "1.17.1-fabric",
    "1.18.2-forge",
    "1.18.2-fabric",
    "1.19.4-forge",
    "1.19.4-fabric",
    "1.20.4-fabric",
    "1.20.4-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}