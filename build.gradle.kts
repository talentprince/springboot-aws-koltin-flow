import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.7.0"
}

group = "org.weyoung"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val coroutinesVersion = "1.6.2"
val awsSdkVersion = "2.17.220"

noArg {
    annotation("software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean")
}

repositories {
    mavenCentral()
}

configure<DependencyManagementExtension> {
    imports {
        mavenBom("software.amazon.awssdk:bom:${awsSdkVersion}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework:spring-context")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("software.amazon.awssdk:sqs")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
