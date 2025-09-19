package com.example.movieapp

import android.app.Application
import com.example.movieapp.di.AppContainer
import com.example.movieapp.di.DefaultAppContainer

class MoviesApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = this)
    }
}
