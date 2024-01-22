package com.example.allahs99names.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BlessedNameDao {

    @Query("SELECT * FROM $BLESSED_NAME_TABLE_NAME")
    suspend fun getAllLearnedNames(): List<BlessedNameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearnedName(learnedName: BlessedNameEntity)

    @Query("DELETE FROM $BLESSED_NAME_TABLE_NAME WHERE arabicName = :nameId")
    suspend fun removeSingleName(nameId: String)
}
