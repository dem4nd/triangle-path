plugins {
    scala
    application
    id("com.gradleup.shadow") version "9.3.0"
}

dependencies {
    implementation("org.scala-lang:scala3-library_3:3.3.3")

    testImplementation("org.scalatest:scalatest_3:3.2.19")
    testImplementation("org.scalatestplus:junit-5-12_3:3.2.19.0")
}

application {
    mainClass.set("triangle.MinTrianglePath")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "triangle.MinTrianglePath"
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("triangle")
    archiveClassifier.set("all")
    archiveVersion.set("")

    manifest {
        attributes["Main-Class"] = "triangle.MinTrianglePath"
    }
}
