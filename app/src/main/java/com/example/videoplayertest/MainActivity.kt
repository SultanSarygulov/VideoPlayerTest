package com.example.videoplayertest

import android.annotation.SuppressLint
import android.media.browse.MediaBrowser
import android.os.Build.VERSION.SDK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       playerView = findViewById<PlayerView>(R.id.player_view)
        val fullscreenButton = findViewById<ImageView>(R.id.bt_fullscreen)

//        val simpleExoPlayer = SimpleExoPlayer.Builder(this)
//            .setSeekBackIncrementMs(5000)
//            .setSeekForwardIncrementMs(5000)
//            .build()


    }
    @androidx.media3.common.util.UnstableApi
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23){
            initializePlayer()
        }
    }
    @androidx.media3.common.util.UnstableApi
    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || exoPlayer == null){
            initializePlayer()
        }

    }


    @androidx.media3.common.util.UnstableApi
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    @androidx.media3.common.util.UnstableApi
    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @androidx.media3.common.util.UnstableApi
    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        exoPlayer?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        exoPlayer = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}