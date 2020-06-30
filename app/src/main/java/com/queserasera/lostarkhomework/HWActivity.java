package com.queserasera.lostarkhomework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HWActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences appData;
    private ImageView reloadButtonDaily;
    private ImageView reloadButtonWeekly;

    private int characterIdx;

    // dataset: {name, total, current, enabled}
    String[][][] mAllHWList = {
            //daily
            {{"에포나","4"},{"카오스 던전","4"}, {"실리안의 지령서","4"},
                    {"이벤트 카던","1"}, {"길드출석","1"},
                    {"호감도","1"},{"행운의 기운","1"}},
            //raid
            {{"레이드-1T","4"},{"레이드-2T","4"},{"레이드-3T","4"},
                    {"레이드-4T","4"},{"레이드-5T","4"},{"레이드-6T","4"},{"레이드-7T","4"},{"레이드-8T","4"}},
            //weekly
            {{"주간 에포나","4"},{"주간 레이드-1","4"},{"주간 레이드-2","4"},
                    {"유령선","1"},{"철새치","1"},{"한파인양","1"}},
    };

    private LinearLayout[] mCategorySelector = new LinearLayout[mAllHWList.length];

    // dataset
    boolean[][] mAllEnabledData = {
            new boolean[mAllHWList[0].length],
            new boolean[mAllHWList[1].length],
            new boolean[mAllHWList[2].length]
    };
    boolean[][][] mAllCheckedData = {
            new boolean[mAllHWList[0].length][4],
            new boolean[mAllHWList[1].length][4],
            new boolean[mAllHWList[2].length][4]
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw);

        Intent intent = getIntent();
        characterIdx = intent.getIntExtra("characterIdx", 0);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        reloadButtonDaily = (ImageView) findViewById(R.id.reload_button_daily);
        reloadButtonWeekly = (ImageView) findViewById(R.id.reload_button_weekly);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // set select buttons
        mCategorySelector[0] = (LinearLayout)findViewById(R.id.daily_select);
        mCategorySelector[1] = (LinearLayout)findViewById(R.id.weekly_select);
        mCategorySelector[2] = (LinearLayout)findViewById(R.id.passive_select);

        for (int i=0;i<mAllHWList.length;i++) {
            final int idx = i;
            mCategorySelector[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categorySelect(idx);
                }
            });
        }
        categorySelect(0);
    }
    public void categorySelect(final int categoryIdx){
        for (int i=0;i<3;i++) mCategorySelector[i].setBackgroundColor(this.getResources().getColor(R.color.white));
        mCategorySelector[categoryIdx].setBackgroundColor(this.getResources().getColor(R.color.colorAccent));

        reloadButtonDaily.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //refreshAlert(categoryIdx); // daily로 변경
                refreshDailyAlert();
                loadChecked(categoryIdx);
            }
        });
        reloadButtonWeekly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //refreshAlert(categoryIdx); // daily로 변경
                refreshWeeklyAlert();
                loadChecked(categoryIdx);
            }
        });

        loadChecked(categoryIdx);
    }
    public void loadChecked(int categoryIdx){
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        for (int pos=0;pos<mAllHWList[categoryIdx].length;pos++) {
            for (int i = 0; i < 4; i++) {
                mAllCheckedData[categoryIdx][pos][i] = appData.getBoolean(
                        "CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                                "CHECKED_" + String.valueOf(categoryIdx) + "_" +
                                String.valueOf(pos) + "_" +
                                String.valueOf(i), false);
            }
            mAllEnabledData[categoryIdx][pos] = appData.getBoolean(
                    "CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                            "ENABLED_" + String.valueOf(categoryIdx) + "_" +
                            String.valueOf(pos), true);
        }
        // specify an adapter (see also next example)
        mAdapter = new MyHWAdapter(characterIdx, categoryIdx,
                mAllHWList[categoryIdx], mAllCheckedData[categoryIdx], mAllEnabledData[categoryIdx]);
        recyclerView.setAdapter(mAdapter);
    }
    public void refreshAlert(final int categoryIdx){
        String[] categoryText = {"일간", "레이드", "주간"};
        new AlertDialog.Builder(this)
                .setTitle(categoryText[categoryIdx] + " 숙제 초기화")
                .setMessage("초기화하시겠습니까?")
                //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hwRefresh(categoryIdx);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                .show();
    }

    public void refreshDailyAlert(){
        new AlertDialog.Builder(this)
                .setTitle("일간, 레이드 숙제 초기화")
                .setMessage("초기화하시겠습니까?")
                //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hwRefresh_noreloading(0);
                        hwRefresh_noreloading(1);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                .show();
    }
    public void refreshWeeklyAlert(){
        new AlertDialog.Builder(this)
                .setTitle("주간 숙제 초기화")
                .setMessage("초기화하시겠습니까?")
                //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hwRefresh_noreloading(2);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                .show();
    }
    public void hwRefresh(int categoryIdx){
        // 설정값 불러오기
        appData = this.getSharedPreferences("appData", MODE_PRIVATE);

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (int pos=0;pos<mAllHWList[categoryIdx].length;pos++){
            for (int i=0;i<4;i++){
                editor.putBoolean(
                        "CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                                "CHECKED_"+String.valueOf(categoryIdx) + "_" +
                                String.valueOf(pos)+"_"+String.valueOf(i), false);
            }
            // 1.0.1 수정: 새로고침 시 줄긋기 수정 안 되게
            //editor.putBoolean("DAILY_ENABLED_"+String.valueOf(pos), true);
        }
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
        loadChecked(categoryIdx);
    }

    public void hwRefresh_noreloading(int categoryIdx){
        // 설정값 불러오기
        appData = this.getSharedPreferences("appData", MODE_PRIVATE);

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 저장시킬 이름이 이미 존재하면 덮어씌움
        for (int pos=0;pos<mAllHWList[categoryIdx].length;pos++){
            for (int i=0;i<4;i++){
                editor.putBoolean(
                        "CHARACTER_"+ String.valueOf(characterIdx) + "_" +
                                "CHECKED_"+String.valueOf(categoryIdx) + "_" +
                                String.valueOf(pos)+"_"+String.valueOf(i), false);
            }
            // 1.0.1 수정: 새로고침 시 줄긋기 수정 안 되게
            //editor.putBoolean("DAILY_ENABLED_"+String.valueOf(pos), true);
        }
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }
}
