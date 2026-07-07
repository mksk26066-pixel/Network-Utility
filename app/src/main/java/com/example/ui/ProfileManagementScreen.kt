package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ConfigProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileManagementScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showEditDialog by remember { mutableStateOf(false) }
    var editingProfile by remember { mutableStateOf<ConfigProfile?>(null) }

    if (showEditDialog) {
        ProfileEditDialog(
            profile = editingProfile,
            onDismiss = {
                showEditDialog = false
                editingProfile = null
            },
            onSave = { profile ->
                if (profile.id == 0) {
                    viewModel.insertProfile(profile)
                } else {
                    viewModel.updateProfile(profile)
                }
                showEditDialog = false
                editingProfile = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuration Profiles") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                editingProfile = null
                showEditDialog = true
            }) {
                Icon(Icons.Default.Add, "Add Profile")
            }
        }
    ) { padding ->
        if (state.profiles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No profiles configured.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(state.profiles, key = { it.id }) { profile ->
                    val isActive = profile.id == state.activeProfileId
                    ListItem(
                        headlineContent = { Text(profile.profileName, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text("${profile.serverDescription}\n${profile.httpsEndpointUrl}") },
                        leadingContent = {
                            IconButton(onClick = { viewModel.setActiveProfile(profile.id) }) {
                                Icon(
                                    imageVector = if (isActive) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                    contentDescription = "Set Active",
                                    tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = { 
                                    editingProfile = profile
                                    showEditDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, "Edit")
                                }
                                IconButton(onClick = { viewModel.deleteProfile(profile) }) {
                                    Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ProfileEditDialog(
    profile: ConfigProfile?,
    onDismiss: () -> Unit,
    onSave: (ConfigProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile?.profileName ?: "") }
    var description by remember { mutableStateOf(profile?.serverDescription ?: "") }
    var endpoint by remember { mutableStateOf(profile?.httpsEndpointUrl ?: "") }
    var tls by remember { mutableStateOf(profile?.tlsHostname ?: "") }
    var identifier by remember { mutableStateOf(profile?.accountIdentifier ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (profile == null) "New Profile" else "Edit Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Server Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = endpoint,
                    onValueChange = { endpoint = it },
                    label = { Text("HTTPS Endpoint URL") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tls,
                    onValueChange = { tls = it },
                    label = { Text("TLS Hostname") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text("Account Identifier / Secret") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onSave(
                            ConfigProfile(
                                id = profile?.id ?: 0,
                                profileName = name,
                                serverDescription = description,
                                httpsEndpointUrl = endpoint,
                                tlsHostname = tls,
                                accountIdentifier = identifier
                            )
                        )
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
