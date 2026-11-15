plugins {
    kotlin("jvm") version "2.4.21"
}

group = "com.raynor.demo"
version = "1.0-SNAPSHOT"

val kotestVersion = "6.2.7"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.mockk:mockk:1.14.12")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
