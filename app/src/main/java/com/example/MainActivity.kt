package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.AppPreferences
import com.example.data.ConfigRepository
import com.example.ui.AppNavigation
import com.example.ui.AppViewModel
import com.example.ui.AppViewModelFactory
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "network-utility-db"
        ).build()
        
        val repository = ConfigRepository(database.configDao())
        val preferences = AppPreferences(applicationContext)
        val viewModelFactory = AppViewModelFactory(repository, preferences)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AppViewModel::class.java]

        lifecycleScope.launch {
            repository.allProfiles.collect { profiles ->
                if (profiles.isEmpty()) {
                    val defaultProfile = com.example.data.ConfigProfile(
                        profileName = "uBlock DNS Default",
                        serverDescription = "uBlock Premium DNS",
                        httpsEndpointUrl = "https://my.ublockdns.com/ph4u8qzp",
                        tlsHostname = "ph4u8qzp.dot.ublockdns.com",
                        accountIdentifier = "shy-wine-abstract-frozen"
                    )
                    repository.insert(defaultProfile)
                    preferences.setActiveProfileId(1) // Assuming first profile gets ID 1
                }
            }
        }

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
