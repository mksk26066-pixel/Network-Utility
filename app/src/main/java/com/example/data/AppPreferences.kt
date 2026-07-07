package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) {
    companion object {
        val ADMIN_PASSWORD = stringPreferencesKey("admin_password")
        val ACTIVE_PROFILE_ID = intPreferencesKey("active_profile_id")
        val IS_CONNECTED = booleanPreferencesKey("is_connected")
    }

    val adminPassword: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[ADMIN_PASSWORD] ?: "admin123" }

    val activeProfileId: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[ACTIVE_PROFILE_ID] ?: -1 }
        
    val isConnected: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_CONNECTED] ?: false }

    suspend fun setAdminPassword(password: String) {
        context.dataStore.edit { settings ->
            settings[ADMIN_PASSWORD] = password
        }
    }

    suspend fun setActiveProfileId(id: Int) {
        context.dataStore.edit { settings ->
            settings[ACTIVE_PROFILE_ID] = id
        }
    }
    
    suspend fun setIsConnected(connected: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_CONNECTED] = connected
        }
    }
}
