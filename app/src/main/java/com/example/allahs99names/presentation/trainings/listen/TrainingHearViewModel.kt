package com.example.allahs99names.presentation.trainings.listen

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.domain.repository.NamesListRepository
import com.example.allahs99names.presentation.trainings.listen.TrainingHearState.Content
import com.example.allahs99names.presentation.trainings.listen.TrainingHearState.Nothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingHearViewModel @Inject constructor(private val repository: NamesListRepository) : ViewModel() {

    private val mutableState = MutableStateFlow<TrainingHearState>(Nothing)
    val state = mutableState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun loadContent(namesToLoad: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val allNames = repository.getAllNames()
            val namesToGuess = allNames.asSequence().shuffled().take(namesToLoad).toList()
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

    fun dropState() {
        viewModelScope.launch {
            mutableState.emit(Nothing)
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

    fun playSound(context: Context, @RawRes soundResId: Int) {
        // If there's already a sound playing, stop it and release the resources
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            start()
        }
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
