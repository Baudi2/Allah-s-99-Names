package com.example.allahs99names.presentation.trainings

import androidx.lifecycle.viewModelScope
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Content
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Nothing
import com.example.allahs99names.presentation.trainings.utils.BaseTrainingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimpleTrainingViewModel @Inject constructor(
    private val repository: NamesListRepository
) : BaseTrainingViewModel<SimpleTrainingState>(Nothing) {

    fun loadContent(namesToLoad: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val allNames = repository.getAllNames()
            val namesToGuess = allNames.asSequence().shuffled().take(namesToLoad).toList()
            val correctOption = namesToGuess.random()

            emitState(
                Content(
                    nameToGuess = correctOption,
                    namesOptions = namesToGuess,
                    isCorrect = null
                )
            )
        }
    }

    fun checkSelectedOption(selectedName: FullBlessedNameEntity, correctName: FullBlessedNameEntity) {
        viewModelScope.launch {
            val isGuessed = selectedName == correctName
            if (state.value is Content) {
                emitState(
                    (state.value as Content).copy(isCorrect = isGuessed)
                )
            }
        }
    }
}
