object Versions {
    const val retrofitVersion = "2.7.1"
    const val jacksonVersion = "2.9.7"
    const val okHttpVersion = "3.14.6"
    const val tomcatVersion = "9.0.50"
//    const val springVersion = "5.3.9"
    const val coroutinesVersion = "1.5.0"
}

//TODO : Remove all packages from vendor plugins that are not required anymore.
object Dependencies {
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    val retrofitJacksonConverter = "com.squareup.retrofit2:converter-jackson:${Versions.retrofitVersion}"
    val adapterRxJava = "com.squareup.retrofit2:adapter-rxjava:${Versions.retrofitVersion}"

    val autoValue = "com.google.auto.value:auto-value:1.7.4"
    val autoValueAnnotation = "com.google.auto.value:auto-value-annotations:1.7.4"

//    val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpVersion}"

    val slf4jSimple = "org.slf4j:slf4j-simple:1.7.25"
    val slf4jApi = "org.slf4j:slf4j-api:1.7.25"

    val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jacksonVersion}"

    val bouncyCastleProv = "org.bouncycastle:bcprov-jdk15on:1.62"
    val bouncyCastlePkix = "org.bouncycastle:bcpkix-jdk15on:1.62"

    val guava = "com.google.guava:guava:23.0"

    val rxJava = "io.reactivex:rxjava:1.2.2"

    val adapterCoroutine = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"

    val jedis = "redis.clients:jedis:2.9.0"
    val lettuce = "io.lettuce:lettuce-core:6.1.2.RELEASE"

    val orgJson = "org.json:json:20170516"

    val jsonSimple = "com.googlecode.json-simple:json-simple:1.1.1"

    val apacheHttpClient = "org.apache.httpcomponents:httpclient:4.5.3"
    val apacheHttpCore = "org.apache.httpcomponents:httpcore:4.4.7"

    val jacksonJoda =  "com.fasterxml.jackson.datatype:jackson-datatype-joda:2.9.5"
    val jacksonDataTypeJSON = "com.fasterxml.jackson.datatype:jackson-datatype-json-org:${Versions.jacksonVersion}"
    val jacksonjsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jacksonVersion}"

    val jacksonDataformatXml = "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Versions.jacksonVersion}"
    val jacksonDataformatCsv = "com.fasterxml.jackson.dataformat:jackson-dataformat-csv:${Versions.jacksonVersion}"

    val jsr305 = "com.google.code.findbugs:jsr305:3.0.2"

    val javaxServlet = "javax.servlet:javax.servlet-api:4.0.1"
    val javaxAnnotationApi = "javax.annotation:javax.annotation-api:1.3.2"
    val validationApi = "javax.validation:validation-api:2.0.1.Final"

    val tomcat = "org.apache.tomcat:tomcat-catalina:${Versions.tomcatVersion}"

    val commonsValidator = "commons-validator:commons-validator:1.4.0"

    val awsJavaSdk = "com.amazonaws:aws-java-sdk:1.12.39"

    val gson = "com.google.code.gson:gson:2.7"

    val hibernate = "org.hibernate:hibernate-core:5.3.6.Final"

    val jaxbApi = "javax.xml.bind:jaxb-api:2.3.1"
    val jaxWsRt = "com.sun.xml.ws:jaxws-rt:2.3.5"
    val jaxWsTools = "com.sun.xml.ws:jaxws-tools:2.2.10"
    val javaxJws = "javax.jws:jsr181-api:1.0-MR1"

    val mockito = "org.mockito:mockito-core:1.10.19"

    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    val kotlinReactive = "org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Versions.coroutinesVersion}"

//    val okio = "com.squareup.okio:okio:1.11.0"
//    val springWeb = "org.springframework:spring-web:${Versions.springVersion}"
}

object GroupIds {
    val headoutInternal = "com.headout.internal"
}
