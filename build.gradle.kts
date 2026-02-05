import groovy.util.Node

plugins {
    base // Plugin dasar
    id("maven-publish") // Plugin WAJIB agar task publishToMavenLocal tersedia
}

// Opsional: Buat group dan version default (bisa ditimpa oleh Tag Git nanti)
group = "id.sequre"

// Sesuaikan nama file .aar kamu di sini (harus SAMA PERSIS dengan file yang diupload)
val myAarFile = file("sequre-android-sdk.aar") 
val myArtifactId = "sequre-android-sdk" // Nama library saat dipanggil nanti

publishing {
    publications {
        create<MavenPublication>("release") {
            // Set Group ID (biasanya otomatis dari JitPack, tapi kita set defaultnya)
            groupId = project.group.toString()
            artifactId = myArtifactId
            version = "unspecified" // Nanti otomatis diganti JitPack sesuai Tag Git

            // Beritahu Gradle bahwa yang mau di-publish adalah file AAR mentah ini
            artifact(myAarFile)

            pom {
                    withXml {
                        asNode().appendNode("dependencies").apply {
                            addAllDependencies()
                        }
                    }
                }
        }
    }
}


// Helper function to append dependencies in Kotlin DSL
fun Node.appendDependency(
    groupId: String,
    artifactId: String,
    version: String,
    scope: String = "runtime"
) {
    appendNode("dependency").apply {
        appendNode("groupId", groupId)
        appendNode("artifactId", artifactId)
        appendNode("version", version)
        appendNode("scope", scope)
    }
}

// Helper function to add all dependencies
fun Node.addAllDependencies() {
    // coroutine
    appendDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.8.1")

    // html parser
    appendDependency(groupId = "org.jsoup", artifactId = "jsoup", version = "1.15.3")

    /// networking
    appendDependency("com.squareup.retrofit2", "retrofit", "2.11.0")
    appendDependency("com.squareup.retrofit2", "converter-gson", "2.11.0")
    appendDependency("com.squareup.okhttp3", "logging-interceptor", "4.11.0")

    /// camera
    appendDependency("androidx.camera", "camera-core", "1.4.0")
    appendDependency("androidx.camera", "camera-camera2", "1.4.0")
    appendDependency("androidx.camera", "camera-view", "1.4.0")
    appendDependency("androidx.camera", "camera-lifecycle", "1.4.0")
    appendDependency("androidx.camera", "camera-extensions", "1.4.0")

    /// coil
    appendDependency("io.coil-kt", "coil", "2.7.0")
    appendDependency("io.coil-kt", "coil-compose", "2.7.0")
    appendDependency("io.coil-kt", "coil-gif", "2.7.0")
    appendDependency("io.coil-kt", "coil-svg", "2.7.0")

    /// tensorflow
    appendDependency("com.google.ai.edge.litert", "litert", "1.4.1")
    appendDependency("com.google.ai.edge.litert", "litert-support", "1.4.1")

    // barcode decoder
    appendDependency("com.google.zxing", "core", "3.4.1")

    // Sentry
    appendDependency("io.sentry", "sentry-android", "8.4.0")
    appendDependency("io.sentry", "sentry-compose", "8.4.0")

    /// datastore
    appendDependency("androidx.datastore", "datastore-preferences", "1.1.1")
}
