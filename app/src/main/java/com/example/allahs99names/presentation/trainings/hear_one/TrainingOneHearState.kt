package com.example.allahs99names.presentation.trainings.hear_one

import com.example.allahs99names.domain.model.FullBlessedNameEntity

sealed class TrainingOneHearState {
    data class Content(
        val nameToGuess: FullBlessedNameEntity,
        val namesOptions: List<FullBlessedNameEntity>,
        val isCorrect: Boolean?
    ) : TrainingOneHearState()

    data object Nothing : TrainingOneHearState()
}
