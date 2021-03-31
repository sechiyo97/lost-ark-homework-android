package com.queserasera.lostarkhomework

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditNameActivity : AppCompatActivity() {
    /** Called when the activity is first created.  */
    private var mNameDisplay: EditText? = null
    private var mInputDone: Button? = null
    private var characterName: String? = null
    private var characterIdx = 0
    private var appData: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_name)

        // characterIdx 받아오기
        val intent = intent
        characterIdx = intent.getIntExtra("characterIdx", 0)

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)

        // 선택 완료 시 main으로
        mInputDone = findViewById<View>(R.id.inputDone) as Button
        mInputDone!!.setOnClickListener {
            save()
            showMain()
        }

        //// Edit Name: 시작 ////////////
        mNameDisplay = findViewById<View>(R.id.edit_name) as EditText
        characterName = appData?.getString("CHARACTER_IDX_$characterIdx", "이름없음")
        mNameDisplay!!.setText(characterName)
    }

    private fun showMain() = finish()

    // 설정값을 저장하는 함수
    private fun save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        val editor = appData!!.edit()
        characterName = mNameDisplay!!.text.toString()
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("CHARACTER_IDX_$characterIdx", characterName)
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply()
        (MainActivity.mContext as MainActivity).showCharacter(characterIdx)
    }
}