package com.queserasera.lostarkhomework.homework

import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.queserasera.lostarkhomework.Event
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.common.GameCharacter
import com.queserasera.lostarkhomework.databinding.ActivityHomeworkBinding
import com.queserasera.lostarkhomework.homework.event.OnResetDailyClicked
import com.queserasera.lostarkhomework.homework.event.OnResetWeeklyClicked
import com.queserasera.lostarkhomework.main.event.OnHomeworkLoaded
import com.queserasera.lostarkhomework.mari.event.OnTabChanged
import com.queserasera.lostarkhomework.standard.GAME_CHARACTER

class HomeworkActivity : AppCompatActivity() {
    private var binding: ActivityHomeworkBinding? = null
    private val viewModel = HomeworkViewModel()

    // TODO: 변수 개편
    private var adapter: RecyclerView.Adapter<*>? = null
    // dataset: {name, total, current, enabled}
    private var mAllHWList = arrayOf(arrayOf(arrayOf("에포나", "4"), arrayOf("카오스 던전", "4"), arrayOf("실리안의 지령서", "4"), arrayOf("이벤트 카던", "1"), arrayOf("길드출석", "1"), arrayOf("호감도", "1"), arrayOf("행운의 기운", "1")), arrayOf(arrayOf("레이드-1T", "4"), arrayOf("레이드-2T", "4"), arrayOf("레이드-3T", "4"), arrayOf("레이드-4T", "4"), arrayOf("레이드-5T", "4"), arrayOf("레이드-6T", "4"), arrayOf("레이드-7T", "4"), arrayOf("레이드-8T", "4")), arrayOf(arrayOf("주간 에포나", "4"), arrayOf("주간 레이드-1", "4"), arrayOf("주간 레이드-2", "4"), arrayOf("유령선", "1"), arrayOf("철새치", "1"), arrayOf("한파인양", "1")))
    private val mCategorySelector: Array<LinearLayout?> = arrayOfNulls(mAllHWList.size)
    // dataset
    private var mAllEnabledData = arrayOf(BooleanArray(mAllHWList[0].size), BooleanArray(mAllHWList[1].size), BooleanArray(mAllHWList[2].size))
    private var mAllCheckedData = arrayOf(Array(mAllHWList[0].size) { BooleanArray(4) }, Array(mAllHWList[1].size) { BooleanArray(4) }, Array(mAllHWList[2].size) { BooleanArray(4) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_homework)
        binding?.viewModel = viewModel

        binding?.arkHomeworkList?.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeworkActivity)
        }

        val character = intent.getSerializableExtra(GAME_CHARACTER) as? GameCharacter ?: GameCharacter("", 0)

        viewModel.event.observe(this, Observer<Event>{
            when(it) {
                OnResetDailyClicked -> {
                    askResetDailyHomework {
                        viewModel.resetDailyHomework(character)
                    }
                }
                OnResetWeeklyClicked -> {
                    askResetWeeklyHomework {
                        viewModel.resetWeeklyHomework(character)
                    }
                }
                OnHomeworkLoaded -> {
                    Toast.makeText(this, "OnHomeworkLoaded", Toast.LENGTH_SHORT).show()
                }
                is OnTabChanged -> {
                    if (binding?.selectedTab != it.index) {
                        binding?.selectedTab = it.index
                    }
                }
            }
        })

        (character as? GameCharacter)?.let {
            viewModel.loadHomework(it)
            viewModel.setTab(0)
        }
    }

    private fun askResetDailyHomework(onConfirmed: () -> Unit) {
        AlertDialog.Builder(this)
                .setTitle("일간 숙제 초기화")
                .setMessage("초기화하시겠습니까?")
                .setPositiveButton(R.string.confirm) { _, _ ->
                    onConfirmed.invoke()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
    }

    private fun askResetWeeklyHomework(onConfirmed: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("주간 숙제 초기화")
            .setMessage("초기화하시겠습니까?")
            .setPositiveButton(R.string.confirm) { _, _ ->
                onConfirmed.invoke()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun loadChecked(categoryIdx: Int) {
       /* // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)
        for (pos in mAllHWList[categoryIdx].indices) {
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
        adapter = MyHWAdapter(characterIdx, categoryIdx,
                mAllHWList[categoryIdx], mAllCheckedData[categoryIdx], mAllEnabledData[categoryIdx])
        binding?.arkHomeworkList?.adapter = adapter*/
    }

    private fun hwRefreshWithoutReload(categoryIdx: Int) {
        // 설정값 불러오기
       /* appData = getSharedPreferences("appData", MODE_PRIVATE)

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
        editor.apply()*/
    }
}