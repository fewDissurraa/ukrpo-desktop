plugins {
    id("java")
}

group = "ru.kpfu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.appium:java-client:7.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.hamcrest:hamcrest:3.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.platform:junit-platform-launcher:1.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.test {
    useJUnitPlatform()
}