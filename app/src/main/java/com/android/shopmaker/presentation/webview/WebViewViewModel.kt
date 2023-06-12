package com.android.shopmaker.presentation.webview

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.shopmaker.data.service.network.vo.MomentRegistResult
import com.android.shopmaker.domain.usecase.BannerUseCase
import com.android.shopmaker.domain.usecase.FileDownloadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


interface WebViewViewModelType{
    var input: WebViewViewModelInput
    var output: WebViewViewModelOutput
}
interface WebViewViewModelOutput{
    val openCamera:LiveData<String>
    val openAlbum:LiveData<String>
    val getAlbumInfo: LiveData<String>
    val getCameraInfo: LiveData<String>
    val registMoment:LiveData<MomentRegistResult>
    val downloadFile:LiveData<String>
}
interface WebViewViewModelInput{
    fun processUri(uri:Uri)
    fun downloadFile(uri:Uri)
}

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val downloadUseCase: FileDownloadUseCase,
    private val bannerUseCase: BannerUseCase
) : ViewModel(),
    WebViewViewModelType,
    WebViewViewModelOutput,
    WebViewViewModelInput {

    override var input: WebViewViewModelInput = this
    override var output: WebViewViewModelOutput = this

    private var _openCamera = MutableLiveData<String>()
    override val openCamera: LiveData<String>
        get() = _openCamera

    private var _openAlbum = MutableLiveData<String>()
    override val openAlbum: LiveData<String>
        get() = _openAlbum

    private var _getAlbumInfo = MutableLiveData<String>()
    override val getAlbumInfo: LiveData<String>
                get() = _getAlbumInfo

    private var _getCameraInfo = MutableLiveData<String>()
    override val getCameraInfo: LiveData<String>
        get() = _getCameraInfo

    private var _registMoment = MutableLiveData<MomentRegistResult>()
    override val registMoment:LiveData<MomentRegistResult>
        get() = _registMoment

    private var _downloadFile = MutableLiveData<String>()
    override val downloadFile:LiveData<String>
        get() = _downloadFile


    /***
     * 웹앱 인터페이스 처리
     */
    override fun processUri(uri: Uri) {

        val selectedInterface = WebAppInterface.values().find { it.interfafceName == uri.host }
        selectedInterface?.let {
            when (it) {
                WebAppInterface.AUTH_BIO -> {
                    viewModelScope.launch(Dispatchers.Main) {
                        var result = bannerUseCase.excute("00010")
                        Log.d("WebViewModel", "result =-======> $result")
                    }
                }
            }
        }
    }

    override fun downloadFile(uri: Uri) {
        viewModelScope.launch {
            val fileName = URLUtil.guessFileName(uri.toString(), null, null)
            downloadUseCase?.downloadFile(uri.toString(), fileName)?.let {
                Log.d("WebViewViewModel", "downloadFile ${it.getFileFullPath()}")
                _downloadFile.postValue(it.getFileFullPath())
            }
        }
    }

    fun encodingBase64(bitmap:Bitmap):String {
        try {
            // BytArrayOutputStream을 이용해 Bitmap 인코딩
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            // 인코딩된 ByteStream을 String으로 획득
            val image: ByteArray = byteArrayOutputStream.toByteArray()
            val byteStream: String = Base64.encodeToString(image, 0)
            return byteStream
        } catch (e:Exception ) {
            return ""
        }
    }
}