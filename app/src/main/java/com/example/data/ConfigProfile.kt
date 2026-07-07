package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config_profiles")
data class ConfigProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileName: String,
    val serverDescription: String,
    val httpsEndpointUrl: String,
    val tlsHostname: String,
    val accountIdentifier: String,
    val isEnabled: Boolean = true
)
