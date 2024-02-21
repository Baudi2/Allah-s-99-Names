package com.example.allahs99names.presentation.trainings.match_pair

import androidx.lifecycle.viewModelScope
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchState.Content
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchState.Nothing
import com.example.allahs99names.presentation.trainings.utils.BaseTrainingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingMatchViewModel @Inject constructor(
    private val repository: NamesListRepository
) : BaseTrainingViewModel<TrainingMatchState>(Nothing) {

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch(Dispatchers.IO) {
            val allNames = repository.getAllNames()
            val namesToGuess = allNames.asSequence().shuffled().take(NAMES_TO_MATCH_COUNT).toList()

            emitState(
                Content(
                    namesToGuess = namesToGuess,
                    namesToGuessShuffled = namesToGuess.asSequence().shuffled().toList(),
                    isComplete = null,
                    guessedNames = setOf()
                )
            )
        }
    }

    fun removeErrorModal() {
        viewModelScope.launch {
            val state = state.value
            if (state is Content) {
                emitState(
                    state.copy(isComplete = null)
                )
            }
        }
    }

    fun matchPair(arabicVersion: FullBlessedNameEntity, translationVersion: FullBlessedNameEntity) {
        viewModelScope.launch {
            val state = state.value
            if (state is Content) {
                if (arabicVersion == translationVersion) {
                    val newGuessedList = mutableSetOf(arabicVersion).apply {
                        addAll(state.guessedNames)
                    }

                    if (newGuessedList.size == NAMES_TO_MATCH_COUNT) {
                        emitState(state.copy(isComplete = true))
                        return@launch
                    }

                    emitState(
                        state.copy(
                            guessedNames = newGuessedList
                        )
                    )
                } else {
                    emitState(
                        state.copy(
                            isComplete = false
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val NAMES_TO_MATCH_COUNT = 5
    }
}
