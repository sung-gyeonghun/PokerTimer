package com.hoon.pokertimer.dao

import com.hoon.pokertimer.dto.Blind

object BlindDao {
    private var blindAl = mutableListOf(
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

    fun getBlind(index: Int): Blind {
        return blindAl[index]
    }

    // 🔥 새로 추가: 모든 블라인드 리스트 갱신용
    fun setBlinds(newList: List<Blind>) {
        blindAl = newList.toMutableList()
    }

    // 🔥 (선택) 현재 리스트 가져오기
    fun getAllBlinds(): List<Blind> = blindAl
}
