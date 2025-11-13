package com.hoon.pokertimer.dao

import android.content.Context

object TimeDao {

    private const val PREF_NAME = "timer_pref"
    private const val KEY_TIME = "round_seconds"

    private lateinit var prefs: android.content.SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /** DAO처럼 메모리에서 가져오는 것처럼 보이지만 실제 저장소는 SharedPreferences */
    fun getTime(): Int {
        return prefs.getInt(KEY_TIME, 420)  // 기본 7분
    }

    fun setTime(seconds: Int) {
        prefs.edit().putInt(KEY_TIME, seconds).apply()
    }
}
