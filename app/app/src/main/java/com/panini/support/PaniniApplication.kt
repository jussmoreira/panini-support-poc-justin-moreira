package com.panini.support

import android.app.Application
import com.panini.support.data.AppContainer
import com.panini.support.data.DefaultAppContainer

/**
 * Custom Application class that initializes the manual Dependency Injection container.
 */
class PaniniApplication : Application() {

    // AppContainer instance used by the rest of the classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
