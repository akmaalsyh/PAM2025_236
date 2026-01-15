package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.modeldata.DetailKader
import com.example.simkader_236.modeldata.UIStateKader
import com.example.simkader_236.modeldata.toDataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import retrofit2.Response

class EntryViewModel(
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    // Menyimpan state UI untuk form input kader [cite: 563]
    var uiStateKader by mutableStateOf(UIStateKader())
        private set

    // Fungsi untuk memperbarui state saat pengguna mengetik di TextField
    fun updateUiState(detailKader: DetailKader) {
        uiStateKader = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader) // Cek validasi setiap ada perubahan [cite: 573]
        )
    }

    // Fungsi validasi untuk memastikan semua field terisi (isNotBlank) [cite: 564-565]
    private fun validasiInput(uiState: DetailKader = uiStateKader.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank() // Memastikan data tidak kosong
        }
    }

    // Fungsi suspend untuk menyimpan data ke database MySQL melalui Repositori [cite: 575]
    suspend fun addKader() {
        if (validasiInput()) { // Hanya eksekusi jika input valid [cite: 576]
            try {
                // Mengonversi detail form ke model data dan mengirim ke server [cite: 577]
                val response: Response<Void> = repositoriDataKader.postDataKader(
                    uiStateKader.detailKader.toDataKader()
                )

                if (response.isSuccessful) {
                    println("Sukses Tambah Data: ${response.message()}") // Log keberhasilan [cite: 578-579]
                } else {
                    println("Gagal tambah data: ${response.errorBody()}") // Log kegagalan [cite: 580-581]
                }
            } catch (e: Exception) {
                println("Terjadi Kesalahan: ${e.message}")
            }
        }
    }
}