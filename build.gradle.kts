plugins {
    kotlin("jvm") version "1.7.20"
    id("io.qameta.allure") version "2.11.2"
}

repositories { mavenCentral() }


dependencies {
    testImplementation(kotlin("test"))

    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("io.qameta.allure:allure-assertj:2.20.1")
    testImplementation("io.qameta.allure:allure-junit5:2.20.1")
    testImplementation("org.jetbrains.exposed:exposed-core:0.41.1")
    testImplementation("org.postgresql:postgresql:42.5.1")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

}

tasks.test { useJUnitPlatform() }
