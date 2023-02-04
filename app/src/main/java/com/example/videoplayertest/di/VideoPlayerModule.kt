package com.example.videoplayertest.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VideoPlayerModule {

    @androidx.media3.common.util.UnstableApi

    @Provides
    @ViewModelScoped
    fun getVideoPlayer(app: Application): ExoPlayer{
        val trackSelector = DefaultTrackSelector(app).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        return ExoPlayer.Builder(app)
            .setTrackSelector(trackSelector)
            .build()
    }
}