plugins {
    kotlin("jvm") version "1.9.10"  // Latest stable version as of now
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.serialization") version "2.0.21"
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "mike.dev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.tomcat.embed:tomcat-embed-core")
    implementation("com.google.http-client:google-http-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-core")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // if you're using Thymeleaf
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.security:spring-security-oauth2-core")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // Jakarta Servlet API for Spring Boot 3.x
    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0") // Use the version compatible with your Spring Boot version
    // Ensure you have Spring Boot starters which should include necessary dependencies
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // or jjwt-gson
    implementation("com.google.api-client:google-api-client:1.34.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.oauth-client:google-oauth-client:1.34.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
}

tasks.test {
    useJUnitPlatform()
}