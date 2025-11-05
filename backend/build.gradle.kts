plugins {
  id("org.springframework.boot") version "3.3.4"
  id("io.spring.dependency-management") version "1.1.6"
  kotlin("jvm") version "2.0.10" apply false // if not use kotlin, U can remove this section
  java 
}

group = "com.example"
verison = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  runtimeOnly("org.postgresql:postgresql:42.7.4")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test>{
  useJUnitPlatform()
}