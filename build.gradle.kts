import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    java
    `maven-publish`
    jacoco
    id("com.jfrog.bintray") version "1.8.5"
}

group = "me.hltj"
version = "1.0.1"

repositories {
    mavenCentral()
}

val vertxVersion = "3.9.1"

dependencies {
    implementation(group = "io.vertx", name = "vertx-core", version = vertxVersion)
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.6.2")
    testCompileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    testAnnotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
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

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/report.xml")
        csv.isEnabled = false
    }
}

publishing {
    publications {
        create<MavenPublication>("bintray") {
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
}

bintray {
    user = propertyOrEnv("bintray.user", "BINTRAY_USER")
    key = propertyOrEnv("bintray.key", "BINTRAY_KEY")
    setPublications("bintray")

    with(pkg) {
        repo = "mvn"
        name = project.name
        vcsUrl = "https://github.com/hltj/vertx-future-utils.git"
        setLicenses("LGPL-3.0")
        setLabels("vert.x", "future", "utilities", "utility-library")

        with(version) {
            val versionString = project.version.toString()
            name = versionString
            vcsTag = versionString
        }
    }
}

fun BintrayExtension.propertyOrEnv(propertyName: String, envName: String) =
    project.properties[propertyName]?.toString() ?: System.getenv(envName)
