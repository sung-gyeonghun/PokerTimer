package com.hoon.pokertimer.count

import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hoon.pokertimer.R
import com.hoon.pokertimer.dao.BlindDao
import com.hoon.pokertimer.dao.TimeDao
import com.hoon.pokertimer.databinding.ActivityCountBinding

class CountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountBinding
    private var timer: CountDownTimer? = null

    private var currentLevel = 1
    private var remainingSeconds = 0
    private var isRunning = false

    // 🔥 Sound 관련 변수
    private lateinit var soundPool: SoundPool
    private var beepSoundId: Int = 0
    private var beepStreamId: Int = 0      // play()로 받은 실제 재생 스트림 ID
    private var isLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기시간 로드
        remainingSeconds = TimeDao.getTime()
        updateUI()

        // 화면 상단바 숨기기
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // ▶ 시작 / 일시정지 버튼
        binding.btnStart.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        // 🔙 뒤로가기
        binding.btnBack.setOnClickListener {
            finish()
        }

        // ⏱ BreakTime → Continue
        binding.btnContinue.setOnClickListener {
            binding.breakLayout.visibility = android.view.View.GONE
            goToNextLevelAndStart()
        }

        // 🔊 사운드 로딩
        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        beepSoundId = soundPool.load(this, R.raw.clock, 1)

        soundPool.setOnLoadCompleteListener { _, _, _ ->
            isLoaded = true
        }
    }

    /* -------------------------------------------------------------
       UI 업데이트 (타이머, 레벨, 블라인드)
    ------------------------------------------------------------- */
    private fun updateUI() {
        binding.timerText.text = formatMMSS(remainingSeconds)
        binding.levelText.text = "Level $currentLevel"

        val blind = BlindDao.getBlind(currentLevel - 1)
        binding.smallBlindText.text = "Small: ${blind.small}"
        binding.bigBlindText.text = "Big: ${blind.big}"
        binding.anteText.text = "Ante: ${blind.ante}"
    }

    /* -------------------------------------------------------------
       타이머 시작
    ------------------------------------------------------------- */
    private fun startTimer() {
        isRunning = true
        binding.btnStart.text = "PAUSE"

        timer = object : CountDownTimer(remainingSeconds * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                remainingSeconds = (ms / 1000).toInt()
                updateTimerUI()
            }

            override fun onFinish() {
                // 카운트다운 종료 시 0초로 설정 후 소리 멈추기
                remainingSeconds = 0
                stopBeepSound()
                handleTimerFinish()
            }
        }.start()
    }

    /* -------------------------------------------------------------
       UI 업데이트 + 효과음 처리
    ------------------------------------------------------------- */
    private fun updateTimerUI() {

        // 화면 타이머 갱신
        binding.timerText.text = formatMMSS(remainingSeconds)

        // 🔥 3초 남았을 때 "단 한 번만" 효과음 재생
        if (remainingSeconds == 3 && isLoaded) {
//            stopBeepSound()  // 혹시 모르니 기존 소리 먼저 정지
            beepStreamId = soundPool.play(beepSoundId, 1f, 1f, 0, 0, 1f) // ← streamId 저장
        }

        // 🔥 0초에서는 소리 강제 정지
        if (remainingSeconds >= 4) {
            stopBeepSound()
        }
    }

    /* -------------------------------------------------------------
       효과음 정지 (streamId 기반)
    ------------------------------------------------------------- */
    private fun stopBeepSound() {
        if (beepStreamId != 0) {
            soundPool.stop(beepStreamId)
            beepStreamId = 0
        }
    }

    /* -------------------------------------------------------------
       타이머 일시정지
    ------------------------------------------------------------- */
    private fun pauseTimer() {
        isRunning = false
        timer?.cancel()
        binding.btnStart.text = "START"
    }

    /* -------------------------------------------------------------
       타이머 종료 시 처리
    ------------------------------------------------------------- */
    private fun handleTimerFinish() {
        timer?.cancel()

        // 🔥 Break Time 구간
        if (currentLevel == 7 || currentLevel == 13) {
            showBreakTime()
        } else {
            goToNextLevelAndStart()
        }
    }

    /* -------------------------------------------------------------
       Break Time 화면 출력
    ------------------------------------------------------------- */
    private fun showBreakTime() {
        pauseTimer()
        binding.breakLayout.visibility = android.view.View.VISIBLE
    }

    /* -------------------------------------------------------------
       다음 레벨로 이동 + 타이머 재시작
    ------------------------------------------------------------- */
    private fun goToNextLevelAndStart() {
        if (currentLevel < 20) currentLevel++

        remainingSeconds = TimeDao.getTime()
        updateUI()
        startTimer()
    }

    /* -------------------------------------------------------------
       포맷: 00:00
    ------------------------------------------------------------- */
    private fun formatMMSS(total: Int): String {
        val m = total / 60
        val s = total % 60
        return String.format("%02d:%02d", m, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        timer?.cancel()
    }
}
