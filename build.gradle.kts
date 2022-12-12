plugins { kotlin("jvm") version "1.7.20" }

repositories { mavenCentral() }

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }
