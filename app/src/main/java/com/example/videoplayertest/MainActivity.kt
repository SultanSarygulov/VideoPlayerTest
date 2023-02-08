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
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayertest.databinding.ActivityMainBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var playerControlerLayout: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private var isFullScreen = false

    private lateinit var binding: ActivityMainBinding


//    private val controllerParams by lazy { playerControlerLayout.layoutParams}
    private val playerParams by lazy { playerView.layoutParams}


    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycle.addObserver(binding.youtubeView)

       binding.youtubeView.getPlayerUiController().showFullscreenButton(true)

        binding.youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoID = "wjNln9mXuTI"
                youTubePlayer.loadVideo(videoID, 1F)
            }

        })

    }

//    private fun setAdapter() {
//        adapter = PlaylistAdapter()
//        recyclerView.adapter = adapter
//
//
//        adapter.onItemClickListener = object : PlaylistAdapter.OnItemClickListener{
//            override fun onItemClick(link: String) {
//
//            }
//        }
//    }



//    @SuppressLint("InlinedApi")
//    private fun toggleSystemUi() {
//
//        val controller =  WindowInsetsControllerCompat(window, playerView)
//
//        if (isFullScreen){
//
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//            controller.hide(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//
//            playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//            recyclerView.visibility = View.GONE
//
//        } else {
//
//            WindowCompat.setDecorFitsSystemWindows(window, true)
//            controller.show(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//
//            recyclerView.visibility = View.VISIBLE
//        }
//    }

    companion object {

        const val TAG = "Chura"
    }

}

