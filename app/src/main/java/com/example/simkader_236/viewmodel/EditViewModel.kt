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

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiEdit.kaderIdArg])

    // REVISI: Nama variabel diubah jadi kaderUiState agar cocok dengan View
    var kaderUiState by mutableStateOf(UIStateKader())
        private set

    init {
        viewModelScope.launch {
            try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                // Mengisi form dengan data lama dari database
                kaderUiState = kader.toUIStateKader(isEntryValid = true)
            } catch (e: Exception) {
                println("Error Ambil Data: ${e.message}")
            }
        }
    }

    // Memperbarui state saat input berubah
    fun updateUiState(detailKader: DetailKader) {
        kaderUiState = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader)
        )
    }

    private fun validasiInput(uiState: DetailKader = kaderUiState.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank()
        }
    }

    // REVISI: Nama fungsi jadi updateKader & pakai viewModelScope agar bisa navigasi
    fun updateKader(onSuccess: () -> Unit) {
        if (validasiInput()) {
            viewModelScope.launch {
                try {
                    val response = repositoriDataKader.editSatuKader(
                        kaderId,
                        kaderUiState.detailKader.toDataKader()
                    )

                    if (response.isSuccessful) {
                        println("Update Sukses")
                        onSuccess() // Menjalankan navigateBack dari View
                    } else {
                        println("Update Gagal: ${response.message()}")
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        }
    }
}