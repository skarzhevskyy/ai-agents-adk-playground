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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.google.adk:google-adk:0.1.0")
    // A Dev UI for debugging your agents
    implementation("com.google.adk:google-adk-dev:0.1.0")
    // Add Apache HttpClient 5.x needed for ADK Dev UI
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.3")


    // SLF4J for logging
    //implementation("org.slf4j:slf4j-api:2.0.9")
    //implementation("org.slf4j:slf4j-simple:2.0.9")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

application {
    mainClass.set("com.example.Application")
}

tasks.register<JavaExec>("devUi") {
    group = "application"
    description = "Start the ADK Dev UI server"
    mainClass.set("com.google.adk.web.AdkWebServer")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("--adk.agents.source-dir=src/main/java")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isDeprecation = true
    options.compilerArgs.add("-parameters")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
