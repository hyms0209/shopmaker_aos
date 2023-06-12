package com.android.shopmaker.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.android.shopmaker.databinding.ActivityMainBinding
import com.android.shopmaker.data.service.network.vo.EmergencyNoticeData
import com.android.shopmaker.presentation.dialog.ConfirmDialog
import com.android.shopmaker.presentation.dialog.OnDialogClickListener
import com.android.shopmaker.presentation.base.BaseActivity
import com.android.shopmaker.domain.constant.Constants
import com.android.shopmaker.presentation.intro.SplashViewModel
import com.android.shopmaker.presentation.webview.WebViewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    companion object {
        var instance: MainActivity? = null
    }

    private lateinit var binding: ActivityMainBinding

    val viewModel by viewModels<SplashViewModel>()

    lateinit var webViewFragment: WebViewFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        webViewFragment = WebViewFragment.newInstance(Constants.WebViewUrl)
        supportFragmentManager
            .beginTransaction()
            .add(binding.flContainer.id, webViewFragment, "WebView")
            .commitNow()

        setContentView(binding.root)

        bindInitViewModel()
        Log.d("MainActivity","onResume")
    }

    override fun onResume() {
        Log.d("MainActivity","onResume")
        super.onResume()
    }

    fun bindInitViewModel() {

    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.last()?.let {
            if ( it is WebViewFragment) {
                it.goBack()
            }
        }
    }

    fun openAlertDialog(data: EmergencyNoticeData) {
        var dialog = ConfirmDialog()

        dialog.setOnClickListener(object: OnDialogClickListener{
            override fun onCancelClickListener() {

            }

            override fun onConfirmClickListener() {
                Log.d("EmergencyDialog", "Confirm Click")
            }
        })

        dialog.show(supportFragmentManager, "AlertDialog")
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        var lastFragment = supportFragmentManager.fragments.last()
//        lastFragment?.let {
//            if ( it is WebViewFragment ) {
//                it.onActivityResult(requestCode, resultCode, data)
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
}