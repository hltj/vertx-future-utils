import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    java
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

group = "me.hltj"
version = "1.0.0"

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
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Javadoc> {
    (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.withType<Test> {
    useJUnitPlatform()
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
