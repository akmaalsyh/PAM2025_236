package com.example.simkader_236.repositori

import android.app.Application
import android.content.Context
import com.example.simkader_236.apiservice.ServiceApiKader
import com.example.simkader_236.room.DatabaseKader
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

// Kontrak Container untuk seluruh aplikasi [cite: 669, 674]
interface AppContainer {
    val repositoriDataKader: RepositoriDataKader // Untuk MySQL
    val offlineRepositori: OfflineRepositoriKader     // Untuk Room
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // --- KONFIGURASI MYSQL (RETROFIT) ---
    // Sesuaikan baseUrl dengan folder htdocs Anda [cite: 743-746]
    private val baseUrl = "http://10.17.71.102/simkader/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    private val serviceApiKader: ServiceApiKader by lazy {
        retrofit.create(ServiceApiKader::class.java)
    }

    // Instance Repositori Jaringan [cite: 469, 472]
    override val repositoriDataKader: RepositoriDataKader by lazy {
        JaringanRepositoriDataKader(serviceApiKader)
    }

    // --- KONFIGURASI ROOM (DATABASE LOKAL) ---
    // Instance Repositori Offline sesuai logika praktikum Room
    override val offlineRepositori: OfflineRepositoriKader by lazy {
        OfflineRepositoriKader(
            kaderDao = DatabaseKader.getDatabase(context).kaderDao()
        )
    }
}

// Class Application sebagai Entry Point Utama [cite: 669, 674]
class AplikasiDataKader : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Menginisialisasi container dengan context aplikasi
        container = DefaultAppContainer(this)
    }
}