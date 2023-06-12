package com.android.shopmaker.presentation.intro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.android.shopmaker.R
import com.android.shopmaker.data.service.network.vo.AppUpdateData
import com.android.shopmaker.data.service.network.vo.EmergencyNoticeData
import io.reactivex.rxjava3.core.Observable
import com.android.shopmaker.presentation.main.MainActivity
import com.android.shopmaker.databinding.ActivitySplashBinding
import com.android.shopmaker.presentation.base.BaseActivity
import com.android.shopmaker.presentation.dialog.ConfirmDialog
import com.android.shopmaker.presentation.dialog.EmergencyDialog
import com.android.shopmaker.presentation.dialog.OnDialogClickListener
import com.android.shopmaker.presentation.dialog.PermissionDialog
import java.util.concurrent.TimeUnit

class SplashActivity: BaseActivity() {

    val viewModel by viewModels<SplashViewModel>()

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(binding.root)
        bindInitViewModel()

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel.start()
                    }
    }

    fun bindInitViewModel() {
        // 긴급공지 체크 팝업
        viewModel.output.checkEmergency.observe(this) {
            openEmergencyDialog(it)
        }

        // 앱업데이트 체크 팝업
        viewModel.output.checkAppUpdate.observe(this) {
            openAppUpdateDialog(it)
        }

        // 퍼미션 체크 팝업
        viewModel.output.checkPermission.observe(this) {
            openPermissionDialog()
        }

        // 메인화면 이동
        viewModel.output.goMain.observe(this) {

            this.startActivity(Intent(this, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    fun openEmergencyDialog(data: EmergencyNoticeData) {
        var dialog = EmergencyDialog()
        dialog.setData(data)
        dialog.setOnClickListener(object: OnDialogClickListener {
            override fun onCancelClickListener() {
                viewModel.input.checkEmergency(false)
                dialog.dismiss()
            }

            override fun onConfirmClickListener() {
                Log.d("EmergencyDialog", "Confirm Click")
                viewModel.input.checkEmergency(true)
                dialog.dismiss()
            }
        })
        dialog.onCreateAnimation(androidx.appcompat.R.anim.abc_fade_in, true, androidx.appcompat.R.anim.abc_fade_out)
        dialog.show(supportFragmentManager, "Emergency")
    }

    fun openAppUpdateDialog(data: AppUpdateData) {
        var dialog = ConfirmDialog()
        dialog.title = data.title
        dialog.message = data.message
        dialog.cancelTitle = if (data.status == "FORCE") "종료" else "취소"
        dialog.confirmTitle = if (data.status == "FORCE") "업데이트" else "업데이트"

        dialog.setOnClickListener(object: OnDialogClickListener {
            override fun onCancelClickListener() {
                if ( data.status == "FORCE" ) {
                    forceFinish()
                } else {
                    viewModel.input.checkAppUpdate(false)
                }
                dialog.dismiss()
            }

            override fun onConfirmClickListener() {
                Log.d("EmergencyDialog", "Confirm Click")
                // 앱업데이트 이동
                viewModel.input.checkAppUpdate(true)
                dialog.dismiss()
            }
        })
        dialog.show(supportFragmentManager, "AppUpdateDialog")
    }

    fun openPermissionDialog() {
        var dialog = PermissionDialog()
        dialog.setOnClickListener(object: OnDialogClickListener{
            override fun onCancelClickListener() {

            }
            override fun onConfirmClickListener() {
                Log.d("EmergencyDialog", "Confirm Click")
                viewModel.input.checkPermission(true)
                //supportFragmentManager.beginTransaction().remove(dialog).commitNow()
            }
        })
        var transcation = supportFragmentManager.beginTransaction()
        transcation.setCustomAnimations(R.anim.from_right, R.anim.to_left)
        transcation.add(binding.flContainer.id, dialog).commitNow()
    }
}