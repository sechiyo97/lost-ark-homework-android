package com.queserasera.lostarkhomework

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.queserasera.lostarkhomework.MyHWAdapter.CustomViewHolder

class MyHWAdapter(private val characterIdx: Int, private val categoryIdx: Int,
                  private val mHWList: Array<Array<String>>?, private val mCheckedList: Array<BooleanArray>, private val mEnabledList: BooleanArray) : RecyclerView.Adapter<CustomViewHolder?>() {
    private var appData: SharedPreferences? = null
    private var context: Context? = null

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checktext: TextView = view.findViewById<View>(R.id.recyclerview_checktext) as TextView
        var checkbox1: CheckBox = view.findViewById<View>(R.id.recyclerview_checkbox1) as CheckBox
        var checkbox2: CheckBox = view.findViewById<View>(R.id.recyclerview_checkbox2) as CheckBox
        var checkbox3: CheckBox = view.findViewById<View>(R.id.recyclerview_checkbox3) as CheckBox
        var checkbox4: CheckBox = view.findViewById<View>(R.id.recyclerview_checkbox4) as CheckBox
        var checktextholder: LinearLayout = view.findViewById<View>(R.id.recyclerview_checktextholder) as LinearLayout

        init {
            context = view.context
        }
    }

    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_list, viewGroup, false)
        return CustomViewHolder(view)
    }

    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출됩니다.
    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int) {
        viewholder.checktext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        viewholder.checktext.gravity = Gravity.LEFT
        viewholder.checktext.text = mHWList!![position][0]

        //check original
        checkBoxes(viewholder, position)

        //longpress => disable
        viewholder.checktextholder.setOnClickListener { toggleEnabled(position, viewholder) }

        //toggle
        viewholder.checkbox1.setOnClickListener {
            mCheckedList[position][0] = !mCheckedList[position][0]
            save()
        }
        viewholder.checkbox2.setOnClickListener {
            mCheckedList[position][1] = !mCheckedList[position][1]
            save()
        }
        viewholder.checkbox3.setOnClickListener {
            mCheckedList[position][2] = !mCheckedList[position][2]
            save()
        }
        viewholder.checkbox4.setOnClickListener {
            mCheckedList[position][3] = !mCheckedList[position][3]
            save()
        }
        showCheckList(position, viewholder)
    }

    fun toggleEnabled(position: Int, viewholder: CustomViewHolder) {
        mEnabledList[position] = !mEnabledList[position]
        if (mEnabledList[position]) {
            viewholder.checktext.setTextColor(Color.parseColor("#000000"))
            viewholder.checktext.paintFlags = 0
        } else {
            viewholder.checktext.setTextColor(Color.parseColor("#DDDDDD"))
            viewholder.checktext.paintFlags = 1
        }
        showCheckList(position, viewholder)
        save()
    }

    fun showCheckList(position: Int, viewholder: CustomViewHolder) {
        if (!mEnabledList[position]) {
            viewholder.checktext.setTextColor(Color.parseColor("#DDDDDD"))
            viewholder.checktext.paintFlags = viewholder.checktext.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewholder.checkbox1.visibility = View.INVISIBLE
            viewholder.checkbox2.visibility = View.INVISIBLE
            viewholder.checkbox3.visibility = View.INVISIBLE
            viewholder.checkbox4.visibility = View.INVISIBLE
        } else {
            val checkboxNum = Integer.valueOf(mHWList!![position][1])
            if (checkboxNum == 1) {
                viewholder.checkbox1.visibility = View.VISIBLE
                viewholder.checkbox2.visibility = View.INVISIBLE
                viewholder.checkbox3.visibility = View.INVISIBLE
                viewholder.checkbox4.visibility = View.INVISIBLE
            } else if (checkboxNum == 2) {
                viewholder.checkbox1.visibility = View.VISIBLE
                viewholder.checkbox2.visibility = View.VISIBLE
                viewholder.checkbox3.visibility = View.INVISIBLE
                viewholder.checkbox4.visibility = View.INVISIBLE
            } else if (checkboxNum == 3) {
                viewholder.checkbox1.visibility = View.VISIBLE
                viewholder.checkbox2.visibility = View.VISIBLE
                viewholder.checkbox3.visibility = View.VISIBLE
                viewholder.checkbox4.visibility = View.INVISIBLE
            } else if (checkboxNum == 4) {
                viewholder.checkbox1.visibility = View.VISIBLE
                viewholder.checkbox2.visibility = View.VISIBLE
                viewholder.checkbox3.visibility = View.VISIBLE
                viewholder.checkbox4.visibility = View.VISIBLE
            }
        }
    }

    fun checkBoxes(viewholder: CustomViewHolder, position: Int) {
        viewholder.checkbox1.isChecked = mCheckedList[position][0]
        viewholder.checkbox2.isChecked = mCheckedList[position][1]
        viewholder.checkbox3.isChecked = mCheckedList[position][2]
        viewholder.checkbox4.isChecked = mCheckedList[position][3]
    }

    fun save() {
        // 설정값 불러오기
        appData = context!!.getSharedPreferences("appData", Context.MODE_PRIVATE)

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        val editor = appData!!.edit()

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (pos in mHWList!!.indices) {
            for (i in 0..3) {
                editor.putBoolean("CHARACTER_" + characterIdx.toString() + "_" +
                        "CHECKED_" + categoryIdx.toString() + "_" + pos.toString() + "_" + i.toString(), mCheckedList[pos][i])
            }
            editor.putBoolean("CHARACTER_" + characterIdx.toString() + "_" +
                    "ENABLED_" + categoryIdx.toString() +
                    "_" + pos.toString(), mEnabledList[pos])
        }

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply()
    }

    override fun getItemCount() =
        mHWList?.size?:0
}