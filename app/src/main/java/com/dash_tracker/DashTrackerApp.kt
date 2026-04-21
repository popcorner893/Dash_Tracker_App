package com.dash_tracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Esta anotación enciende el motor de inyección de dependencias en toda la app
@HiltAndroidApp
class DashTrackerApp : Application()