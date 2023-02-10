package com.example.videoplayertest


import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class MainActivity : YouTubeBaseActivity() {


    @androidx.media3.common.util.UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val player = findViewById<YouTubePlayerView>(R.id.player_view)

        player.initialize(API_KEY, object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                youTubePlayerProvider: YouTubePlayer.Provider?,
                youTubePlayer: YouTubePlayer?,
                wasRestored: Boolean
            ) {

                youTubePlayer?.loadVideo("w9xfXsqIGKk")

            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })




    }


    companion object {

        const val TAG = "Chura"
        const val API_KEY = "AIzaSyADWY7wAq2firnBPX2qZYnJSYabjR5otEk"
    }

}

