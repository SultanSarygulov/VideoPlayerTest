package com.example.videoplayertest

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.browse.MediaBrowser
import android.os.Build.VERSION.SDK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    private var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        val fullscreenButton = findViewById<ImageView>(R.id.bt_fullscreen)

        val playerControlerLayout = findViewById<RelativeLayout>(R.id.playerController)

        val controllerParams: ViewGroup.LayoutParams = playerControlerLayout.layoutParams
        val playerParams: ViewGroup.LayoutParams = playerView.layoutParams


        fullscreenButton.setOnClickListener {
            if (!isFullScreen){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                playerParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                controllerParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

                playerParams.height = 300
                controllerParams.height = 300
            }
            isFullScreen = !isFullScreen
        }




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

        // Set right track to save user's data
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        exoPlayer = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri(URI)
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

    companion object {
        val URI = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    }

}