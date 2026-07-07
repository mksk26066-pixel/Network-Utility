package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppPreferences
import com.example.data.ConfigProfile
import com.example.data.ConfigRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppState(
    val profiles: List<ConfigProfile> = emptyList(),
    val activeProfileId: Int = -1,
    val isConnected: Boolean = false,
    val adminPassword: String = "admin123",
    val activeProfile: ConfigProfile? = null
)

class AppViewModel(
    private val repository: ConfigRepository,
    private val preferences: AppPreferences
) : ViewModel() {

    val uiState: StateFlow<AppState> = combine(
        repository.allProfiles,
        preferences.activeProfileId,
        preferences.isConnected,
        preferences.adminPassword
    ) { profiles, activeProfileId, isConnected, adminPassword ->
        val activeProfile = profiles.find { it.id == activeProfileId }
        AppState(
            profiles = profiles,
            activeProfileId = activeProfileId,
            isConnected = isConnected,
            adminPassword = adminPassword,
            activeProfile = activeProfile
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppState()
    )

    fun toggleConnection() {
        viewModelScope.launch {
            val currentState = uiState.value.isConnected
            preferences.setIsConnected(!currentState)
        }
    }

    fun setActiveProfile(id: Int) {
        viewModelScope.launch {
            preferences.setActiveProfileId(id)
        }
    }

    fun setAdminPassword(password: String) {
        viewModelScope.launch {
            preferences.setAdminPassword(password)
        }
    }

    fun insertProfile(profile: ConfigProfile) {
        viewModelScope.launch {
            repository.insert(profile)
        }
    }

    fun updateProfile(profile: ConfigProfile) {
        viewModelScope.launch {
            repository.update(profile)
        }
    }

    fun deleteProfile(profile: ConfigProfile) {
        viewModelScope.launch {
            repository.delete(profile)
            if (uiState.value.activeProfileId == profile.id) {
                preferences.setActiveProfileId(-1)
            }
        }
    }
}

class AppViewModelFactory(
    private val repository: ConfigRepository,
    private val preferences: AppPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
