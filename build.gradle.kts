plugins {
    id("java")
}

group = "net.officer"
version = "1.0"

val lwjglVersion = "3.3.6"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
}

dependencies {
    // Core LWJGL modules
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")

    // Native bindings (important!)
    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:$lwjglNatives")

    implementation("com.google.code.gson:gson:2.13.1")
    implementation("io.github.spair:imgui-java-app:1.89.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
}

tasks.test {
    useJUnitPlatform()
}