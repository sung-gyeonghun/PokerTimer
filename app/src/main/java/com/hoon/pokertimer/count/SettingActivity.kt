package com.hoon.pokertimer.count

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hoon.pokertimer.dao.BlindDao
import com.hoon.pokertimer.dao.TimeDao
import com.hoon.pokertimer.databinding.ActivitySettingBinding

private const val TAG = "SettingActivity_싸피"
class SettingActivity : AppCompatActivity() {

    var Bdao = BlindDao
    var Tdao = TimeDao
    companion object {
        const val EXTRA_TOTAL_SECONDS = "extra_total_seconds"
    }

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Tdao.init(this)

        // 메뉴 숨기기
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // NumberPicker 설정 (0~59)
        with(binding.minPicker) {
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = true
        }
        with(binding.secPicker) {
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = true
        }

        // 전달받은 현재 값 분해해서 세팅
        val current = TimeDao.getTime() // 기본 10분
        binding.minPicker.value = (current / 60).coerceIn(0, 59)
        binding.secPicker.value = (current % 60).coerceIn(0, 59)

        binding.btnCancel.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val minutes = binding.minPicker.value
            val seconds = binding.secPicker.value
            val total = minutes * 60 + seconds
            // 00:00 ~ 59:59 허용 (total 0~3599)
            TimeDao.setTime(total)
            val intent = Intent(this, CountActivity::class.java)
            startActivity(intent)
        }

        binding.btnBlindSetting.setOnClickListener{
            val intent = Intent(this, BlindSettingActivity::class.java)
            startActivity(intent)

        }


    }

    override fun onResume() {
        super.onResume()

        for(i in 0 .. 19){
            Log.d(TAG, "small $i : ${Bdao.getBlind(i).small}, big $i : ${Bdao.getBlind(i).big}, ante $i : ${Bdao.getBlind(i).ante} ")
        }
    }
}
