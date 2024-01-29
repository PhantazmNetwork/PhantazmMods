// https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-can-t-be-called-in-this-context-by-implicit-recei
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.fabric.loom)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

version = "1.5.0+1.19.4"

base {
    archivesName.set("phantazm-zombies-mapeditor")
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://jitpack.io")
        }
        filter {
            includeGroup("com.github.0x3C50")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://maven.fabricmc.net")
        }
        filter {
            includeGroup("net.fabricmc")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://dl.cloudsmith.io/public/steanky/ethylene/maven/")
        }
        filter {
            includeModuleByRegex("com\\.github\\.steanky", "ethylene-.+")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://dl.cloudsmith.io/public/steanky/element/maven/")
        }
        filter {
            includeModule("com.github.steanky", "element-core")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://dl.cloudsmith.io/public/steanky/vector/maven/")
        }
        filter {
            includeModule("com.github.steanky", "vector-core")
        }
    }
    exclusiveContent {
        forRepository {
            maven("https://dl.cloudsmith.io/public/steanky/toolkit/maven/")
        }
        filter {
            includeModuleByRegex("com\\.github\\.steanky", "toolkit-.+")
        }
    }
    maven("https://server.bbkr.space/artifactory/libs-release")
}

dependencies {
    minecraft(libs.minecraft.oneNineteen)
    mappings(libs.yarn.mappings.oneNineteen) {
        artifact {
            classifier = "v2"
        }
    }

    modImplementation(libs.fabric.loader)
    modImplementation(libs.libgui)
    modImplementation(libs.fabric.api.oneNineteen)
    modImplementation(libs.renderer)

    implementation(libs.adventure.api)
    implementation(libs.vector.core)
    implementation(libs.element.core)
    implementation(libs.ethylene.core)
    implementation(libs.ethylene.yaml)
    implementation(libs.ethylene.mapper)
    implementation(libs.yaml)
    implementation(libs.commons)

    include(libs.libgui)
    include(libs.renderer)

    include(libs.adventure.api)
    include(libs.adventure.key)
    include(libs.adventure.text.minimessage)
    include(libs.caffeine)
    include(libs.element.core)
    include(libs.ethylene.core)
    include(libs.ethylene.yaml)
    include(libs.ethylene.mapper)
    include(libs.examination.api)
    include(libs.examination.string)
    include(libs.snakeyaml)
    include(libs.toolkit.collection)
    include(libs.toolkit.function)
    include(libs.vector.core)
    include(libs.commons)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.compileJava {
    options.release.set(java.toolchain.languageVersion.get().asInt())
}

tasks.jar {
    from("../LICENSE") {
        rename { "${it}_${archiveBaseName.get()}" }
    }
}
