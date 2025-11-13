package com.hoon.pokertimer.count

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hoon.pokertimer.R
import com.hoon.pokertimer.dao.BlindDao
import com.hoon.pokertimer.dto.Blind

class BlindSettingActivity : AppCompatActivity() {
    private val dao = BlindDao
    private lateinit var container: LinearLayout
    private val smallInputs = mutableListOf<EditText>()
    private val anteInputs = mutableListOf<EditText>()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val smalls = IntArray(20)
        val antes = IntArray(20)
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is LinearLayout && view.tag == "level_row") {
                val smallInput = view.getChildAt(1) as EditText
                val anteInput = view.getChildAt(3) as EditText
                val smallindex = smallInputs.indexOf(smallInput)
                val anteIndex = anteInputs.indexOf(anteInput)
                if (smallindex in 0 until 20)
                    smalls[smallindex] = smallInput.text.toString().toIntOrNull() ?: 0
                if (anteIndex in 0 until 20)
                    antes[anteIndex] = anteInput.text.toString().toIntOrNull() ?: 0
            }
        }
        outState.putIntArray("smalls", smalls)
        outState.putIntArray("antes", antes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind_setting)

        container = findViewById<LinearLayout>(R.id.container)

        // 전체화면 immersive 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // 🔹 상단바: 취소 / 제목 / 기본값
        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 0, 0, 20)
        }

        val cancelButton = Button(this).apply {
            text = "취소"
            textSize = 14f
            setTextColor(Color.WHITE)
            backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.darker_gray)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            params.marginEnd = 8
            layoutParams = params
            setOnClickListener { finish() } // 현재 액티비티 종료
        }

        val title = TextView(this).apply {
            text = "🃏 Blind Level Settings"
            textSize = 22f
            setTextColor(Color.parseColor("#E53935"))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        }

        val defaultButton = Button(this).apply {
            text = "기본값"
            textSize = 14f
            setTextColor(Color.WHITE)
            backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.holo_red_dark)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            params.marginStart = 8
            layoutParams = params
            setOnClickListener {
                Toast.makeText(context, "기본값으로 초기화되었습니다.", Toast.LENGTH_SHORT).show()
                dao.initBlinds()
                finish()
            }
        }

        topBar.addView(cancelButton)
        topBar.addView(title)
        topBar.addView(defaultButton)
        container.addView(topBar)

        // 컬럼 헤더 (Level / Small / Big / Ante)
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 10)
        }

        val headers = listOf("Level", "Small", "Big", "Ante")
        headers.forEach {
            val tv = TextView(this).apply {
                text = it
                textSize = 16f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }
            header.addView(tv)
        }
        container.addView(header)

        // 🔹 1~20 레벨 생성
        for (i in 1..20) {
            // 7→8, 13→14 사이에 Break Time 추가
            if (i == 8 || i == 14) {
                val breakText = TextView(this).apply {
                    text = "♨ Break Time and chip change $"
                    textSize = 18f
                    setTextColor(Color.parseColor("#E53935"))
                    gravity = Gravity.CENTER
                    setPadding(0, 14, 0, 10)
                }
                container.addView(breakText)
            }

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                setPadding(0, 6, 0, 6)
                tag = "level_row"   // ✅ 태그 추가
            }

            val levelText = TextView(this).apply {
                text = "Lv. $i"
                textSize = 14f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val smallInput = EditText(this).apply {
                setText(dao.getBlind(i - 1).small.toString())
                setHintTextColor(Color.GRAY)
                setTextColor(Color.WHITE)
                backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.darker_gray)
                gravity = Gravity.CENTER
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

// 🔥 여기 추가해야 함!!
            smallInputs.add(smallInput)


            val bigInput = EditText(this).apply {
                setText(dao.getBlind(i - 1).big.toString())
                setTextColor(Color.parseColor("#FF9233"))
                backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.darker_gray)
                gravity = Gravity.CENTER
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val anteInput = EditText(this).apply {
                setText(dao.getBlind(i - 1).ante.toString())
                setTextColor(Color.RED)
                backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.darker_gray)
                gravity = Gravity.CENTER
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                layoutParams =
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            anteInputs.add(anteInput)

            // small 값 변경 → big/ante 자동 계산
            smallInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val small = s?.toString()?.toIntOrNull() ?: 0
                    val big = small * 2
                    bigInput.setText(big.toString())
//                    anteInput.setText(big.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })


            row.addView(levelText)
            row.addView(smallInput)
            row.addView(bigInput)
            row.addView(anteInput)
            container.addView(row)
        }

        val restoredSmall = savedInstanceState?.getIntArray("smalls")
        val restoredAnte = savedInstanceState?.getIntArray("antes")
        if (restoredSmall != null) {
            for (i in restoredSmall.indices) {
                if (restoredSmall[i] != 0) {
                    smallInputs[i].setText(restoredSmall[i].toString())
                }
            }
        }

        if (restoredAnte != null) {
            for (i in restoredAnte.indices) {
                // ante는 0도 valid 값일 수 있으니 무조건 세팅
                anteInputs[i].setText(restoredAnte[i].toString())
            }
        }

        // 저장 버튼
        val saveButton = Button(this).apply {
            text = "저장하기"
            textSize = 16f
            setTextColor(Color.WHITE)
            backgroundTintList =
                ContextCompat.getColorStateList(context, android.R.color.holo_red_dark)
            setPadding(0, 20, 0, 20)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 40
            params.bottomMargin = 60
            layoutParams = params
        }

        saveButton.setOnClickListener {
            val updatedList = mutableListOf<Blind>()
            var levelCount = 0

            for (i in 0 until container.childCount) {
                val view = container.getChildAt(i)

                // ✅ 태그가 "level_row"인 것만 처리
                if (view is LinearLayout && view.tag == "level_row") {
                    val smallInput = view.getChildAt(1) as EditText
                    val bigInput = view.getChildAt(2) as EditText
                    val anteInput = view.getChildAt(3) as EditText

                    val small = smallInput.text.toString().toIntOrNull() ?: 0
                    val big = bigInput.text.toString().toIntOrNull() ?: (small * 2)
                    val ante = anteInput.text.toString().toIntOrNull() ?: big

                    if (small == 0) {
                        Toast.makeText(this, "Lv.${levelCount + 1}의 Small 값을 입력하세요.", Toast.LENGTH_SHORT).show()
                        smallInput.requestFocus()
                        return@setOnClickListener
                    }

                    updatedList.add(Blind(small, big, ante))
                    levelCount++
                }
            }

            if (updatedList.size == 20) {
                dao.setBlinds(updatedList)
                Toast.makeText(this, "블라인드 설정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "저장이 올바르지 않습니다 (${updatedList.size}/20)", Toast.LENGTH_SHORT).show()
            }
        }



        container.addView(saveButton)
    }



}
