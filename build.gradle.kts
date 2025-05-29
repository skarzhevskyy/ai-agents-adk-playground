plugins {
    id("java")
    id("application")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // JSON processing for API responses
    implementation("com.google.code.gson:gson:2.10.1")
    
    // HTTP client for API calls
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // SLF4J for logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

application {
    mainClass.set("com.example.WeatherAgent")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.named<JavaCompile>("compileTestJava") {
    options.encoding = "UTF-8"
}