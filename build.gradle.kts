plugins {
    id("java")
}

group = "com.tonic.woodcutter"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.runelite.net")
        content {
            includeGroupByRegex("net\\.runelite.*")
        }
    }
    mavenCentral()
}

val apiVersion = "latest.release"

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("net.runelite:client:$apiVersion")
    compileOnly("com.tonic:base-api:$apiVersion")
    compileOnly("com.tonic:api:$apiVersion")
    compileOnly("com.tonic.plugins:plugins:$apiVersion")
}