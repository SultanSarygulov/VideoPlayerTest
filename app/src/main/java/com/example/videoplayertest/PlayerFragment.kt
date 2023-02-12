package com.example.videoplayertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.youtube.player.*

class PlayerFragment : YouTubePlayerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = view.findViewById<YouTubePlayerView>(R.id.player_view)

        player.initialize(MainActivity.API_KEY, object: YouTubePlayer.OnInitializedListener{
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
            }

        })
    }
}