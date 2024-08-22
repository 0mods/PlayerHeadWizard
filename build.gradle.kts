import groovy.lang.Closure
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.remapping.RemapperExtension
import net.fabricmc.loom.api.remapping.RemapperParameters
import net.fabricmc.loom.extension.LoomGradleExtensionImpl
import net.fabricmc.loom.extension.RemapperExtensionHolder
import net.fabricmc.loom.util.ModPlatform
import net.fabricmc.tinyremapper.TinyRemapper

plugins {
    java
    `maven-publish`
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val modId = "archives_name".fromProperties
val minecraftVersion = stonecutter.current.project.substringBeforeLast('-')
val modPlatform = stonecutter.current.project.substringAfterLast('-')
val modName = "mod_name".fromProperties
val modVersion = "mod_version".fromProperties
val kotlinVersion: String by project
val shadowLibrary: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

architectury {
    minecraft = minecraftVersion
    when (modPlatform) {
        "fabric" -> fabric()
        "forge" -> forge()
        "neoforge" -> neoForge()
    }
}

loom {
    silentMojangMappingsLicense()
    val awFile = rootProject.file("src/main/resources/$modId.accesswidener")
    if (awFile.exists()) accessWidenerPath = awFile

    when (modPlatform) {
        "forge" -> forge {
            convertAccessWideners = true
            mixinConfig("$modId.mixins.json")
            (this@loom as LoomGradleExtensionImpl).remapperExtensions.add(ForgeFixer)
        }
        "neoforge" -> neoForge {}
    }
}

base {
    archivesName = "$modId-$modPlatform-$minecraftVersion"
}

version = modVersion

afterEvaluate {
    stonecutter {
        val platform = loom.platform.get().id()
        stonecutter.const("fabric", platform == "fabric")
        stonecutter.const("forge", platform == "forge")
        stonecutter.const("neoforge", platform == "neoforge")

        stonecutter.exclude("src/main/resources")
    }
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://maven.0mods.team/releases")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.0mods.team/releases")
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.fabricmc.net/")
    flatDir { dir("libs") }
}

dependencies {
    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    "mappings"(loom.layered {
        officialMojangMappings()
        val mappingsVer = if (stonecutter.eval(minecraftVersion, ">=1.21")) "1.21:2024.07.28"
        else {
            if (minecraftVersion == "1.20.6") { "1.20.6:2024.06.16" }
            else if (stonecutter.eval(minecraftVersion, "<1.20.5")) "1.20.1:2023.09.03"
            else if (stonecutter.eval(minecraftVersion, ">=1.19")) "1.19.2:2022.11.27"
            else ""
        }
        parchment("org.parchmentmc.data:parchment-$mappingsVer")
    })

    when (modPlatform) {
        "fabric" -> {
            "modImplementation"("net.fabricmc:fabric-loader:0.15.11")
        }

        "forge" -> {
            when (minecraftVersion) {
                "1.21" -> "forge"("net.minecraftforge:forge:${minecraftVersion}-51.0.33")
                "1.20.6" -> "forge"("net.minecraftforge:forge:${minecraftVersion}-50.1.12")
                "1.20" -> "forge"("net.minecraftforge:forge:${minecraftVersion}-46.0.14")
                "1.19" -> "forge"("net.minecraftforge:forge:${minecraftVersion}-41.1.0")
            }
        }

        "neoforge" -> {
            when (minecraftVersion) {
                "1.21" -> "neoForge"("net.neoforged:neoforge:21.0.167")
                "1.20.6" -> "neoForge"("net.neoforged:neoforge:20.6.119")
            }
        }
    }

    shadowLibrary("team.0mods:KotlinExtras:1.4-noreflect")

    implementation(kotlin("stdlib", "2.0.10")); minecraftRuntimeLibraries(kotlin("stdlib", "2.0.10"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+") { minecraftRuntimeLibraries(this) }
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:+") { minecraftRuntimeLibraries(this) }
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:+") { minecraftRuntimeLibraries(this) }
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:+") { minecraftRuntimeLibraries(this) }
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.jar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/$modVersion"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }

    rootProject.tasks.register("runActive") {
        group = "project"
        dependsOn(tasks.named("runClient"))
    }
}

sourceSets {
    if (modPlatform == "forge" || modPlatform == "neoforge") {
        main.get().resources.exclude("")
    }
}

tasks {
    shadowJar {
        configurations = listOf(shadowLibrary)
        archiveClassifier = "dev-shadow"

        val relocateLibs = listOf(
            "org.jetbrains", "com.typesafe", "kotlinx",
            "kotlin", "okio", "org.intellij", "_COROUTINE"
        )

        relocateLibs.forEach {
            relocate(it, "${"maven_group".fromProperties}.$modId.shadowlibs.$it")
        }
    }

    remapJar {
        inputFile = shadowJar.get().archiveFile
    }

    processResources {
        from(project.sourceSets.main.get().resources)

        if (modPlatform == "forge" || modPlatform == "neoforge") {
            exclude("fabric.mod.json")

            if (modPlatform == "forge") {
                exclude("neoforge.mods.toml")
            } else exclude("mods.toml")
        } else {
            exclude("neoforge.mods.toml")
            exclude("mods.toml")
        }

        filesMatching(listOf("META-INF/mods.toml", "fabric.mod.json")) {
            expand(mapOf(
                "version" to modVersion, "id" to modId, "mod_version" to modVersion,
                "display" to modName, "target_minecraft" to minecraftVersion
            ))
        }
    }
}

stonecutter {
    val j21 = eval(minecraftVersion, ">=1.20.5")
    java {
        withSourcesJar()
        sourceCompatibility = if (j21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
        targetCompatibility = if (j21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17

        toolchain {
            languageVersion = JavaLanguageVersion.of(if (eval(minecraftVersion,  ">=1.20.5")) 21 else 17)
        }
    }

    kotlin {
        jvmToolchain(if (j21) 21 else 17)
    }

    stonecutter.exclude("src/main/resources")
}

class KClosure<T : Any?>(val function: T.() -> Unit) : Closure<T>(null, null) {
    fun doCall(it: T): T {
        function(it)
        return it
    }
}

fun <T : Any> closure(function: T.() -> Unit): Closure<T> {
    return KClosure(function)
}

object ForgeFixer : RemapperExtensionHolder(object : RemapperParameters {}) {
    override fun getRemapperExtensionClass(): Property<Class<out RemapperExtension<*>>> {
        throw UnsupportedOperationException("How did you call this method?")
    }

    override fun apply(
        tinyRemapperBuilder: TinyRemapper.Builder,
        sourceNamespace: String,
        targetNamespace: String,
        objectFactory: ObjectFactory,
    ) {
        // Under some strange circumstances there are errors with mapping source names, but that doesn't stop me from compiling the jar, does it?
        tinyRemapperBuilder.ignoreConflicts(true)
    }
}

val String.fromProperties get() = rootProject.properties[this].toString()