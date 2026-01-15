package com.example.simkader_236.repositori

import com.example.simkader_236.apiservice.ServiceApiKader
import com.example.simkader_236.modeldata.DataKader
import retrofit2.Response

// Interface tunggal untuk kontrak data Kader via Jaringan
interface RepositoriDataKader {
    suspend fun getDataKader(): List<DataKader>
    suspend fun postDataKader(dataKader: DataKader): Response<Void>
    suspend fun getSatuKader(id: Int): DataKader
    suspend fun editSatuKader(id: Int, dataKader: DataKader): Response<Void>
    suspend fun hapusSatuKader(id: Int): Response<Void>
}

// Implementasi yang menghubungkan interface dengan Service API
class JaringanRepositoriDataKader(
    private val serviceApiKader: ServiceApiKader
) : RepositoriDataKader {

    override suspend fun getDataKader(): List<DataKader> =
        serviceApiKader.getKader()

    override suspend fun postDataKader(dataKader: DataKader): Response<Void> =
        serviceApiKader.postKader(dataKader)

    override suspend fun getSatuKader(id: Int): DataKader =
        serviceApiKader.getSatuKader(id)

    override suspend fun editSatuKader(id: Int, dataKader: DataKader): Response<Void> =
        serviceApiKader.editSatuKader(id, dataKader)

    override suspend fun hapusSatuKader(id: Int): Response<Void> =
        serviceApiKader.hapusSatuKader(id)
}