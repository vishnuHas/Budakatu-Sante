package com.budakattu.sante.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.budakattu.sante.data.model.VerificationRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface VerificationDao {
    @Query("SELECT * FROM verifications")
    fun getAllVerifications(): Flow<List<VerificationRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(verifications: List<VerificationRequest>)
}
