package com.hoon.pokertimer.count

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hoon.pokertimer.databinding.ActivityCountBinding

class CountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountBinding

    // 설정 결과 받기 (Activity Result API)
    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val totalSeconds = result.data?.getIntExtra(SettingActivity.EXTRA_TOTAL_SECONDS, -1) ?: -1
                if (totalSeconds >= 0) {
                    saveSeconds(totalSeconds)
                    binding.timerText.text = formatMMSS(totalSeconds)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 처음 표시할 값 (저장된 값 불러오기, 없으면 10분 예시)
        val initial = loadSeconds()
        binding.timerText.text = formatMMSS(initial)

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra(SettingActivity.EXTRA_TOTAL_SECONDS, loadSeconds())
            settingsLauncher.launch(intent)
        }
    }

    private fun formatMMSS(totalSeconds: Int): String {
        val m = totalSeconds / 60
        val s = totalSeconds % 60
        return String.format("%02d:%02d", m, s)
    }

    private fun loadSeconds(): Int {
        val sp = getSharedPreferences("timer_prefs", MODE_PRIVATE)
        return sp.getInt("total_seconds", 10 * 60) // 기본 10:00
    }

    private fun saveSeconds(value: Int) {
        getSharedPreferences("timer_prefs", MODE_PRIVATE)
            .edit().putInt("total_seconds", value).apply()
    }
}
