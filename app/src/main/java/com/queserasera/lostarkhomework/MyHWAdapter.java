package com.queserasera.lostarkhomework;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class MyHWAdapter extends RecyclerView.Adapter<MyHWAdapter.CustomViewHolder> {
    private int characterIdx;
    private int categoryIdx;
    private String[][] mHWList;
    private boolean[] mEnabledList;
    private boolean[][] mCheckedList;
    private SharedPreferences appData;
    private Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView checktext;
        protected CheckBox checkbox1;
        protected CheckBox checkbox2;
        protected CheckBox checkbox3;
        protected CheckBox checkbox4;
        protected LinearLayout checktextholder;


        public CustomViewHolder(View view) {
            super(view);
            this.checktext = (TextView) view.findViewById(R.id.recyclerview_checktext);
            this.checkbox4 = (CheckBox) view.findViewById(R.id.recyclerview_checkbox4);
            this.checkbox3 = (CheckBox) view.findViewById(R.id.recyclerview_checkbox3);
            this.checkbox2 = (CheckBox) view.findViewById(R.id.recyclerview_checkbox2);
            this.checkbox1 = (CheckBox) view.findViewById(R.id.recyclerview_checkbox1);
            this.checktextholder = (LinearLayout) view.findViewById(R.id.recyclerview_checktextholder);
            context = view.getContext();
        }
    }

    public MyHWAdapter(int characterIdx, int categoryIdx,
                       String[][] HWList, boolean[][] checkedList, boolean[] enabledList) {
        this.characterIdx = characterIdx;
        this.categoryIdx = categoryIdx;
        this.mHWList = HWList;
        this.mCheckedList = checkedList;
        this.mEnabledList = enabledList;
    }

    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출됩니다.
    @Override
    public void onBindViewHolder(final @NonNull MyHWAdapter.CustomViewHolder viewholder, final int position) {
        viewholder.checktext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        viewholder.checktext.setGravity(Gravity.LEFT);
        viewholder.checktext.setText(mHWList[position][0]);

        //check original
        checkBoxes(viewholder, position);

        //longpress => disable
        viewholder.checktextholder.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toggleEnabled(position, viewholder);
            }
        });

        //toggle
        viewholder.checkbox1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCheckedList[position][0] = !mCheckedList[position][0] ;
                save();}
        });
        viewholder.checkbox2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCheckedList[position][1] = !mCheckedList[position][1] ;
                save();}
        });
        viewholder.checkbox3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCheckedList[position][2] = !mCheckedList[position][2] ;
                save();}
        });
        viewholder.checkbox4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCheckedList[position][3] = !mCheckedList[position][3] ;
                save();}
        });

        showCheckList(position, viewholder);
    }
    public void toggleEnabled(int position, final @NonNull MyHWAdapter.CustomViewHolder viewholder){
        mEnabledList[position] = !mEnabledList[position];
        if (mEnabledList[position]==true) {
            viewholder.checktext.setTextColor(Color.parseColor("#000000"));
            viewholder.checktext.setPaintFlags(0);
        } else{
            viewholder.checktext.setTextColor(Color.parseColor("#DDDDDD"));
            viewholder.checktext.setPaintFlags(1);
        }
        showCheckList(position, viewholder);
        save();
    }
    public void showCheckList(int position, @NonNull MyHWAdapter.CustomViewHolder viewholder){
        if (mEnabledList[position]==false) {
            viewholder.checktext.setTextColor(Color.parseColor("#DDDDDD"));
            viewholder.checktext.setPaintFlags(viewholder.checktext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewholder.checkbox1.setVisibility(View.INVISIBLE);
            viewholder.checkbox2.setVisibility(View.INVISIBLE);
            viewholder.checkbox3.setVisibility(View.INVISIBLE);
            viewholder.checkbox4.setVisibility(View.INVISIBLE);
        } else{
            int checkboxNum = Integer.valueOf(mHWList[position][1]);
            if (checkboxNum==1) {
                viewholder.checkbox1.setVisibility(View.VISIBLE);
                viewholder.checkbox2.setVisibility(View.INVISIBLE);
                viewholder.checkbox3.setVisibility(View.INVISIBLE);
                viewholder.checkbox4.setVisibility(View.INVISIBLE);
            } else if (checkboxNum==2) {
                viewholder.checkbox1.setVisibility(View.VISIBLE);
                viewholder.checkbox2.setVisibility(View.VISIBLE);
                viewholder.checkbox3.setVisibility(View.INVISIBLE);
                viewholder.checkbox4.setVisibility(View.INVISIBLE);
            } else if (checkboxNum==3) {
                viewholder.checkbox1.setVisibility(View.VISIBLE);
                viewholder.checkbox2.setVisibility(View.VISIBLE);
                viewholder.checkbox3.setVisibility(View.VISIBLE);
                viewholder.checkbox4.setVisibility(View.INVISIBLE);
            } else if (checkboxNum==4){
                viewholder.checkbox1.setVisibility(View.VISIBLE);
                viewholder.checkbox2.setVisibility(View.VISIBLE);
                viewholder.checkbox3.setVisibility(View.VISIBLE);
                viewholder.checkbox4.setVisibility(View.VISIBLE);
            }
        }
    }

    public void checkBoxes(MyHWAdapter.CustomViewHolder viewholder, int position){
        viewholder.checkbox1.setChecked(mCheckedList[position][0]);
        viewholder.checkbox2.setChecked(mCheckedList[position][1]);
        viewholder.checkbox3.setChecked(mCheckedList[position][2]);
        viewholder.checkbox4.setChecked(mCheckedList[position][3]);
    }

    @Override
    public int getItemCount() {
        return (null != mHWList ? mHWList.length : 0);
    }
    public void save(){
        // 설정값 불러오기
        appData = context.getSharedPreferences("appData", MODE_PRIVATE);

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (int pos=0;pos<mHWList.length;pos++){
            for (int i=0;i<4;i++){
                editor.putBoolean("CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                        "CHECKED_" + String.valueOf(categoryIdx)+ "_" +
                        String.valueOf(pos) + "_" +
                        String.valueOf(i), mCheckedList[pos][i]);
            }
            editor.putBoolean("CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                    "ENABLED_"+ String.valueOf(categoryIdx)+
                    "_"+String.valueOf(pos), mEnabledList[pos]);
        }

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }
}