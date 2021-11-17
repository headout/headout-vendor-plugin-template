plugins {
    `java-library`
    kotlin("jvm") version "1.5.10"
    `maven-publish`
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
}


subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "java")
    publishing {
        publications {
            create<MavenPublication>("default") {
                groupId = "com.headout.internal"
                artifactId = "headout-vendor-plugins"
                from(components["java"])
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/headout/headout-vendor-plugins")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=enable")
    }
}