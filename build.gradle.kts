@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.tools.minecraft.OneConfigBuilder
import dev.deftu.gradle.utils.GameSide
import dev.deftu.gradle.utils.version.MinecraftVersions

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion") // Applies preprocessing for multiple versions of Minecraft and/or multiple mod loaders.
    id("dev.deftu.gradle.tools") // Applies several configurations to things such as the Java version, project name/version, etc.
    id("dev.deftu.gradle.tools.resources") // Applies resource processing so that we can replace tokens, such as our mod name/version, in our resources.
    id("dev.deftu.gradle.tools.bloom") // Applies the Bloom plugin, which allows us to replace tokens in our source files, such as being able to use `@MOD_VERSION` in our source files.
    id("dev.deftu.gradle.tools.shadow") // Applies the Shadow plugin, which allows us to shade our dependencies into our mod JAR. This is NOT recommended for Fabric mods, but we have an *additional* configuration for those!
    id("dev.deftu.gradle.tools.minecraft.loom") // Applies the Loom plugin, which automagically configures Essential's Architectury Loom plugin for you.
    id("dev.deftu.gradle.tools.minecraft.releases") // Applies the Minecraft auto-releasing plugin, which allows you to automatically release your mod to CurseForge and Modrinth.
}
val applyOneConfigBuilder: OneConfigBuilder.() -> Unit = {
    version = "1.0.0-alpha.109"
    loaderVersion = "1.1.0-alpha.46"
    applyLoaderTweaker = true
}
val omnicoreVersion = "0.32.0"
val textileVersion = "0.18.0"

val oneConfigBuilder = OneConfigBuilder().apply(applyOneConfigBuilder)

toolkitLoomHelper {
    useOneConfig(applyOneConfigBuilder)
    useDevAuth("1.2.1")
    disableRunConfigs(GameSide.SERVER)
}

dependencies {
    if (mcData.version >= MinecraftVersions.VERSION_1_21) {
        val dependencies = arrayOf("commands", "config", "config-impl", "events", "internal", "ui", "hud", "utils").map { it to false } + ("$mcData" to true)
        for ((dep, isMod) in dependencies) {
            val dependency = "org.polyfrost.oneconfig:$dep:${oneConfigBuilder.version}"
            if (isMod) {
                include(modImplementation(dependency)!!)
            } else {
                include(implementation(dependency)!!)
            }
        }
        include(compileOnly("org.polyfrost.oneconfig.dependencies:agnostic:${oneConfigBuilder.version}")!!)
        include(compileOnly("dev.deftu:omnicore-$mcData:$omnicoreVersion")!!)
        include(compileOnly("dev.deftu:textile-$mcData:$textileVersion")!!)
        include(compileOnly("net.fabricmc:fabric-language-kotlin:1.12.2+kotlin.2.0.20") {
            isTransitive = false
        })

        val lwjglBase = "org.lwjgl:lwjgl"
        val lwjglVersion = when (mcData.version.preprocessorKey) {
            in 11600..11800 -> "3.2.2"
            in 11900..11999 -> "3.3.1"
            in 12000..12099 -> "3.3.2"
            in 12100..12199 -> "3.3.3"
            else -> throw IllegalStateException("Unsupported Minecraft version: ${mcData.version}")
        }

        include(compileOnly("$lwjglBase-tinyfd:$lwjglVersion")!!)
        include(compileOnly("$lwjglBase-nanovg:$lwjglVersion")!!)
    }
}