plugins {
    kotlin("jvm") version "1.5.0"
    `maven-publish`
    `java-library`
}

allprojects {
    repositories {
        maven("https://jitpack.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Notion
    api("org.jraf:klibnotion:1.6.0")

    // Kotlin
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))

    // Database 
    implementation("com.github.denguez:exposed-wrapper:master-SNAPSHOT")
}

publishing {
    publications {
        val mavenJava by creating(MavenPublication::class) {
            groupId = "com.github.denguez"
            artifactId = "reify.notion"
            version = "1.1"
            from(components["java"])
        }
    }
}