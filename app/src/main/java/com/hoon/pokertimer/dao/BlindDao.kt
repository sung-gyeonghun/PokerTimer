package com.hoon.pokertimer.dao

import android.content.Context
import com.hoon.pokertimer.dto.Blind

object BlindDao {
    private const val PREF_NAME = "blind_pref"

    private lateinit var prefs: android.content.SharedPreferences

    private var blindAl = mutableListOf<Blind>()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // 최초 실행 or 저장된 값이 없으면 기본값 로딩
        if (!prefs.contains("small_0")) {
            blindAl = getDefaultBlinds().toMutableList()
            saveAll() // 바로 저장
        } else {
            loadAll()
        }
    }

    fun getDefaultBlinds(): List<Blind> = listOf(
        Blind(100, 200, 200),
        Blind(300, 500, 500),
        Blind(500, 1000, 1000),
        Blind(1000, 2000, 2000),
        Blind(1500, 3000, 3000),
        Blind(2000, 4000, 4000),
        Blind(2500, 5000, 5000),
        Blind(3000, 6000, 6000),
        Blind(4000, 8000, 8000),
        Blind(5000, 10000, 10000),
        Blind(6000, 12000, 12000),
        Blind(8000, 16000, 16000),
        Blind(10000, 20000, 20000),
        Blind(12000, 25000, 25000),
        Blind(15000, 30000, 30000),
        Blind(20000, 40000, 40000),
        Blind(25000, 50000, 50000),
        Blind(30000, 60000, 60000),
        Blind(40000, 80000, 80000),
        Blind(50000, 100000, 100000)
    )



    fun initBlinds() {
        blindAl = mutableListOf(
            Blind(100, 200, 200),
            Blind(300, 500, 500),
            Blind(500, 1000, 1000),
            Blind(1000, 2000, 2000),
            Blind(1500, 3000, 3000),
            Blind(2000, 4000, 4000),
            Blind(2500, 5000, 5000),
            Blind(3000, 6000, 6000),
            Blind(4000, 8000, 8000),
            Blind(5000, 10000, 10000),
            Blind(6000, 12000, 12000),
            Blind(8000, 16000, 16000),
            Blind(10000, 20000, 20000),
            Blind(12000, 25000, 25000),
            Blind(15000, 30000, 30000),
            Blind(20000, 40000, 40000),
            Blind(25000, 50000, 50000),
            Blind(30000, 60000, 60000),
            Blind(40000, 80000, 80000),
            Blind(50000, 100000, 100000)
        )
    }
    // -------------------- 저장 --------------------
    private fun saveAll() {
        val editor = prefs.edit()

        for (i in blindAl.indices) {
            editor.putInt("small_$i", blindAl[i].small)
            editor.putInt("big_$i", blindAl[i].big)
            editor.putInt("ante_$i", blindAl[i].ante)
        }

        editor.apply()
    }

    // -------------------- 불러오기 --------------------
    private fun loadAll() {
        val list = mutableListOf<Blind>()

        for (i in 0 until 20) {
            val small = prefs.getInt("small_$i", 0)
            val big = prefs.getInt("big_$i", 0)
            val ante = prefs.getInt("ante_$i", 0)
            list.add(Blind(small, big, ante))
        }

        blindAl = list
    }

    fun getBlind(index: Int): Blind {
        return blindAl[index]
    }

    // 🔥 새로 추가: 모든 블라인드 리스트 갱신용
    fun setBlinds(newList: List<Blind>) {
        blindAl = newList.toMutableList()
        saveAll()
    }

    // 🔥 (선택) 현재 리스트 가져오기
    fun getAllBlinds(): List<Blind> = blindAl
}
