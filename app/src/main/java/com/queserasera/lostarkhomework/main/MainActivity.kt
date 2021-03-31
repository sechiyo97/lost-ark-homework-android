package com.queserasera.lostarkhomework.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.Constants
import com.anjlab.android.iab.v3.TransactionDetails
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.databinding.ActivityMainBinding
import com.queserasera.lostarkhomework.homework.HomeworkActivity
import com.queserasera.lostarkhomework.main.event.OnHomeworkClicked
import com.queserasera.lostarkhomework.main.event.OnMariClicked
import com.queserasera.lostarkhomework.mari.MariActivity

class MainActivity : AppCompatActivity(), IBillingHandler {
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
    private val viewModel = MainViewModel()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = viewModel

        initViewModel()
        initAd()
    }

    private fun initViewModel() {
        viewModel.event.observe(this){
            when(it) {
                OnMariClicked -> showMari()
                OnHomeworkClicked -> showHomework(0)
            }
        }
    }

    private fun initAd(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding?.arkAdViewBottom?.loadAd(adRequest)
    }

    ////////////////////////////////////////

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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