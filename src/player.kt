package com.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.media.MediaPlayer
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class PlayerModel(private val context: Context) : ViewModel() {
    private val player_ = MutableStateFlow<MediaPlayer?>(null)
    val player: StateFlow<MediaPlayer?> = player_

    private val volume_ = MutableStateFlow(0.5F)
    val volume: StateFlow<Float> = volume_

    private val position_ = MutableStateFlow(0F)
    val position: StateFlow<Float> = position_

    private val isPlaying_ = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = isPlaying_

    private val duration_ = MutableStateFlow(0)
    val duration: StateFlow<Int> = duration_

    fun play(audioResId: Int) {
        player_.value?.release()
        player_.value = MediaPlayer.create(context, audioResId)
        resume()
    }

    fun resume() {
        player_.value?.start()
    }

    fun pause() {
        player_.value?.pause()
    }

    fun toggle() {
        if (isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun stop() {
        player_.value?.stop()
        player_.value?.release()
        player_.value = null
    }

    fun setPosition(position: Int) {
        player_.value?.seekTo(position)
    }

    fun setVolume(volume: Float) {
        volume_.value = volume
    }

    init {
        viewModelScope.launch {
            while (true) {
                val player = player_.value
                 if (player != null) {
                    position_.value = player.currentPosition.toFloat()
                    isPlaying_.value = player.isPlaying
                    duration_.value = player.duration
                    player.setVolume(volume_.value, volume_.value)
                } else {
                    isPlaying_.value = false
                }
                delay(100)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player_.value?.release()
    }
}

@Composable
fun PlayerControls(
    playerModel: PlayerModel,
    modifier: Modifier = Modifier,
) {
    val isPlaying by playerModel.isPlaying.collectAsState()
    val position by playerModel.position.collectAsState()
    val volume by playerModel.volume.collectAsState()
    val duration by playerModel.duration.collectAsState()

    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Position
        Slider(
            value = position,
            onValueChange = { playerModel.setPosition(it.toInt()) },
            valueRange = 0F..duration.toFloat(),
            modifier = Modifier.fillMaxWidth(),
        )

        // Volume
        Slider(
            value = volume,
            onValueChange = { playerModel.setVolume(it) },
            valueRange = 0F..1F,
            modifier = Modifier.fillMaxWidth(),
        )

        // Play/ Pause
        IconButton(
            onClick = {
                playerModel.toggle()
            },
            modifier = Modifier.height(80.dp),
        ) {
            Icon(
                painter = painterResource(
                    if (isPlaying) R.drawable.pause_circle_24px
                    else R.drawable.play_circle_24px
                ),
                contentDescription = "Toggle Audio",
                modifier = Modifier.size(80.dp),
            )
        }
    }
}
