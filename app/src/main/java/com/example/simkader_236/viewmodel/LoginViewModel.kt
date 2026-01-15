package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.repositori.RepositoriDataKader
import com.example.simkader_236.modeldata.AuthDataKader // WAJIB TAMBAHKAN IMPORT INI
import kotlinx.coroutines.launch

class LoginViewModel(private val repositori: RepositoriDataKader) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    fun login(onSuccess: (String) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            loginError = "Harap isi username dan password!"
            return
        }
        viewModelScope.launch {
            isLoading = true
            loginError = null
            // Di dalam LoginViewModel.kt
            try {
                val response = repositori.login(mapOf("username" to username, "password" to password))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        val userRole = body.user?.role ?: "user"
                        onSuccess(userRole) // Navigasi akan terpanggil di sini
                    } else {
                        loginError = body?.message ?: "Login Gagal"
                    }
                } else {
                    loginError = "Username atau Password salah!"
                }
            } catch (e: Exception) {
                // Log pesan aslinya ke Logcat untuk debug
                android.util.Log.e("LOGIN_ERROR", "Detail: ${e.message}")
                loginError = "Terjadi kesalahan sistem saat membaca data."
            } finally {
                isLoading = false
            }
        }
    }
}