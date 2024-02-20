package com.example.allahs99names.presentation.trainings.listen

import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.presentation.trainings.utils.TrainingState

sealed class TrainingHearState : TrainingState {
    data class Content(
        val nameToGuess: FullBlessedNameEntity,
        val namesOptions: List<FullBlessedNameEntity>,
        val isCorrect: Boolean?
    ) : TrainingHearState()

    data object Nothing : TrainingHearState()
}
