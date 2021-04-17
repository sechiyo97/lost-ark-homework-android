package com.queserasera.lostarkhomework.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.databinding.ActivityMainBinding
import com.queserasera.lostarkhomework.homework.HomeworkActivity
import com.queserasera.lostarkhomework.main.event.*
import com.queserasera.lostarkhomework.mari.MariActivity
import com.queserasera.lostarkhomework.standard.ItemSnapHelper


class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private var binding: ActivityMainBinding? = null

    val snapHelper = ItemSnapHelper()

    private val characterAdapter = CharacterAdapter()
    private val llManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = viewModel

        binding?.arkCharacterList?.apply {
            adapter = characterAdapter
            layoutManager = llManager
            snapHelper.attachToRecyclerView(this)
        }

        initViewModel()
        initAd()
    }

    private fun initViewModel() {
        viewModel.event.observe(this){
            when(it) {
                OnCharactersLoaded -> {
                    characterAdapter.characterList = viewModel.characterList
                    characterAdapter.selectedIndex = 0
                    characterAdapter.notifyDataSetChanged()
                }
                OnMariClicked -> showMari()
                OnHomeworkClicked -> showHomework(0)
                OnArrowDownClicked -> {
                    characterAdapter.onArrowDownClicked()
                }
                OnArrowUpClicked -> {
                    characterAdapter.onArrowUpClicked()
                }
            }
        }
    }

    private fun initAd(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding?.arkAdViewBottom?.loadAd(adRequest)
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

    private fun showHomework(characterIdx: Int) {
        val intent = Intent(baseContext, HomeworkActivity::class.java)
        intent.putExtra("characterIdx", characterIdx)
        startActivity(intent)
    }

    private fun showMari() {
        val intent = Intent(baseContext, MariActivity::class.java)
        startActivity(intent)
    }
}