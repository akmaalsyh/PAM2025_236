package com.example.simkader_236.apiservice

import com.example.simkader_236.modeldata.DataKader
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ServiceApiKader {

    // --- AUTENTIKASI ---

    @POST("register.php") // Berdasarkan SRS hal 11 (REQ-1)
    suspend fun registerKader(@Body kader: DataKader): Response<Void>

    @POST("login.php") // Berdasarkan SRS hal 11 (REQ-2)
    suspend fun login(@Body kredensial: Map<String, String>): Response<Void>

    // --- MANAJEMEN DATA KADER (CRUD) ---

    @GET("bacaKader.php") // Mengambil seluruh daftar kader (REQ-5)
    suspend fun getKader(): List<DataKader>

    @GET("detailKader.php") // Mengambil detail satu kader (hal 12)
    suspend fun getSatuKader(@Query("id_kader") id: Int): DataKader

    @POST("insertKader.php") // Menambah kader baru (REQ-4)
    suspend fun postKader(@Body kader: DataKader): Response<Void>

    @PUT("editKader.php") // Mengedit data kader (REQ-6)
    suspend fun editSatuKader(
        @Query("id_kader") id: Int,
        @Body kader: DataKader
    ): Response<Void>

    @DELETE("deleteKader.php") // Menghapus data kader (REQ-7)
    suspend fun hapusSatuKader(@Query("id_kader") id: Int): Response<Void>
}