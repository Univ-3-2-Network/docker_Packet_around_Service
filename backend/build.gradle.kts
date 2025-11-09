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

    // spring Boot section
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // DB section
    runtimeOnly("org.postgresql:postgresql:42.7.4")

    // Lombok section - newer
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    // Apache commons CSV 파싱 라이브러리 추가
    implementation("org.apache.commons:commons-csv:1.10.0")

    // 테스트 (선택)
    // testImplementation("org.springframework.boot:spring-boot-starter-test")s
}

// (선택) 리플렉션용 파라미터 이름 유지 – @RequestBody 바인딩/로그에 유용
// tasks.withType<JavaCompile> {
//     options.compilerArgs.add("-parameters")
// }

// 메인클래스 지정
springBoot {
    mainClass.set("com.example.demo.DemoApplication")
}

