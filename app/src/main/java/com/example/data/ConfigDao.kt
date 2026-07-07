package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM config_profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<ConfigProfile>>

    @Query("SELECT * FROM config_profiles WHERE id = :id LIMIT 1")
    fun getProfileById(id: Int): Flow<ConfigProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ConfigProfile)

    @Update
    suspend fun updateProfile(profile: ConfigProfile)

    @Delete
    suspend fun deleteProfile(profile: ConfigProfile)
}
