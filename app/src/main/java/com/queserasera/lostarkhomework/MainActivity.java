package com.queserasera.lostarkhomework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends Activity implements BillingProcessor.IBillingHandler {
    private BillingProcessor bp; //결제용 객체
    public static Context mContext;

    private int characterIdx = 0;
    private String characterName;

    private Button hwButton;
    private Button mariButton;
    //private Button fundButton;

    private ImageView leftArrow;
    private ImageView rightArrow;
    private ImageView characterIcon;
    private TextView characterNameView;
    private AdView mAdViewBottom;

    private SharedPreferences appData;

    public static final int[] iconList = {
            R.drawable.character0, R.drawable.character1, R.drawable.character2,
            R.drawable.character3, R.drawable.character4, R.drawable.character5};

    private String licenseKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAls8fKwHSV9z7cLIgl4jsmKdXQFsTO" +
                    "DzQ93N44hZHTu9OcRg9gJOcFQum3j3EgRG1cd0rCRQ6AQeLReMKLIhttIeuKhrOh7S" +
                    "MW/el3iRzviu3D6xeM7nVWUzjYP3PUMu355mqCaWVlQkcEHiR8QBtOpIvpTLE7BFUb0" +
                    "UTD+9Mdrp4fb78/2KfrFURZ5iRtRfWupJTpY787AMUBmrssYGETH2QkW96uQ9WJNWYj" +
                    "dJOmuPHxno0gHwhikyT+jM83csWiXgw2xDVjQsv2dnagfTrjefJJpuMG0Fo79I2y73Z" +
                    "9Jlfs8ZLTGBFXt4QYY+6ZSGJDR3ZyCcrWuzIKbmNbD0/PwIDAQAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        hwButton = (Button) findViewById(R.id.hw_button);
        mariButton = (Button) findViewById(R.id.mari_button);
        //fundButton = (Button) findViewById(R.id.fund_button);

        leftArrow= (ImageView) findViewById(R.id.left_arrow);
        rightArrow= (ImageView) findViewById(R.id.right_arrow);
        characterIcon = (ImageView) findViewById(R.id.character_icon);

        characterNameView = (TextView) findViewById(R.id.character_name);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        bp = new BillingProcessor(this, licenseKey, this);
        bp.initialize();

        hwButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showHW(characterIdx); }
        });
        /*mariButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { comingSoonToast(); }
        });*/
        mariButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showMari(); }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeCharacter(false); }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { changeCharacter(true); }
        });
        characterIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ editNameAlert(); }
        });
        characterNameView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){ editNameAlert(); }
        });
        showCharacter(0);

        // 광고 불러오기
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdViewBottom = findViewById(R.id.ad_view_bottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequest);
    }

    // 캐릭터 변경
    public void changeCharacter(boolean nextOne){
        if (nextOne) characterIdx = (characterIdx + 1)%6;
        else if (characterIdx != 0) characterIdx = (characterIdx - 1)%6;
        else characterIdx = 5;
        showCharacter(characterIdx);
    }

    public void showCharacter(int characterIdx){
        characterIcon.setImageResource(iconList[characterIdx]);
        characterName = appData.getString("CHARACTER_IDX_" + String.valueOf(characterIdx), "이름없음");
        characterNameView.setText(characterName);
    }

    public void editNameAlert(){
        new AlertDialog.Builder(this)
                .setTitle("캐릭터 이름 변경")
                .setMessage("캐릭터 이름을 변경하시겠습니까?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            editName();
        }})
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }})
            .show();
    }

    public void editName(){
        Intent intent = new Intent(getBaseContext(), EditNameActivity.class);
        intent.putExtra("characterIdx", characterIdx);
        startActivity(intent);
    }

    // 결제 관련
    public void donateAlert(){
        new AlertDialog.Builder(this)
            .setTitle("★ 개발자에게 후원하기 ★")
            .setMessage("이용해 주셔서 감사합니다!\n" +
                    "로스트아크: 숙제했니?는 한 대학생 나부랭이가 열심히 앱개발을 공부하며 만든 앱입니다.\n" +
                    "고생한 대학생에게 클라우드 한 캔만 사 주시면 감사하겠습니다... :D")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    donate();
                }})
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }})
            .show();
    }

    public void donate(){
        bp.purchase(this, "donate");
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        // * 구매 완료시 호출
        // productId: 구매한 sku (ex) no_ads)
        // details: 결제 관련 정보
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("후원해 주셔서 감사합니다! >ㅁ<")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // to do action
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        bp.consumePurchase("donate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResult 부분이 없을시 구글 인앱 결제창이 동시에 2개가 나타나는 현상이 발생
        if (bp.handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
    @Override
    public void onPurchaseHistoryRestored() {
        // * 구매 정보가 복원되었을때 호출
        // bp.loadOwnedPurchasesFromGoogle() 하면 호출 가능
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        // * 구매 오류시 호출
        // errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 일때는
        // 사용자가 단순히 구매 창을 닫은것임으로 이것 제외하고 핸들링하기.
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            String errorMessage = "구매 에러 발생 " + " (Code " + errorCode + ")";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingInitialized() {
        // * 처음에 초기화됬을때.
        SkuDetails mProduct = bp.getPurchaseListingDetails("donate");
        if(mProduct != null) {
            String temp = mProduct.productId + " / " + mProduct.priceText + " / "
                    + mProduct.priceValue + " / " + mProduct.priceLong;
            Log.d("BILL_INITIALIZE_SUCCESS", temp);
        } else {
            Log.d("BILL_INITIZAILZE_FAIL", "mProduct is null.");
        }
    }

    public void showHW(int characterIdx){
        Intent intent = new Intent(getBaseContext(), HWActivity.class);
        intent.putExtra("characterIdx", characterIdx);
        startActivity(intent);
    }
    public void showMari(){
        Intent intent = new Intent(getBaseContext(), MariActivity.class);
        startActivity(intent);
    }
    public void comingSoonToast(){
        Toast.makeText(getApplicationContext(), "준비 중인 기능입니다.", Toast.LENGTH_LONG).show();
    }
}
