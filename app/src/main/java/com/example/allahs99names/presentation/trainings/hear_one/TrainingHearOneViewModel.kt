package com.example.allahs99names.presentation.trainings.hear_one

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import com.example.allahs99names.presentation.trainings.hear_one.TrainingOneHearState.Content
import com.example.allahs99names.presentation.trainings.hear_one.TrainingOneHearState.Nothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingHearOneViewModel @Inject constructor(private val repository: NamesListRepository) : ViewModel() {

    private val mutableState = MutableStateFlow<TrainingOneHearState>(Nothing)
    val state = mutableState.asStateFlow()

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch(Dispatchers.IO) {
            val allNames = repository.getAllNames()
            val namesToGuess = allNames.asSequence().shuffled().take(OPTIONS_TO_GUESS).toList()
            val correctOption = namesToGuess.random()

            mutableState.emit(
                Content(
                    nameToGuess = correctOption,
                    namesOptions = namesToGuess,
                    isCorrect = null
                )
            )
        }
    }

    fun checkSelectedOption(selectedName: FullBlessedNameEntity, correctName: FullBlessedNameEntity) {
        val isGuessed = selectedName == correctName
        if (mutableState.value is Content) {
            mutableState.update {
                (it as Content).copy(isCorrect = isGuessed)
            }
        }
    }

    private companion object {
        const val OPTIONS_TO_GUESS = 4
    }
}
