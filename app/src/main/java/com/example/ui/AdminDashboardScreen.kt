package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
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
                .padding(padding)
        ) {
            item {
                DashboardCategory("Core Management")
            }
            item {
                DashboardItem(
                    icon = Icons.Default.Dns,
                    title = "Configuration Profiles",
                    subtitle = "Manage secure endpoints and TLS hostnames",
                    onClick = onNavigateToProfiles
                )
            }
            item {
                DashboardItem(
                    icon = Icons.Default.Settings,
                    title = "System Settings",
                    subtitle = "Theme, Admin Security, Ads",
                    onClick = onNavigateToSettings
                )
            }
            item {
                DashboardCategory("Analytics & Logs")
            }
            item {
                DashboardItem(
                    icon = Icons.Default.Analytics,
                    title = "Traffic Analytics",
                    subtitle = "View bandwidth usage (Placeholder)",
                    onClick = { /* Placeholder */ }
                )
            }
            item {
                DashboardItem(
                    icon = Icons.Default.ListAlt,
                    title = "System Logs",
                    subtitle = "View connection events (Placeholder)",
                    onClick = { /* Placeholder */ }
                )
            }
            item {
                DashboardCategory("Data Management")
            }
            item {
                DashboardItem(
                    icon = Icons.Default.CloudUpload,
                    title = "Backup & Restore",
                    subtitle = "Export/Import configurations (Placeholder)",
                    onClick = { /* Placeholder */ }
                )
            }
        }
    }
}

@Composable
fun DashboardCategory(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun DashboardItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}
