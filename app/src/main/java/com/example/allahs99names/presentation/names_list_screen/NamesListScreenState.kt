package com.example.allahs99names.presentation.names_list_screen

import com.example.allahs99names.domain.model.FullBlessedNameEntity

data class NamesListScreenState(
    val namesList: ArrayList<FullBlessedNameEntity> = arrayListOf()
)
