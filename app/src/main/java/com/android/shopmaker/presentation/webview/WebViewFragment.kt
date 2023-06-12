package com.android.shopmaker.presentation.webview

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.shopmaker.AppApplication
import com.android.shopmaker.databinding.FragmentWebViewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class WebViewFragment : Fragment() {

    private lateinit var viewbinding : FragmentWebViewBinding
    private lateinit var webView:WebView

    private val vm: WebViewViewModel by viewModels()

    var url = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewbinding = FragmentWebViewBinding.inflate(inflater, container, false)
        viewbinding.lifecycleOwner = this

        // 웹뷰 설정
        initSetting()

        // 바인딩
        bindViewModel()

        // url취득
        arguments?.let {
            url =  it.getString(LOAD_URL, "")
        }

        // url 로딩
        loadUrl(this.url)

        // UI바인딩
        return viewbinding.root
    }


    /***
     * 웹뷰 설정 값 셋팅
     */
    fun initSetting() {
        webView = viewbinding.webView.apply {
            with(settings) {
                javaScriptEnabled = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                setNetworkAvailable(true)
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
            }
            isHorizontalScrollBarEnabled = false
            webViewClient = CommonWebViewClient(webSchemeProcess)
        }
    }

    interface WebSchemeListener {
        fun onSchemeListener(uri: Uri?)
        fun onFileDownLoad(url: String)
    }

    /***
     * 웹뷰에서 넘어오는 웹 스키마 처리
     */
    var webSchemeProcess: WebSchemeListener? = object: WebSchemeListener {
        override fun onSchemeListener(uri: Uri?) {
            uri?.let {
                vm.processUri(it)
            }
        }

        override fun onFileDownLoad(url: String) {
            if (isStoragePermissionGranted()) {
                var uri = Uri.parse(url)
                uri?.let {
                    vm.downloadFile(it)
                }
            } else {
                // 권한 요청 처리
            }
        }
    }

    /***
     * url 로딩
     */
    fun loadUrl(url:String) {
        viewbinding.webView.post {
            viewbinding.webView.loadUrl(url)
        }
    }

    /***
     * 뷰모델 바인딩
     */
    fun bindViewModel() {
        vm.output.openCamera.observe(viewLifecycleOwner, Observer {

        })

        vm.output.openAlbum.observe(viewLifecycleOwner, Observer {

        })

        vm.output.getAlbumInfo.observe(viewLifecycleOwner, Observer {
            setJavaScript(it)
        })

        vm.output.getCameraInfo.observe(viewLifecycleOwner, Observer {
            setJavaScript(it)
        })

        vm.output.downloadFile.observe(viewLifecycleOwner, Observer {
            shareFile(it)
        })
    }

    /***
     * 파일공유
     */
    fun shareFile(url: String) {
        AppApplication.ctx?.let {
            val file = File(url)
            val uri = FileProvider.getUriForFile(
                it,
                "com.android.shopmaker.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = getMimeType(uri.toString())
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            it.grantUriPermission(
                it.packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            try {
                startActivity(Intent.createChooser(shareIntent, "파일 공유"))
            } catch (e: Exception) {
                e.printStackTrace()
                // 예외 처리 로직 추가
            }
        }
    }

    /***
     * url의 mimeType
     */
    fun getMimeType(url: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    /***
     * 자바스크립트 직접 호출
     */
    fun setJavaScript(setData:String) {
        webView.loadUrl("javascript:setImageByteCode('3','data:image/png;base64," + setData + "')")
    }

    /***
     * 백키 처리
     */
    fun goBack() {
        if ( webView.canGoBack() ) {
            webView.goBack()
        }
    }

    /***
     * 파일다운로드 ( 노티피케이션 보임)
     */
    private fun downloadFileAndNoti(url: String) {
        val context = AppApplication.ctx
        val request = DownloadManager.Request(Uri.parse(url))
        val fileName = URLUtil.guessFileName(url, null, null)
        request.setMimeType(getMimeType(url))
        request.setDescription("Downloading file...")
        request.setTitle(fileName)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName )
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    /***
     * 권한 체크
     */
    private fun isStoragePermissionGranted(): Boolean {
        val context = AppApplication.ctx
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val granted = context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            granted === PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /***
     * 종료
     */
    override fun onDestroy() {
        super.onDestroy()
        webSchemeProcess = null
    }

    companion object {
        var LOAD_URL = ""
        @JvmStatic
        fun newInstance(url: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    this.putString(LOAD_URL, url)
                }
            }
    }
}
