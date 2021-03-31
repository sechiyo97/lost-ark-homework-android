package com.queserasera.lostarkhomework

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.Constants
import com.anjlab.android.iab.v3.TransactionDetails
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.queserasera.lostarkhomework.databinding.ActivityMainBinding
import com.queserasera.lostarkhomework.homework.HomeworkActivity
import com.queserasera.lostarkhomework.mari.MariActivity

class MainActivity : Activity(), IBillingHandler {
    private var bp //결제용 객체
            : BillingProcessor? = null
    private var characterIdx = 0
    private var characterName: String? = null
    private var hwButton: Button? = null
    private var mariButton: Button? = null

    //private Button fundButton;
    private var leftArrow: ImageView? = null
    private var rightArrow: ImageView? = null
    private var characterIcon: ImageView? = null
    private var characterNameView: TextView? = null
    private var mAdViewBottom: AdView? = null
    private var appData: SharedPreferences? = null
    private val licenseKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAls8fKwHSV9z7cLIgl4jsmKdXQFsTO" +
            "DzQ93N44hZHTu9OcRg9gJOcFQum3j3EgRG1cd0rCRQ6AQeLReMKLIhttIeuKhrOh7S" +
            "MW/el3iRzviu3D6xeM7nVWUzjYP3PUMu355mqCaWVlQkcEHiR8QBtOpIvpTLE7BFUb0" +
            "UTD+9Mdrp4fb78/2KfrFURZ5iRtRfWupJTpY787AMUBmrssYGETH2QkW96uQ9WJNWYj" +
            "dJOmuPHxno0gHwhikyT+jM83csWiXgw2xDVjQsv2dnagfTrjefJJpuMG0Fo79I2y73Z" +
            "9Jlfs8ZLTGBFXt4QYY+6ZSGJDR3ZyCcrWuzIKbmNbD0/PwIDAQAB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arkHomeworkButton.setOnClickListener { showHomework(0) }
        binding.arkMariButton.setOnClickListener { showMari() }

//        mContext = this
//        hwButton = findViewById<View>(R.id.ark_homework_button) as Button
//        mariButton = findViewById<View>(R.id.ark_mari_button) as Button
//
//        // 설정값 불러오기
//        appData = getSharedPreferences("appData", MODE_PRIVATE)
//        bp = BillingProcessor(this, licenseKey, this)
//        bp!!.initialize()
//        hwButton!!.setOnClickListener { showHW(characterIdx) }
//
//        mariButton!!.setOnClickListener { showMari() }
//        leftArrow!!.setOnClickListener { changeCharacter(false) }
//        rightArrow!!.setOnClickListener { changeCharacter(true) }
//        characterIcon!!.setOnClickListener { editNameAlert() }
//        characterNameView!!.setOnClickListener { editNameAlert() }
//        showCharacter(0)

        // 광고 불러오기
        MobileAds.initialize(this) { }
        mAdViewBottom = findViewById(R.id.ark_ad_view_bottom)
        val adRequest = AdRequest.Builder().build()
        mAdViewBottom?.loadAd(adRequest)
    }

    // 캐릭터 변경
    private fun changeCharacter(nextOne: Boolean) {
        characterIdx = if (nextOne) (characterIdx + 1) % 6 else if (characterIdx != 0) (characterIdx - 1) % 6 else 5
        showCharacter(characterIdx)
    }

    fun showCharacter(characterIdx: Int) {
        characterName = appData!!.getString("CHARACTER_IDX_$characterIdx", "이름없음")
        characterNameView!!.text = characterName
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

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        // * 구매 완료시 호출
        // productId: 구매한 sku (ex) no_ads)
        // details: 결제 관련 정보
        val builder = AlertDialog.Builder(this)
        builder.setMessage("후원해 주셔서 감사합니다! >ㅁ<")
                .setCancelable(false)
                .setPositiveButton("확인") { dialog, id ->
                    // to do action
                }
        val alert = builder.create()
        alert.show()
        bp!!.consumePurchase("donate")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // onActivityResult 부분이 없을시 구글 인앱 결제창이 동시에 2개가 나타나는 현상이 발생
        if (bp!!.handleActivityResult(requestCode, resultCode, data)) {
            return
        }
    }

    override fun onPurchaseHistoryRestored() {
        // * 구매 정보가 복원되었을때 호출
        // bp.loadOwnedPurchasesFromGoogle() 하면 호출 가능
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        // * 구매 오류시 호출
        // errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 일때는
        // 사용자가 단순히 구매 창을 닫은것임으로 이것 제외하고 핸들링하기.
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            val errorMessage = "구매 에러 발생  (Code $errorCode)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBillingInitialized() {
        // * 처음에 초기화됬을때.
        val mProduct = bp!!.getPurchaseListingDetails("donate")
        if (mProduct != null) {
            val temp = (mProduct.productId + " / " + mProduct.priceText + " / "
                    + mProduct.priceValue + " / " + mProduct.priceLong)
            Log.d("BILL_INITIALIZE_SUCCESS", temp)
        } else {
            Log.d("BILL_INITIZAILZE_FAIL", "mProduct is null.")
        }
    }

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