package com.queserasera.lostarkhomework

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HWActivity : Activity() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var appData: SharedPreferences? = null
    private var reloadButtonDaily: ImageView? = null
    private var reloadButtonWeekly: ImageView? = null
    private var characterIdx = 0

    // dataset: {name, total, current, enabled}
    var mAllHWList = arrayOf(arrayOf(arrayOf("에포나", "4"), arrayOf("카오스 던전", "4"), arrayOf("실리안의 지령서", "4"), arrayOf("이벤트 카던", "1"), arrayOf("길드출석", "1"), arrayOf("호감도", "1"), arrayOf("행운의 기운", "1")), arrayOf(arrayOf("레이드-1T", "4"), arrayOf("레이드-2T", "4"), arrayOf("레이드-3T", "4"), arrayOf("레이드-4T", "4"), arrayOf("레이드-5T", "4"), arrayOf("레이드-6T", "4"), arrayOf("레이드-7T", "4"), arrayOf("레이드-8T", "4")), arrayOf(arrayOf("주간 에포나", "4"), arrayOf("주간 레이드-1", "4"), arrayOf("주간 레이드-2", "4"), arrayOf("유령선", "1"), arrayOf("철새치", "1"), arrayOf("한파인양", "1")))
    private val mCategorySelector: Array<LinearLayout?> = arrayOfNulls(mAllHWList.size)

    // dataset
    var mAllEnabledData = arrayOf(BooleanArray(mAllHWList[0].size), BooleanArray(mAllHWList[1].size), BooleanArray(mAllHWList[2].size))
    var mAllCheckedData = arrayOf(Array(mAllHWList[0].size) { BooleanArray(4) }, Array(mAllHWList[1].size) { BooleanArray(4) }, Array(mAllHWList[2].size) { BooleanArray(4) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hw)
        val intent = intent
        characterIdx = intent.getIntExtra("characterIdx", 0)
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        reloadButtonDaily = findViewById<View>(R.id.reload_button_daily) as ImageView
        reloadButtonWeekly = findViewById<View>(R.id.reload_button_weekly) as ImageView

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView!!.setHasFixedSize(true)

        // use a linear layout manager
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager

        // set select buttons
        mCategorySelector[0] = findViewById<View>(R.id.daily_select) as LinearLayout
        mCategorySelector[1] = findViewById<View>(R.id.weekly_select) as LinearLayout
        mCategorySelector[2] = findViewById<View>(R.id.passive_select) as LinearLayout
        for (i in 0 until mAllHWList.size) {
            val idx: Int = i
            mCategorySelector[i]?.setOnClickListener { categorySelect(idx) }
        }
        categorySelect(0)
    }

    fun categorySelect(categoryIdx: Int) {
        for (i in 0..2) mCategorySelector[i]!!.setBackgroundColor(this.resources.getColor(R.color.white))
        mCategorySelector[categoryIdx]!!.setBackgroundColor(this.resources.getColor(R.color.colorAccent))
        reloadButtonDaily!!.setOnClickListener { //refreshAlert(categoryIdx); // daily로 변경
            refreshDailyAlert()
            loadChecked(categoryIdx)
        }
        reloadButtonWeekly!!.setOnClickListener { //refreshAlert(categoryIdx); // daily로 변경
            refreshWeeklyAlert()
            loadChecked(categoryIdx)
        }
        loadChecked(categoryIdx)
    }

    fun loadChecked(categoryIdx: Int) {
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)
        for (pos in 0 until mAllHWList[categoryIdx].size) {
            for (i in 0..3) {
                mAllCheckedData[categoryIdx][pos][i] = appData!!.getBoolean(
                        "CHARACTER_" + characterIdx.toString() + "_" +
                                "CHECKED_" + categoryIdx.toString() + "_" + pos.toString() + "_" + i.toString(), false)
            }
            mAllEnabledData[categoryIdx][pos] = appData!!.getBoolean(
                    ("CHARACTER_" + characterIdx.toString() + "_" +
                            "ENABLED_" + categoryIdx.toString() + "_" + pos.toString()), true)
        }
        // specify an adapter (see also next example)
        mAdapter = MyHWAdapter(characterIdx, categoryIdx,
                mAllHWList[categoryIdx], mAllCheckedData[categoryIdx], mAllEnabledData[categoryIdx])
        recyclerView!!.adapter = mAdapter
    }

    fun refreshAlert(categoryIdx: Int) {
        val categoryText = arrayOf("일간", "레이드", "주간")
        AlertDialog.Builder(this)
                .setTitle(categoryText[categoryIdx] + " 숙제 초기화")
                .setMessage("초기화하시겠습니까?") //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes) { _, _ -> hwRefresh(categoryIdx) }
                .setNegativeButton(android.R.string.no) { _, _ -> }
                .show()
    }

    private fun refreshDailyAlert() {
        AlertDialog.Builder(this)
                .setTitle("일간, 레이드 숙제 초기화")
                .setMessage("초기화하시겠습니까?") //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    hwRefresh_noreloading(0)
                    hwRefresh_noreloading(1)
                }
                .setNegativeButton(android.R.string.no) { dialog, whichButton -> }
                .show()
    }

    private fun refreshWeeklyAlert() {
        AlertDialog.Builder(this)
                .setTitle("주간 숙제 초기화")
                .setMessage("초기화하시겠습니까?") //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton -> hwRefresh_noreloading(2) }
                .setNegativeButton(android.R.string.no) { dialog, whichButton -> }
                .show()
    }

    fun hwRefresh(categoryIdx: Int) {
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        val editor = appData?.edit()

        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (pos in mAllHWList[categoryIdx].indices) {
            for (i in 0..3) {
                editor!!.putBoolean(
                        ("CHARACTER_" + characterIdx.toString() + "_" +
                                "CHECKED_" + categoryIdx.toString() + "_" + pos.toString() + "_" + i.toString()), false)
            }
            // 1.0.1 수정: 새로고침 시 줄긋기 수정 안 되게
            //editor.putBoolean("DAILY_ENABLED_"+String.valueOf(pos), true);
        }
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor!!.apply()
        loadChecked(categoryIdx)
    }

    fun hwRefresh_noreloading(categoryIdx: Int) {
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        val editor = appData!!.edit()

        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (pos in mAllHWList[categoryIdx].indices) {
            for (i in 0..3) {
                editor.putBoolean(
                        ("CHARACTER_" + characterIdx.toString() + "_" +
                                "CHECKED_" + categoryIdx.toString() + "_" + pos.toString() + "_" + i.toString()), false)
            }
            // 1.0.1 수정: 새로고침 시 줄긋기 수정 안 되게
            //editor.putBoolean("DAILY_ENABLED_"+String.valueOf(pos), true);
        }
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply()
    }
}