package com.example.allahs99names.presentation.names_list_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahs99names.core.Empty
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NamesListViewModel @Inject constructor(private val repository: NamesListRepository) :
    ViewModel() {

    private val mutableState = MutableStateFlow(NamesListScreenState())
    val state = mutableState.asStateFlow()

    private var searchJob: Job? = null
    private var fullNamesList: ArrayList<FullBlessedNameEntity> = arrayListOf()

    val searchQuery = mutableStateOf(String.Empty)

    init {
        getNamesList()
    }

    private fun getNamesList() {
        viewModelScope.launch(Dispatchers.IO) {
            val namesList = repository.getAllNames()
            fullNamesList = namesList
            mutableState.update {
                it.copy(namesList = namesList)
            }
        }
    }

    fun changeSavedNameLearnedState(isLearned: Boolean, arabicName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isLearned) {
                repository.saveLearnedName(arabicName)
            } else {
                repository.removeLearnedName(arabicName)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotEmpty()) delay(SEARCH_DEBOUNCE_TIME)
            val searchedNames = searchNames(query)
            withContext(Dispatchers.Main) {
                mutableState.update { it.copy(namesList = ArrayList(searchedNames)) }
            }
        }
    }

    private fun searchNames(query: String): List<FullBlessedNameEntity> {
        return fullNamesList.filter { nameEntity ->
            query.lowercase().let { loweredQuery ->
                nameEntity.arabicVersion.lowercase().contains(loweredQuery) ||
                    nameEntity.transliteration.lowercase().contains(loweredQuery) ||
                    nameEntity.russianVersion.lowercase().contains(loweredQuery) ||
                    nameEntity.nameCount.toString().contains(loweredQuery)
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_TIME: Long = 350
    }
}
