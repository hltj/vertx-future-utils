plugins {
    java
    `maven-publish`
    jacoco
    signing
    id("com.github.ben-manes.versions") version "0.39.0"
}

group = "me.hltj"
version = "1.1.2"

repositories {
    mavenCentral()
}

val vertxVersion = "4.2.1"

dependencies {
    val lombokDependency = "org.projectlombok:lombok:1.18.22"

    compileOnly(lombokDependency)
    annotationProcessor(lombokDependency)
    implementation(group = "io.vertx", name = "vertx-core", version = vertxVersion)
    testCompileOnly(lombokDependency)
    testAnnotationProcessor(lombokDependency)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.8.1")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val javaApiDocUrl = JavaVersion.current().run {
    if (isJava11Compatible)
        "https://docs.oracle.com/en/java/javase/$majorVersion/docs/api/"
    else
        "https://docs.oracle.com/javase/8/docs/api/"
}

tasks.javadoc {
    with(options as StandardJavadocDocletOptions) {
        locale = "en_US"
        encoding = "UTF-8"
        links = listOf(javaApiDocUrl, "https://javadoc.io/doc/io.vertx/vertx-core/$vertxVersion/")
        addStringOption("Xdoclint:none", "-quiet")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/report.xml"))
        csv.required.set(false)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("Vert.x Future Utils")
                description.set("Convenient Utilities for Vert.x Future")
                url.set("https://github.com/hltj/vertx-future-utils")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("hltj")
                        name.set("JiaYanwei")
                        email.set("jiaywe@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/hltj/vertx-future-utils.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = propertyOrEnv("ossrhUsername", "OSSRH_USER")
                password = propertyOrEnv("ossrhPassword", "OSSRH_PASS")
            }
        }
    }
}

fun propertyOrEnv(propertyName: String, envName: String) =
        project.properties[propertyName]?.toString() ?: System.getenv(envName)

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.dependencyUpdates {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { it in version.toUpperCase() }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    return !stableKeyword && !regex.matches(version)
}
