package com.queserasera.lostarkhomework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditNameActivity extends Activity {
    /** Called when the activity is first created. */

    private EditText mNameDisplay;
    private Button mInputDone;
    private String characterName;
    private int characterIdx;

    private SharedPreferences appData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        // characterIdx 받아오기
        Intent intent = getIntent();
        characterIdx = intent.getIntExtra("characterIdx", 0);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        // 선택 완료 시 main으로
        mInputDone = (Button) findViewById(R.id.inputDone);
        mInputDone.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                save();
                showMain();
            }
        });

        //// Edit Namer: 시작  ////////////
        mNameDisplay = (EditText) findViewById(R.id.edit_name);
        characterName = appData.getString("CHARACTER_IDX_" + String.valueOf(characterIdx), "이름없음");
        mNameDisplay.setText(characterName);
    }

    public void showMain() {
        finish();
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();
        characterName = mNameDisplay.getText().toString();
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("CHARACTER_IDX_" + String.valueOf(characterIdx), characterName);
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
        ((MainActivity)MainActivity.mContext).showCharacter(characterIdx);
    }
}
