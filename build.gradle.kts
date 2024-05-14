val funcName = "entityspawn2"
val group = "cn.cyanbukkit.${funcName}"
val version = "0.1"
val mainPlugin = "SiModuleGame"

bukkit {
    name = rootProject.name
    description = "我的世界第二代实体对战系列 类似于兵临城下 第一代是2022年哦~"
    authors = listOf("CyanBukkit")
    website = "https://cyanbukkit.cn"
    main = "${group}.cyanlib.launcher.CyanPluginLauncher"
    loadBefore = listOf(mainPlugin)
    apiVersion = "1.18"
}

plugins {
    java
    kotlin("jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    maven("https://nexus.cyanbukkit.cn/repository/maven-public/")
    maven("https://maven.elmakers.com/repository")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly(fileTree("libs") { include("*.jar") })

}

kotlin {
    jvmToolchain(8)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
}
