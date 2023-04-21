/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

}

dependencies {
    api("mysql:mysql-connector-java:8.0.30")
    api("org.apache.commons:commons-lang3:3.12.0")
    api("io.springfox:springfox-swagger2:3.0.0")
    api("io.springfox:springfox-boot-starter:3.0.0")
    api("io.springfox:springfox-swagger-ui:3.0.0")
    api("org.springframework.boot:spring-boot-autoconfigure:2.7.2")
    api("org.springframework.boot:spring-boot-starter-web:2.7.2")
    api("org.springframework.boot:spring-boot-starter:2.7.2")
    api("org.springframework.boot:spring-boot-starter-tomcat:2.7.2")
    api("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
    api("org.springframework.boot:spring-boot-starter-hateoas:2.7.2")
    api("org.mapstruct:mapstruct:1.5.3.Final")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1")
    testImplementation("com.h2database:h2:1.4.193")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.4")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("org.mapstruct:mapstruct-processor:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")


    implementation("com.github.java-json-tools:json-patch:1.13")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")

}

group = "com.epam.esm"
version = "1.0"
description = "v_burmus Web-app"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}