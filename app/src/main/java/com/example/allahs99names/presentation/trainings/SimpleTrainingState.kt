package com.example.allahs99names.presentation.trainings

import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.presentation.trainings.utils.TrainingState

sealed class SimpleTrainingState : TrainingState {
    data class Content(
        val nameToGuess: FullBlessedNameEntity,
        val namesOptions: List<FullBlessedNameEntity>,
        val isCorrect: Boolean?
    ) : SimpleTrainingState()

    data object Nothing : SimpleTrainingState()
}
