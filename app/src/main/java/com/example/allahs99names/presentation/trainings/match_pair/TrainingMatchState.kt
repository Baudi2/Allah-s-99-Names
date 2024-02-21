package com.example.allahs99names.presentation.trainings.match_pair

import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.presentation.trainings.utils.TrainingState

sealed class TrainingMatchState : TrainingState {

    data class Content(
        val namesToGuess: List<FullBlessedNameEntity>,
        val namesToGuessShuffled: List<FullBlessedNameEntity>,
        val isComplete: Boolean?,
        val guessedNames: Set<FullBlessedNameEntity>
    ) : TrainingMatchState()

    data object Nothing : TrainingMatchState()
}
