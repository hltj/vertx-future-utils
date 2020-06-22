plugins {
    java
}

group = "me.hltj"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "io.vertx", name = "vertx-core", version = "3.9.1")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.6.2")
    testCompileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    testAnnotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}
