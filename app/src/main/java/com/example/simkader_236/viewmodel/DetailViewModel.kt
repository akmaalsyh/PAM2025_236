package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import com.example.simkader_236.uicontroller.route.DestinasiDetail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Status UI khusus untuk halaman Detail
sealed interface DetailUiState {
    data class Success(val kader: DataKader) : DetailUiState // REVISI: Nama variabel 'kader' agar cocok dengan View
    object Error : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiDetail.kaderIdArg])

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getKaderById()
    }

    fun getKaderById() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                DetailUiState.Success(kader)
            } catch (e: Exception) {
                DetailUiState.Error
            }
        }
    }

    // REVISI: Menggunakan viewModelScope dan callback onSuccess agar navigasi sinkron
    fun deleteKader(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repositoriDataKader.hapusSatuKader(kaderId)
                if (response.isSuccessful) {
                    onSuccess() // Navigasi kembali setelah hapus sukses
                }
            } catch (e: Exception) {
                // Handle error silakan tambah Toast jika perlu
            }
        }
    }
}