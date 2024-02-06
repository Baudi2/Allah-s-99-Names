package com.example.allahs99names.presentation.trainings.hear_one

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrainingHearOneViewModel @Inject constructor(private val repository: NamesListRepository) : ViewModel() {

    private var allNames = ArrayList<FullBlessedNameEntity>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allNames = repository.getAllNames()
        }
    }


}