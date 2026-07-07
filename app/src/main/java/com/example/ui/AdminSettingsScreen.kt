package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var newPassword by remember { mutableStateOf("") }
    var bannerAds by remember { mutableStateOf(true) }
    var interstitialAds by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Security", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Change Admin Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (newPassword.isNotBlank()) {
                            viewModel.setAdminPassword(newPassword)
                            newPassword = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Password")
                }
            }
            
            item {
                HorizontalDivider()
            }
            
            item {
                Text("Monetization Placeholders", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ListItem(
                    headlineContent = { Text("Banner Ads") },
                    supportingContent = { Text("Show banners on user panel") },
                    trailingContent = {
                        Switch(checked = bannerAds, onCheckedChange = { bannerAds = it })
                    }
                )
                ListItem(
                    headlineContent = { Text("Interstitial Ads") },
                    supportingContent = { Text("Show on connect/disconnect") },
                    trailingContent = {
                        Switch(checked = interstitialAds, onCheckedChange = { interstitialAds = it })
                    }
                )
            }
        }
    }
}
