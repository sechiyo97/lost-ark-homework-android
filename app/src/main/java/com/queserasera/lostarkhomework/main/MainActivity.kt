package com.queserasera.lostarkhomework.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.common.GameCharacter
import com.queserasera.lostarkhomework.databinding.ActivityMainBinding
import com.queserasera.lostarkhomework.homework.HomeworkActivity
import com.queserasera.lostarkhomework.main.event.OnHomeworkClicked
import com.queserasera.lostarkhomework.main.event.OnMariClicked
import com.queserasera.lostarkhomework.mari.MariActivity
import com.queserasera.lostarkhomework.standard.GAME_CHARACTER

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = viewModel

        initViewModel()
        initAd()
    }

    private fun initViewModel() {
        val character = GameCharacter("별으잉", 53)// TODO: 현재 어댐터에서 선택된 캐릭터를 전달
        viewModel.event.observe(this, Observer<Event>{
            when(it) {
                OnMariClicked -> showMari()
                OnHomeworkClicked -> showHomework(character)
            }
        })
    }

    private fun initAd(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding?.arkAdViewBottom?.loadAd(adRequest)
    }

    ////////////////////////////////////////

    // 캐릭터 변경
    private fun changeCharacter(nextOne: Boolean) {
        /*characterIdx = if (nextOne) (characterIdx + 1) % 6 else if (characterIdx != 0) (characterIdx - 1) % 6 else 5
        showCharacter(characterIdx)*/
    }

    fun showCharacter(characterIdx: Int) {
        /*characterName = appData!!.getString("CHARACTER_IDX_$characterIdx", "이름없음")
        characterNameView!!.text = characterName*/
    }

    private fun editNameAlert() {
        AlertDialog.Builder(this)
                .setTitle("캐릭터 이름 변경")
                .setMessage("캐릭터 이름을 변경하시겠습니까?")
                .setPositiveButton("확인") { _, _ -> editName() }
                .setNegativeButton("취소") { _, _ -> }
                .show()
    }

    private fun editName() =
        Toast.makeText(this, "Testing...", Toast.LENGTH_SHORT).show()

    private fun showHomework(character: GameCharacter) {
        val intent = Intent(baseContext, HomeworkActivity::class.java)
        intent.putExtra(GAME_CHARACTER, character)
        startActivity(intent)
    }

    private fun showMari() {
        val intent = Intent(baseContext, MariActivity::class.java)
        startActivity(intent)
    }
}