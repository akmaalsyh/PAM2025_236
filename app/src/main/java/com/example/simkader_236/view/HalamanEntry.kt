package com.example.simkader_236.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simkader_236.viewmodel.EntryViewModel
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntry(
    viewModel: EntryViewModel,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    val warnaUtama = Color(0xFFB71C1C)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Kader Baru", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = warnaUtama)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFEBEE))
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Informasi Personal",
                        fontWeight = FontWeight.Bold,
                        color = warnaUtama,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // 1. Input Nama Lengkap (REVISI: Akses melalui detailKader)
                    OutlinedTextField(
                        value = viewModel.uiStateKader.detailKader.nama,
                        onValueChange = { viewModel.updateUiState(viewModel.uiStateKader.detailKader.copy(nama = it)) },
                        label = { Text("Nama Lengkap") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = warnaUtama) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Input NIM (REVISI: Akses melalui detailKader)
                    OutlinedTextField(
                        value = viewModel.uiStateKader.detailKader.nim,
                        onValueChange = { viewModel.updateUiState(viewModel.uiStateKader.detailKader.copy(nim = it)) },
                        label = { Text("NIM") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = warnaUtama) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp) // Menggunakan HorizontalDivider (lebih modern)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Data Akademik & Kaderisasi",
                        fontWeight = FontWeight.Bold,
                        color = warnaUtama,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // 3. Dropdown Program Studi (REVISI: Akses melalui detailKader)
                    CustomDropdownMenu(
                        label = "Program Studi",
                        options = listOf("Teknologi Informasi", "Teknik Mesin", "Teknik Sipil", "Teknik Elektro", "Teknologi Elektro Medis", "Teknologi Rekayasa Otomotif"),
                        selectedOption = viewModel.uiStateKader.detailKader.prodi,
                        onOptionSelected = { viewModel.updateUiState(viewModel.uiStateKader.detailKader.copy(prodi = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Dropdown Angkatan (REVISI: Akses melalui detailKader)
                    CustomDropdownMenu(
                        label = "Angkatan",
                        options = listOf("2021", "2022", "2023", "2024", "2025"),
                        selectedOption = viewModel.uiStateKader.detailKader.angkatan,
                        onOptionSelected = { viewModel.updateUiState(viewModel.uiStateKader.detailKader.copy(angkatan = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 5. Dropdown Status Perkaderan (REVISI: Akses melalui detailKader)
                    CustomDropdownMenu(
                        label = "Status Kaderisasi",
                        options = listOf("Darul Arqam Dasar (DAD)", "Darul Arqam Madya (DAM)", "Darul Arqam Paripurna (DAP)"),
                        selectedOption = viewModel.uiStateKader.detailKader.status,
                        onOptionSelected = { viewModel.updateUiState(viewModel.uiStateKader.detailKader.copy(status = it)) },
                        warnaUtama = warnaUtama
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol Simpan (REVISI: Menggunakan isEntryValid dari ViewModel)
                    Button(
                        onClick = { viewModel.simpanKader(navigateBack) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = warnaUtama),
                        enabled = viewModel.uiStateKader.isEntryValid
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SIMPAN DATA KADER", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Pastikan data yang dimasukkan sudah benar sesuai database fakultas.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    warnaUtama: Color
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}