package com.example.allahs99names.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BlessedNameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BlessedNameDatabase: RoomDatabase() {
    abstract fun blessedNameDao(): BlessedNameDao

    companion object {
        const val BLESSED_NAME_DATABASE = "BLESSED_NAME_DATABASE"
    }
}
