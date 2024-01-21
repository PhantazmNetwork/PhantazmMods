rootProject.name = "phantazm-mods"
includeBuild("./commons")

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }

        maven("https://dl.cloudsmith.io/public/steanky/element/maven/")
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val toSkip = gradle.startParameter.projectProperties.getOrDefault("skipBuild", "").split(",")

sequenceOf(
    "zombies-mapeditor",
    "zombies-timer"
).forEach {
    if (!toSkip.contains(it)) {
        include(":phantazm-$it")
        project(":phantazm-$it").projectDir = file(it)
        println("Building module $it")
    } else {
        println("Ignoring module $it")
    }
}