package com.arissantas.drill

import android.app.Application
import androidx.room.Room
import com.arissantas.drill.data.DrillDb

class MainApplication : Application() {

  companion object {

    lateinit var drillDatabase: DrillDb
  }

  override fun onCreate() {
    super.onCreate()
    drillDatabase =
        Room.databaseBuilder(applicationContext, DrillDb::class.java, DrillDb.NAME).build()
  }
}
