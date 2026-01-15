package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch

class ListKaderViewModel(private val repositoriDataKader: RepositoriDataKader) : ViewModel() {
    var listUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init { getAllKader() }

    fun getAllKader() { // PASTIKAN NAMA FUNGSI INI ADALAH getAllKader
        viewModelScope.launch {
            listUiState = try {
                HomeUiState.Success(repositoriDataKader.getDataKader())
            } catch (e: Exception) { HomeUiState.Error }
        }
    }
}