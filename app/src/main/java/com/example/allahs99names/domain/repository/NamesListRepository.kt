package com.example.allahs99names.domain.repository

import com.example.allahs99names.domain.model.FullBlessedNameEntity

interface NamesListRepository {

    suspend fun getAllNames(): ArrayList<FullBlessedNameEntity>

    suspend fun saveLearnedName(arabicName: String)

    suspend fun removeLearnedName(arabicName: String)
}
