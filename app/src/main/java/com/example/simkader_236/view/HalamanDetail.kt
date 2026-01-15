package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simkader_236.viewmodel.DetailUiState
import com.example.simkader_236.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    role: String,
    viewModel: DetailViewModel,
    navigateBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.detailUiState
    val warnaUtama = Color(0xFFB71C1C)

    // State untuk Dialog Konfirmasi
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus data kader ini?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteKader(onSuccess = navigateBack)
                }) {
                    Text("Hapus", color = warnaUtama, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil Kader", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaUtama)
            )
        },
        floatingActionButton = {
            if (role == "admin" && uiState is DetailUiState.Success) {
                FloatingActionButton(
                    onClick = { onEditClick(uiState.kader.id_kader) },
                    containerColor = warnaUtama,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFEBEE))
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState) {
                is DetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = warnaUtama)
                    }
                }
                is DetailUiState.Success -> {
                    // Header Section
                    Box(
                        modifier = Modifier.fillMaxWidth().height(180.dp)
                            .background(Brush.verticalGradient(listOf(warnaUtama, Color(0xFFD32F2F)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.White)
                            Text(uiState.kader.nama.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }

                    Card(
                        modifier = Modifier.padding(16.dp).offset(y = (-30).dp).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(all = 20.dp)) {
                            // 1. Pastikan nama fungsi adalah DetailItemRow (satu 'l')
                            DetailItemRow(label = "NIM", value = uiState.kader.nim, icon = Icons.Default.Badge)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            DetailItemRow(label = "Program Studi", value = uiState.kader.prodi, icon = Icons.Default.School)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            // 2. Tambahkan .toString() pada angkatan agar tidak error
                            DetailItemRow(label = "Angkatan", value = uiState.kader.angkatan.toString(), icon = Icons.Default.DateRange)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            DetailItemRow(label = "Status Perkaderan", value = uiState.kader.status, icon = Icons.Default.Stars)
                        }
                    }

                    if (role == "admin") {
                        OutlinedButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = warnaUtama),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hapus Data Kader")
                        }
                    }
                }
                is DetailUiState.Error -> { /* Handle Error */ }
            }
        }
    }
}

// --- PASTIKAN FUNGSI DI BAWAH INI TERTULIS DENGAN BENAR DI PALING BAWAH FILE ---
@Composable
fun DetailItemRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFB71C1C), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}