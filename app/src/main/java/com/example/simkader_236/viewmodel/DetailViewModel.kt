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
    data class Success(val dataKader: DataKader) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    // Mengambil ID Kader dari argumen navigasi (itemId)
    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiDetail.kaderIdArg])

    // State UI untuk mengontrol tampilan di layar detail
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getKaderById()
    }

    // Fungsi untuk mengambil detail kader dari MySQL berdasarkan ID
    fun getKaderById() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                DetailUiState.Success(kader)
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }
    }

    // Fungsi tambahan untuk menghapus data kader (REQ-7 di SRS)
    suspend fun deleteKader() {
        try {
            repositoriDataKader.hapusSatuKader(kaderId)
        } catch (e: Exception) {
            // Handle error
        }
    }
}