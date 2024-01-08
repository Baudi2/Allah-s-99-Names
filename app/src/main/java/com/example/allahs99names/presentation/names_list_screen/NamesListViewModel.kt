package com.example.allahs99names.presentation.names_list_screen

import androidx.lifecycle.ViewModel
import com.example.allahs99names.domain.repository.NamesListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NamesListViewModel @Inject constructor(private val repository: NamesListRepository) :
    ViewModel() {

    private val mutableState = MutableStateFlow(NamesListScreenState())
    val state = mutableState.asStateFlow()

    init {
        getNamesList()
    }

    private fun getNamesList() {
        val namesList = repository.getAllNames()
        mutableState.update {
            it.copy(namesList = namesList)
        }
    }
}