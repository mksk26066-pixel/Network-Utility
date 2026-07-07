package com.example.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPanelScreen(
    viewModel: AppViewModel,
    onNavigateToAdmin: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showAdminLogin by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }
    
    // Connection Timer
    var connectionTime by remember { mutableStateOf(0L) }
    
    LaunchedEffect(state.isConnected) {
        if (state.isConnected) {
            while (true) {
                delay(1000)
                connectionTime++
            }
        } else {
            connectionTime = 0L
        }
    }
    
    val formatTime = { timeInSeconds: Long ->
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    if (showAdminLogin) {
        AdminLoginDialog(
            correctPassword = state.adminPassword,
            onDismiss = { showAdminLogin = false },
            onSuccess = {
                showAdminLogin = false
                onNavigateToAdmin()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Network Utility", 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { showAdminLogin = true }
                            )
                        }
                    ) 
                },
                actions = {
                    IconButton(onClick = { showUserMenu = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        if (showUserMenu) {
            ModalBottomSheet(onDismissRequest = { showUserMenu = false }) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    Text("More", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    ListItem(headlineContent = { Text("Settings") }, leadingContent = { Icon(Icons.Default.Settings, null) }, modifier = Modifier.clickable { showUserMenu = false })
                    ListItem(headlineContent = { Text("About") }, leadingContent = { Icon(Icons.Default.Info, null) }, modifier = Modifier.clickable { showUserMenu = false })
                    ListItem(headlineContent = { Text("Contact Us") }, leadingContent = { Icon(Icons.Default.ContactSupport, null) }, modifier = Modifier.clickable { showUserMenu = false })
                    ListItem(headlineContent = { Text("Privacy Policy") }, leadingContent = { Icon(Icons.Default.Policy, null) }, modifier = Modifier.clickable { showUserMenu = false })
                    ListItem(headlineContent = { Text("Terms of Service") }, leadingContent = { Icon(Icons.Default.Description, null) }, modifier = Modifier.clickable { showUserMenu = false })
                    ListItem(headlineContent = { Text("Feedback") }, leadingContent = { Icon(Icons.Default.Feedback, null) }, modifier = Modifier.clickable { showUserMenu = false })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            Text(
                text = if (state.isConnected) "SECURE" else "DISCONNECTED",
                style = MaterialTheme.typography.headlineSmall,
                color = if (state.isConnected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (state.isConnected) formatTime(connectionTime) else "00:00:00",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Big Power Button
            val scale by animateFloatAsState(
                targetValue = if (state.isConnected) 1.05f else 1f,
                animationSpec = tween(500), label = ""
            )
            
            val buttonColor = if (state.isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            val iconColor = if (state.isConnected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .clickable { viewModel.toggleConnection() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "Toggle Connection",
                    modifier = Modifier.size(80.dp),
                    tint = iconColor
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Server Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Server",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Selected Server", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = state.activeProfile?.serverDescription ?: "Optimal Location (Auto)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminLoginDialog(
    correctPassword: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Admin Access") },
        text = {
            Column {
                Text("Enter administrator password:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        isError = false
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    isError = isError,
                    leadingIcon = { Icon(Icons.Default.Lock, null) }
                )
                if (isError) {
                    Text("Incorrect password", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (password == correctPassword) {
                        onSuccess()
                    } else {
                        isError = true
                    }
                }
            ) {
                Text("Login")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
