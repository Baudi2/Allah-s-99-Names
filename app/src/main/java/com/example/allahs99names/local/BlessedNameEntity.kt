package com.example.allahs99names.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = BLESSED_NAME_TABLE_NAME)
data class BlessedNameEntity(
    @PrimaryKey
    val arabicName: String
)

const val BLESSED_NAME_TABLE_NAME = "BLESSED_NAME_TABLE_NAME"
