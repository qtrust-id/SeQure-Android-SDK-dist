# Scanner SDK

## ScannerView

Sebuah komponen Jetpack Compose yang menyediakan antarmuka pemindai dokumen yang lengkap. Komponen ini menangani pratinjau kamera, deteksi dokumen, pengambilan gambar, dan interaksi pengguna seperti kontrol lampu kilat (flash).

Composable ini dirancang agar sangat mudah disesuaikan melalui parameter-parameternya, memungkinkan penyesuaian tema dan integrasi ke dalam berbagai alur aplikasi.

### Consume SDK

#### Setup settings.gradle.kts

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
		    ...
        maven("https://jitpack.io") // Add this line
        ...
    }
}
```

#### Setup build.gradle.kts

```kotlin
dependencies {
    ...
    implementation("com.github.qtrust-id.SeQure-Android-SDK-dist:sequre-android-sdk:1.0.8")
    ...
}
```

#### Function Signature

```kotlin
fun ScannerView(
    applicationNumber: Int,
    bundlePackageId: String,
    sha: String? = null,
    onValidateAndCompatible: ((Boolean) -> Unit)? = null,
    scannerState: ScannerState = ScannerState.Scanning,
    onStateChanged: (ScannerState) -> Unit = {},
    isPaused: Boolean = false,
    geolocationKey: String? = null,
    onGeolocationResponse: (GeolocationResponse) -> Unit = {},
    showDetectedBoundary: Boolean = false,
    isFullScreen: Boolean = false,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    colors: ScannerViewColors = ScannerViewDefaults.defaultScannerViewColors,
    flashButton: @Composable ((() -> Unit, Boolean, Color) -> Unit),
    modifier: Modifier = Modifier,
    isInitialize: Boolean? = null,
    saveToGallery: Boolean = false,
    onNavigateBack: (() -> Unit)? = null,
    shouldShowBackButton: Boolean? = null,
): Unit
```

#### Parameter

| Parameter                 |                Type                | Description                                                                                                                                                                                                |
| ------------------------- | :--------------------------------: | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `applicationNumber`       |                Int                 | Your unique application identifier provided for using the SDK.                                                                                                                                             |
| `bundlePackageId`         |               String               | Bundle id for sdk validation, this is usually the package name of the app.                                                                                                                                 |
| `sha`                     |              String?               | Signing base app hash for sdk validation.                                                                                                                                                                  |
| `onValidateAndCompatible` |        ((Boolean) -> Unit)?        | An optional callback lambda with boolean return that is invoked when validation SDK and device compatibility state changed. Allow to control additiional UI from base app.                                 |
| `scannerState`            |            ScannerState            | Controls the current state of the scanner. You can pass states like ScannerState.CAPTURING to initiate a scan.                                                                                             |
| `onStateChanged`          |       (ScannerState) -> Unit       | A callback that notifies your application of changes in the scanner's state (e.g., when a document is detected or successfully captured). This is crucial for reacting to scanner events.                  |
| `isPaused`                |              Boolean               | Set to true to pause the camera preview and detection, and false to resume it. Useful for managing lifecycle events.                                                                                       |
| `geolocationKey`          |              String?               | Geolocation key provided by qtrust to developer. Allow SDK to provide it's own location when no location permission or GPS feature enabled                                                                 |
| `onGeolocationResponse`   | (GeolocationResponse) -> Unit = {} | Callback lambda with GeolocationResponse object return.                                                                                                                                                    |
| `showDetectedBoundary`    |              Boolean               | If true, an overlay will be drawn on the camera preview to visualize the detected object corners.                                                                                                          |
| `isFullScreen`            |              Boolean               | Determines the layout mode. Set to true to make the scanner occupy the entire screen, or false to embed it within another layout.                                                                          |
| `isDarkMode`              |              Boolean               | Determines the SDK color theme. Following system color theme on default.                                                                                                                                   |
| `colors`                  |         ScannerViewColors          | A data object that holds Color values for theming various UI elements like the detected boundary lines, buttons, and overlays.                                                                             |
| `flashButton`             |         @Composable lambda         | A slot for providing a custom Composable to be used as the flash button. The lambda gives you an onClick handler, the current flash status (isFlashOn), and a recommended tint color for UI consistency.   |
| `modifier`                |              Modifier              | Since the UI is already set in the SDK itself, use this modifier only for semantics. Except we set the full screen to false.                                                                               |
| `isInitialize`            |              Boolean?              | Determine if sdk should do validation and device compatibility check.                                                                                                                                      |
| `saveToGallery`           |              Boolean               | If true, the captured document image will be automatically saved to the device's public image gallery.                                                                                                     |
| `onNavigateBack`          |           (() -> Unit)?            | An optional callback lambda that is invoked when an internal back or close action is triggered from within the scanner UI. When this params filled with value, a back button arrow will appear in toolbar. |
| `shouldShowBackButton`    |              Boolean?              | A parameter to override back button arrow provided by `onNavigateBack` when needed.                                                                                                                        |

---

## Instalasi

Sebelum melakukan instalasi, persiapan beberapa credential penting yang akan disimpan pada file `secrets.properties`. Credential tersebut didapatkan dari pihak penyedia layanan Sequre, tanpa akses ini maka library tidak dapat digunakan.

#### Setup `secrets.properties`

Disini kita akan setup secrets.properties untuk menyimpan beberapa credential penting demi keamanan, yaitu:

- `SCANNER_APP_ID` untuk validasi SDK.
- `GEOLOCATION_KEY` untuk mengaktifkan fitur lokasi ketika akses lokasi dan GPS tidak diberikan.
  `SCANNER_SDK_USERNAME` dan `SCANNER_SDK_PASSWORD` untuk akses download library dari maven.
  Jangan lupa untuk menambahkan file `secrets.properties` ke `.gitignore` agar tidak terkirim ke repository dan hanya berada di lokal. Buat file `secrets.properties` pada `folder root project` dan isi dengan kode credential berikut:

```kotlin
SCANNER_APP_ID=
GEOLOCATION_KEY=
SCANNER_SDK_USERNAME=
SCANNER_SDK_PASSWORD=
```

Masukkan credential yang telah diberikan pada variabel yang sesuai.

### I. Instalasi Library

Untuk instalasi library scanner SDK, diperlukan `SCANNER_SDK_USERNAME` dan `SCANNER_SDK_PASSWORD` untuk mengaksesnya. Pada bagian instalasi ini lebih berfokus pada `file settings.gradle.kts`.

#### Akses `secrets.properties`

Buka file `settings.gradle.kts` dan tambahkan kode dibawah untuk memanggil credential didalam `secrets.properties`.

```kotlin
// Import extensions yang dibutuhkan
import java.util.Properties
import java.io.File

// Load properties
val secretsProp = Properties()
// Use 'settings.rootDir' disini
val secretsFile = File(settings.rootDir, "secrets.properties")

if (secretsFile.exists()) {
    // Gunakan .reader().use { ... } untuk safety
    secretsFile.reader().use { secretsProp.load(it) }
} else {
    println("WARNING: No secrets.properties found. Using environment variables if available.")
}
```

Kemudian pada bagian `dependencyResolutionManagement`, tambahkan kode untuk mengakses maven library scanner SDK beserta credential yang dipanggil dari `secrets.properties`

```kotlin

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Tambahkan maven berikut
        maven {
            url = uri("https://gitlab.com/api/v4/projects/62219224/packages/maven")
            credentials {
                username = secretsProp.getProperty("SCANNER_SDK_USERNAME")
                password = secretsProp.getProperty("SCANNER_SDK_PASSWORD")
            }
        }
    }
}
```

#### Konsumsi pada `build.gradle.kts`

Terakhir panggil library pada bagian dependencies seperti memanggil library lainnya:

```kotlin
implementation("id.sequre:scanner-sdk:1.0.8")
```

\*Untuk versi bisa melihat referensi pada package yang sudah dipublish atau mengikuti arahan yang telah diberikan sebelumnya saat mendapatkan credential.

### II. Implementasi Library

Compose pada android studio tidak dapat mengakses langsung credential pada `secrets.properties` seperti pada `settings.gradle` diatas, sehingga kita perlu menambahkan credential tersebut pada `BuildConfig` melalui `build.gradle.kts`.

#### Panggil `secrets,properties`

Tambahkan kode berikut pada bagian atas `build.gradle.kts`

```kotlin
// Import extensions yang dibutuhkan
import java.util.Properties

// Load properties
val secretsFile = rootProject.file("secrets.properties")
val secretsProp = Properties()
if (secretsFile.exists()) {
    secretsProp.load(secretsFile.reader())
} else {
    logger.warn("No secrets.properties found, using default or environment values")
}
```

#### Menambah `BuildConfig`

Di bagian paling bawah `defaultConfig`, tambahkan kode berikut:

```kotlin
applicationId = ...

....

buildConfigField(
    "String",
    "SCANNER_APP_ID",
    "\"${secretsProp.getProperty("SCANNER_APP_ID")}\""
)
buildConfigField(
    "String",
    "GEOLOCATION_KEY",
    "\"${secretsProp.getProperty("GEOLOCATION_KEY")}\""
)
```

Jalankan ulang gradle untuk instalasi library. Untuk berjaga-jaga jalankan build (gambar palu) agar konfigurasi terbaru ditambahkan ke `BuildConfig`.

#### Panggil `ScannerView`

Terakhir panggil compose ScannerView seperti dibawah dan gunakan `SCANNER_APP_ID`, `GEOLOCATION_KEY` dan packageName untuk melakukan validasi SDK. Untuk parameter lainnya dapat melihat referensi pada dokumentasi atau project contoh ynag diberikan.

```kotlin
ScannerView(
        applicationNumber = BuildConfig.SCANNER_APP_ID.toInt(),
        bundlePackageId = context.packageName,
        geolocationKey = BuildConfig.GEOLOCATION_KEY,
    )
```

---

## Dokumentasi ScannerState

`ScannerState` adalah sealed class yang mewakili berbagai status operasi pemindai dalam sebuah aplikasi. Ini memungkinkan cara yang jelas dan ringkas untuk mengelola UI dan logika bisnis berdasarkan status pemindaian saat ini.

#### Daftar Status:

|`Scanning` |
|`Processing` |
|`Error` |
|`Success` |

#### Rincian Status

Berikut adalah penjelasan rinci dari setiap status:

#### a. `data object Scanning`

Status ini menunjukkan bahwa **pemindai secara aktif mencari item yang dapat dipindai**. Selama pemindaian, ada kemungkinan deteksi pantulan cahaya (glare) yang dipicu saat menggunakan lampu kilat atau di bawah cahaya lampu. Ketika terpicu, ada jeda 3 detik untuk memungkinkan pengguna menyesuaikan posisi atau menghilangkan pantulan cahaya sebelum pemindai memindai kembali. Status ini dianggap selesai ketika lampu kilat menyala (jika dalam kondisi mati), layar menjadi kosong (jika dalam kondisi menyala), dan status berubah.

Selama status `Scanning`, ada beberapa pemeriksaan yang berjalan di latar belakang untuk memverifikasi gambar yang diambil dan nilai kode QR, memastikan kode QR yang dipindai adalah kode QR yang sebenarnya dari kami. Setelah pemeriksaan, ada jeda 200ms untuk mematikan lampu kilat (jika menyala) dan mengubah status ke `Processing` dengan teks "`Hold tight while we process your QR`" atau "`Memproses kode QR anda`".

#### b. `data class Processing(val text: String)`

Status ini menandakan bahwa gambar telah berhasil ditangkap, dan **aplikasi sedang memproses gambar dan data yang dipindai**. UI Anda mungkin menampilkan indikator loading atau placeholder selama fase ini untuk memberi tahu pengguna. Parameter `text` berisi string teks hasil dari pemindaian, yang digunakan untuk UI loading (agar proses lebih jelas). String teks akan berisi "`Uploading QR Image…`" atau "`Mengunggah Gambar QR…`" dalam bahasa Indonesia.

Setelah proses unggah dan panggilan API, SDK akan memantau hasil yang dikembalikan. Saat:

- `Error` terpicu, akan menampilkan toast yang bergantung pada kondisi hasil.

- `Success` terpicu, akan memicu penyimpanan ke galeri jika diaktifkan. Status akan berubah menjadi `Success` dengan hasil fallback ke `QR Number Unknown`.

#### c. `data class Error(val exception: Exception, val errorCode: Int? = null)`

Status ini mewakili **kegagalan selama bagian mana pun dari operasi pemindaian atau pemrosesan**.

- `exception`: Objek `Exception` aktual yang memberikan rincian tentang kesalahan.

- `errorCode`: Kode integer opsional yang dapat digunakan untuk identifikasi atau penanganan kesalahan yang lebih spesifik.

#### d. `data class Success(val scanResult: ScanResult, val bitmap: Bitmap?, val scanProductResponse: ScanProductResponse?)`

Status ini menunjukkan bahwa **pemindaian dan pemrosesan selanjutnya telah berhasil diselesaikan**.

`scanResult`: Objek yang berisi hasil utama dari pemindaian (misalnya, nilai barcode, data kode QR).

`bitmap`: Gambar `Bitmap` opsional dari item yang dipindai, jika ditangkap dan tersedia.

`scanProductResponse`: Objek opsional yang berisi respons dari pencarian produk atau panggilan API terkait, berdasarkan `scanResult`.

#### Contoh Penggunaan

Anda dapat mengamati `ScannerState` di lapisan UI Anda (misalnya, di ViewModel atau Activity/Fragment) untuk memperbarui antarmuka pengguna sesuai dengan itu:

```kotlin
// Contoh dalam ViewModel atau pengontrol UI

fun changeScannerState(newState: ScannerState) {
    updateScannerState(newState)
    viewModelScope.launch {
        when (newState) {
            is ScannerState.Error -> {
                when (val exception = newState.errorCode) {
                    // Tangani error berdasarkan kode ketika ada UI spesifik untuk ditampilkan
                    408 -> {
                        _scannedQRUiState.value = UiState.Error(
                            exception,
                            errorMessage = newState.exception.message.toString(),
                        )
                    }

                    // Fallback ke default jika kode error yang ditentukan tidak ada di atas
                    else -> {
                        _scannedQRUiState.value = UiState.Error(
                            null,
                            errorMessage = newState.exception.message.toString()
                        )
                    }
                }
            }

            is ScannerState.Processing -> {
                // SDK sedang memproses gambar, tangani dengan menampilkan loading atau UI lain untuk pengguna
                _scannedQRUiState.value = UiState.Loading
            }

            ScannerState.Scanning -> {
                // SDK dalam mode standby menunggu objek masuk ke dalam frame
                _scannedQRUiState.value = UiState.Idle
            }

            is ScannerState.Success -> {
                // SDK mengembalikan scanResult, bitmap, scanProductResponse
                setScanResult(state.scanResult.value)
                setBitmap(state.bitmap)
                setScanProductResponse(newState.scanProductResponse)
            }
        }
    }
}
```

---

## Dokumentasi ScannerViewColors

Kelas data yang mewakili skema warna untuk `ScannerView`. Kelas ini mendefinisikan warna yang digunakan untuk berbagai elemen UI di dalam antarmuka pemindai.

```kotlin
data class ScannerViewColors(
    val detectionFrameColor: Color = DarkBlue,
    val detectionFrameOptimalColor: Color = LimeGreen,
    val surfaceColor: Color = Color(0xFF232333),
    val onSurfaceColor: Color = Color.White,
    val appBarContainerColor: Color = Color(0xFF232333),
    val appBarContentColor: Color = Color.White,
    val glareIndicatorContainerColor: Color = Color(0xD94A4A4A),
    val glareIndicatorContentColor: Color = Color.White,
)
```

#### Parameter

| Parameter                      |  Tipe   | Deskripsi                                                                  |
| :----------------------------- | :-----: | :------------------------------------------------------------------------- |
| `detectionFrameColor`          | `Color` | Warna bingkai deteksi saat dokumen tidak berada pada posisi optimal.       |
| `detectionFrameOptimalColor`   | `Color` | Warna bingkai deteksi saat dokumen berada pada posisi optimal.             |
| `surfaceColor`                 | `Color` | Warna latar belakang permukaan tampilan pemindai.                          |
| `onSurfaceColor`               | `Color` | Warna konten (teks, ikon) yang ditampilkan di permukaan tampilan pemindai. |
| `appBarContainerColor`         | `Color` | Warna latar belakang app bar.                                              |
| `appBarContentColor`           | `Color` | Warna konten (teks, ikon) di dalam app bar.                                |
| `glareIndicatorContainerColor` | `Color` | Warna latar belakang indikator pantulan cahaya.                            |
| `glareIndicatorContentColor`   | `Color` | Warna konten (teks, ikon) di dalam indikator pantulan cahaya.              |

---

## Dokumentasi ScanResult

#### Parameter

| Tipe                | Deskripsi                                                                                                 |
| :------------------ | :-------------------------------------------------------------------------------------------------------- |
| `QR_GENUINE`        | Menunjukkan bahwa kode QR yang dipindai terverifikasi dan asli.                                           |
| `QR_POOR_IMAGE`     | Hasil pemindaian ini terpicu saat gambar yang diambil tidak cukup jelas atau baik untuk diklasifikasikan. |
| `QR_FAKE`           | Ketika kode QR yang dipindai bukan dari qtrust, menghasilkan status palsu.                                |
| `QR_NUMBER_UNKNOWN` | Terpicu saat domain valid dari qtrust, tetapi nomor QR berikutnya tidak terdaftar.                        |

---

## Dokumentasi ScanProductResponse

ScanProductResponse merupakan object yang berisi hasil-hasil indentifikasi dari proses klasifikasi. Pada bagian ini, kami tidak akan menjelaskan setiap variabel yang tersedia dari `ScanProductResponse` karena variabelnya sangat banyak. Jadi, di sini hanya akan dibahas variabel-variabel yang paling perlu diperhatikan. Jika diperlukan, silakan hubungi kami untuk penjelasan lebih rinci.

```kotlin
data class ScanProductResponse(
    val message: String? = null,
    val canvas: Canvas? = null,
    val emailSent: Boolean? = null,
    var qrcode: Qrcode? = null,
    val pid: String? = null,
    val classification: Classification? = null,
    val originals: Originals? = null,
    val obj: Object? = null,
)
```

#### a. ScanProductResponse

| Parameter        |       Tipe       | Deskripsi                              |
| :--------------- | :--------------: | :------------------------------------- |
| `qrcode`         |     `Qrcode`     | Objek kode QR yang dipindai.           |
| `pid`            |     `String`     | ID unik untuk setiap pemindaian.       |
| `classification` | `Classification` | Hasil klasifikasi dari model `tflite`. |

#### b. Qrcode

Objek hasil ini berisi data untuk kode QR.

| Parameter |   Tipe   | Deskripsi                                    |
| :-------- | :------: | :------------------------------------------- |
| `status`  | `String` | Status apakah kode QR terdeteksi atau tidak. |
| `text`    | `String` | Kode QR yang dipindai.                       |

#### c. Classification

Objek hasil ini terutama berasal dari model `tflite`, dan kami akan menggunakan variabel di dalamnya untuk menentukan alur selanjutnya.

| Parameter |   Tipe   | Deskripsi                                                                     |
| :-------- | :------: | :---------------------------------------------------------------------------- |
| `status`  | `String` | Status apakah model berhasil mendeteksi kode QR atau tidak.                   |
| `label`   | `String` | Ini adalah variabel terpenting, karena hasil akhir berasal dari variabel ini. |
| `score`   | `String` | Skor dari proses klasifikasi `tflite`.                                        |

Seperti yang saya sebutkan di tabel, variabel `label` adalah hasil akhir dari SDK ini. Hasilnya akan digunakan untuk menentukan alur aplikasi selanjutnya. Ada 3 variabel yang tersedia:

| Label     | Deskripsi                                                                                                 |
| :-------- | :-------------------------------------------------------------------------------------------------------- |
| `genuine` | Menunjukkan bahwa kode QR yang dipindai terverifikasi dan asli.                                           |
| `fake`    | Ketika kode QR yang dipindai bukan dari qtrust, hasilnya palsu.                                           |
| `poor`    | Hasil pemindaian ini terpicu saat gambar yang diambil tidak cukup jelas atau baik untuk diklasifikasikan. |

---

## Dokumentasi GeolocationResponse

This object represents the complete result of a successful geolocation request. It bundles the coordinates with a metric for how accurate that location fix is.

#### a. GeolocationResponse

| Parameter  |   Type    | Description                               |
| :--------- | :-------: | :---------------------------------------- |
| `accuracy` | `Double`  | Determine the accuracy from GeoData value |
| `location` | `GeoData` | An object contain latitude and longitude  |

#### b. GeoData

This is the core GeoData object that holds the actual coordinate pair.

| Parameter |   Type    | Description                   |
| :-------- | :-------: | :---------------------------- |
| `lat`     | `Double?` | Latitude with nullable value  |
| `lng`     | `Double?` | Longitude with nullable value |
