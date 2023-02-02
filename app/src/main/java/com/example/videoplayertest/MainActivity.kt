package com.example.videoplayertest

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.icu.lang.UCharacter.JoiningGroup.BEH
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
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var playerControlerLayout: RelativeLayout

    private val mainViewModel: MainViewModel by viewModels()

    private val controllerParams by lazy { playerControlerLayout.layoutParams}
    private val playerParams by lazy { playerView.layoutParams}
    private var defaultHeight: Int? = null

    private lateinit var adapter: PlaylistAdapter

    private var isFullScreen = false
    private var showUI = false

    @androidx.media3.common.util.UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        playerControlerLayout = findViewById<RelativeLayout>(R.id.playerController)
        val fullscreenButton = findViewById<ImageView>(R.id.bt_fullscreen)

        defaultHeight = playerParams.height

        setAdapter()

        fullscreenButton.setOnClickListener {
            requestedOrientation = if (!isFullScreen){

                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            isFullScreen = !isFullScreen
            toggleSystemUi()

            Log.d(TAG, "initializePlayer: ")
        }
    }

    private fun setAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.playlist)
        adapter = PlaylistAdapter()
        recyclerView.adapter = adapter


        adapter.submitList(mainViewModel.playlist)

//        adapter.onItemClickListener = object : PlaylistAdapter.OnItemClickListener{
//            override fun onItemClick() {
//                TODO("Not yet implemented")
//            }
//
//        }
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

                exoPlayer.playWhenReady = mainViewModel.playWhenReady
                exoPlayer.seekTo(mainViewModel.currentItem, mainViewModel.playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        exoPlayer?.let { exoPlayer ->
            mainViewModel.playbackPosition = exoPlayer.currentPosition
            mainViewModel.currentItem = exoPlayer.currentMediaItemIndex
            mainViewModel.playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        exoPlayer = null
    }

    @SuppressLint("InlinedApi")
    private fun toggleSystemUi() {

        val controller =  WindowInsetsControllerCompat(window, playerView)

        if (!showUI){

            WindowCompat.setDecorFitsSystemWindows(window, false)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        showUI = !showUI
    }

    companion object {
        val URI = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        const val TAG = "Chura"
    }

}

