package com.example.allahs99names.domain.repository

import com.example.allahs99names.domain.model.FullBlessedNameEntity

interface NamesListRepository {

    fun getAllNames(): ArrayList<FullBlessedNameEntity>
}
