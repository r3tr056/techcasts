package com.xdivision.techcasts.ui.screens

import android.annotation.SuppressLint
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val audioUseCase: AudioUseCase,
    private val audioRepo: AudioRepo,
    private val disptcher: Dispatcher,
) : ViewModel() {

    init {
        subscribeToAudio()
        downloadAudio()
    }

    private fun subscribeToAudio() = viewModelScope.launch {
        val resource = audioUseCase.subscribeToService()
        if (resource is Resource.Success)
            handleAudio(resource.data ?: emptyList())
    }
}