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
import java.net.URI

class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var playerControlerLayout: RelativeLayout

    private val mainViewModel: MainViewModel by viewModels()

    private val controllerParams by lazy { playerControlerLayout.layoutParams}
    private val playerParams by lazy { playerView.layoutParams}


    private lateinit var adapter: PlaylistAdapter


    @androidx.media3.common.util.UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        playerControlerLayout = findViewById<RelativeLayout>(R.id.playerController)
        val fullscreenButton = findViewById<ImageView>(R.id.bt_fullscreen)

        mainViewModel.defaultHeight = playerParams.height

        setAdapter()

        fullscreenButton.setOnClickListener {

            Log.d(TAG, "isFullScreen: ${mainViewModel.isFullScreen}")

            Log.d(TAG, "requestedOrientation: $requestedOrientation")

            if (!mainViewModel.isFullScreen){

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                playerView.layoutParams.height = mainViewModel.defaultHeight!!
            }

            mainViewModel.isFullScreen = !mainViewModel.isFullScreen

//            releasePlayer()

        }
    }

    private fun setAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.playlist)
        adapter = PlaylistAdapter()
        recyclerView.adapter = adapter


        adapter.submitList(mainViewModel.playlist)

        @androidx.media3.common.util.UnstableApi
        adapter.onItemClickListener = object : PlaylistAdapter.OnItemClickListener{
            override fun onItemClick(link: String) {
                val mediaItem = MediaItem.fromUri(link)
                exoPlayer?.setMediaItem(mediaItem)

                mainViewModel.currentItemUri = link

                Log.d(TAG, "show ${mainViewModel.currentItem}")

            }
        }
    }

    @androidx.media3.common.util.UnstableApi
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23){
            initializePlayer()
            toggleSystemUi()
        }
    }
    @androidx.media3.common.util.UnstableApi
    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || exoPlayer == null){
            initializePlayer()
            toggleSystemUi()
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

                val uri = if (mainViewModel.currentItemUri == null){
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
                } else {
                    mainViewModel.currentItemUri
                }

                val mediaItem = MediaItem.fromUri(uri!!)
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

        if (mainViewModel.isFullScreen){

            WindowCompat.setDecorFitsSystemWindows(window, false)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {



            WindowCompat.setDecorFitsSystemWindows(window, true)
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {

        const val TAG = "Chura"
    }

}

