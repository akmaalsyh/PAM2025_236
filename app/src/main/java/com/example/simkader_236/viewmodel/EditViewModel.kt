package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DetailKader
import com.example.simkader_236.modeldata.UIStateKader
import com.example.simkader_236.modeldata.toDataKader
import com.example.simkader_236.modeldata.toUIStateKader
import com.example.simkader_236.repositori.RepositoriDataKader
import com.example.simkader_236.uicontroller.route.DestinasiEdit
import kotlinx.coroutines.launch
import retrofit2.Response

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    // Mendapatkan ID Kader dari argumen navigasi (itemId) [cite: 597]
    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiEdit.kaderIdArg])

    // State untuk menampung data yang sedang diedit di form [cite: 596]
    var uiStateKader by mutableStateOf(UIStateKader())
        private set

    init {
        // Mengambil data awal dari MySQL untuk ditampilkan di form [cite: 597-598]
        viewModelScope.launch {
            try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                // Mengubah model data menjadi UI State agar muncul di TextField [cite: 599-601]
                uiStateKader = kader.toUIStateKader(isEntryValid = true)
            } catch (e: Exception) {
                // Log error jika gagal mengambil data
            }
        }
    }

    // Memperbarui state setiap kali input di TextField berubah [cite: 602-603]
    fun updateUiState(detailKader: DetailKader) {
        uiStateKader = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader)
        )
    }

    // Validasi agar field tidak kosong [cite: 607-610]
    private fun validasiInput(uiState: DetailKader = uiStateKader.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank()
        }
    }

    // Fungsi untuk mengirim perubahan data ke database MySQL (Update) [cite: 612]
    suspend fun editKader() {
        if (validasiInput(uiStateKader.detailKader)) {
            try {
                // Memanggil API editKader.php melalui repositori [cite: 614]
                val response: Response<Void> = repositoriDataKader.editSatuKader(
                    kaderId,
                    uiStateKader.detailKader.toDataKader()
                )

                if (response.isSuccessful) {
                    println("Update Sukses: ${response.message()}")
                } else {
                    println("Update Gagal: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
}