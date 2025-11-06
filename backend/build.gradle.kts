plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

repositories { mavenCentral() }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("org.postgresql:postgresql:42.7.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Apache commons CSV 파싱 라이브러리 추가
    implementation("org.apache.commons:commons-csv:1.10.0") // CSV 파싱)
}

// 메인클래스 지정
springBoot {
    mainClass.set("com.example.demo.DemoApplication")
}

