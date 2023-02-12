package com.example.videoplayertest


import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.youtube.player.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val youtubePlayerFragment = YouTubePlayerSupportFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, youtubePlayerFragment).commit()


    }


    companion object {

        const val TAG = "Chura"
        const val API_KEY = "AIzaSyADWY7wAq2firnBPX2qZYnJSYabjR5otEk"
    }

}

