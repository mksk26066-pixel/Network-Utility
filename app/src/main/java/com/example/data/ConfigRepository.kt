package com.example.data

import kotlinx.coroutines.flow.Flow

class ConfigRepository(private val configDao: ConfigDao) {
    val allProfiles: Flow<List<ConfigProfile>> = configDao.getAllProfiles()

    fun getProfileById(id: Int): Flow<ConfigProfile?> = configDao.getProfileById(id)

    suspend fun insert(profile: ConfigProfile) = configDao.insertProfile(profile)
    
    suspend fun update(profile: ConfigProfile) = configDao.updateProfile(profile)

    suspend fun delete(profile: ConfigProfile) = configDao.deleteProfile(profile)
}
