plugins {
    base // Plugin dasar
}

// Definisikan letak file aar kamu
val myLibrary = file("sequre-android-sdk.aar")

// Beritahu Gradle bahwa artifact output-nya adalah file aar tersebut
artifacts {
    add("default", myLibrary)
}

// Opsional: Buat group dan version default (bisa ditimpa oleh Tag Git nanti)
group = "id.sequre"
version = "1.0.1"