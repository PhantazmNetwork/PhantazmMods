// https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-can-t-be-called-in-this-context-by-implicit-recei
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.fabric.loom)
}

version = "0.2.1+1.20.4"

base {
    archivesName.set("zombies-autosplits")
}

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
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
}

dependencies {
    minecraft(libs.minecraft.oneTwenty)
    mappings(libs.yarn.mappings.oneTwenty) {
        artifact {
            classifier = "v2"
        }
    }

    modImplementation(libs.fabric.loader.oneTwenty)
    modImplementation(libs.fabric.api.oneTwenty)
    modImplementation(libs.modmenu)
    modImplementation(libs.cloth.config)

    implementation(libs.adventure.key)
    implementation(libs.ethylene.core)
    implementation(libs.ethylene.yaml)
    implementation(libs.yaml)
    implementation(libs.commons)

    include(libs.adventure.key)
    include(libs.examination.api)
    include(libs.examination.string)
    include(libs.ethylene.core)
    include(libs.ethylene.yaml)
    include(libs.toolkit.collection)
    include(libs.toolkit.function)
    include(libs.snakeyaml)
    include(libs.commons)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    from("../LICENSE") {
        rename { "${it}_${archiveBaseName.get()}" }
    }
}

tasks.remapJar {
    this.doNotTrackState("Required to enable configuration caching")
}