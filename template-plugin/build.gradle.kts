plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/headout/headout-common-apis")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/headout/headout-commons")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(Dependencies.kotlinCoroutines)
    implementation(Dependencies.kotlinReactive)

    implementation(headoutInternal("vendor-api", "1.2.1"))
    implementation(headoutInternal("enums", "0.1.7"))

    kapt(Dependencies.autoValue)
    implementation(Dependencies.autoValue)
    implementation(Dependencies.autoValueAnnotation)

    implementation(Dependencies.jacksonJoda)
    implementation(Dependencies.retrofitJacksonConverter)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.adapterRxJava)

    implementation(Dependencies.jacksonDataformatCsv)
    implementation(Dependencies.jacksonDataformatXml)
    implementation(Dependencies.jacksonjsr310)
    implementation(Dependencies.jacksonDataTypeJSON)

    implementation(Dependencies.javaxServlet)
    implementation(Dependencies.javaxAnnotationApi)
    implementation(Dependencies.validationApi)

    implementation(Dependencies.adapterCoroutine)

    implementation(Dependencies.loggingInterceptor)

    implementation(Dependencies.jacksonKotlin)

    implementation(Dependencies.apacheHttpClient)

    implementation(Dependencies.awsJavaSdk)

    implementation(Dependencies.bouncyCastlePkix)
    implementation(Dependencies.bouncyCastleProv)

    implementation(Dependencies.commonsValidator)

    implementation(Dependencies.gson)

    implementation(Dependencies.jaxbApi)
    implementation(Dependencies.javaxJws)
    implementation ("com.sun.xml.ws:jaxws-rt:2.3.5") {
        exclude(group = "com.sun.mail", module = "jakarta.mail")
    }
//    implementation(Dependencies.jaxWsTools)
    implementation(Dependencies.jsr305)
    implementation(Dependencies.guava)

    implementation(Dependencies.slf4jSimple)
    implementation(Dependencies.slf4jApi)

    implementation(Dependencies.jedis)
    implementation(Dependencies.lettuce)

    implementation(files("src/dist"))

    implementation(Dependencies.orgJson)
    implementation(Dependencies.jsonSimple)

    implementation(Dependencies.tomcat)
    implementation(Dependencies.mockito)

    implementation(Dependencies.hibernate)

    testImplementation("org.apache.pdfbox:pdfbox:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.3.1")

}

tasks.withType<Test>  {
    // To run the unit test cases comment line 106 and please revert it before pushing the changes.
    onlyIf { project.hasProperty("runTests") }
    // Using JUnitPlatform for running tests
    useJUnitPlatform()
    testLogging {
        // Make sure output from
        // standard out or error is shown
        // in Gradle output.
        showStandardStreams = true
    }
}