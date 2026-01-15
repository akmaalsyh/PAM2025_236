package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DetailKader
import com.example.simkader_236.modeldata.UIStateKader
import com.example.simkader_236.modeldata.toDataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch

class EntryViewModel(
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    // Menyimpan state UI untuk form input kader
    var uiStateKader by mutableStateOf(UIStateKader())
        private set

    // REVISI: Fungsi diperbarui agar lebih mudah menerima update per-kolom
    fun updateUiState(detailKader: DetailKader) {
        uiStateKader = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader) // Validasi otomatis setiap ada perubahan
        )
    }

    // Fungsi validasi: Memastikan semua kolom wajib diisi
    private fun validasiInput(uiState: DetailKader = uiStateKader.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank()
        }
    }

    // REVISI: Menggunakan viewModelScope agar proses simpan berjalan di background
    fun simpanKader(onSuccess: () -> Unit) {
        if (validasiInput()) {
            viewModelScope.launch {
                try {
                    // Mengirim data ke Repositori
                    val response = repositoriDataKader.postDataKader(
                        uiStateKader.detailKader.toDataKader()
                    )

                    if (response.isSuccessful) {
                        println("Sukses Tambah Data")
                        onSuccess() // Navigasi kembali jika berhasil
                    } else {
                        println("Gagal tambah data: ${response.message()}")
                    }
                } catch (e: Exception) {
                    println("Terjadi Kesalahan: ${e.message}")
                }
            }
        }
    }
}