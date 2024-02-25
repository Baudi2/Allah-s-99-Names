package com.example.allahs99names.presentation.trainings.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseTrainingViewModel<S : TrainingState>(
    private val nothingState: S
) : ViewModel() {

    private val mutableState = MutableStateFlow(nothingState)
    val state = mutableState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun dropState() {
        viewModelScope.launch {
            mutableState.emit(nothingState)
        }
    }

    protected suspend fun emitState(state: S) {
        mutableState.emit(state)
    }

    fun playSound(context: Context, @RawRes soundResId: Int, allowAudioLayering: Boolean = false) {
        // If there's already a sound playing, stop it and release the resources
        if (allowAudioLayering.not()) mediaPlayer?.release()

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

interface TrainingState
