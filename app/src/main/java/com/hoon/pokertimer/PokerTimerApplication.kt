package com.hoon.pokertimer

import android.app.Application
import com.hoon.pokertimer.dao.BlindDao

class PokerTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BlindDao.init(this)
    }
}
