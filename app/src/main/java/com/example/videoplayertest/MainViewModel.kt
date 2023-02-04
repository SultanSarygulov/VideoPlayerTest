package com.example.videoplayertest

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(val player: ExoPlayer): ViewModel() {

    var isFullScreen = false
    var showUI = false

    var defaultHeight: Int? = null



    var playWhenReady = true
    var playbackPosition = 0L

    val playlist = listOf(
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

    )

    var currentItem: MediaItem? = null
    @androidx.media3.common.util.UnstableApi
    val firstItem = MediaItem.fromUri(playlist[0])
    var currentItemIndex = 0

    init {
        player.prepare()
    }

    @androidx.media3.common.util.UnstableApi
    fun playVideo(mediaItem: MediaItem){
//        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)

        player.playWhenReady = playWhenReady
        player.seekTo(currentItemIndex, playbackPosition)
        player.prepare()
    }

    override fun onCleared() {
        super.onCleared()

        player.release()
    }

}