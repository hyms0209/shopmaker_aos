package com.android.shopmaker.presentation.webview

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.*
import com.android.shopmaker.domain.constant.Constants


class CommonWebViewClient(var listener: WebViewFragment.WebSchemeListener?) : WebViewClient(){

    /***
     * 웹앱 인터페이스 후킹방식으로 연동
     * 약속된 웹스키마로 넘어온경우 스키마 처리하고 파일다운로드의 경우 별도 처리한다.
     */
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.url?.let {
            var ret = true;
            if ( it.scheme == Constants.WebScheme ) {
                listener?.onSchemeListener(Uri.parse(it.toString()))
                return true
            } else {
                var urlString = it?.toString()
                urlString?.let {
                    if (it.endsWith(Constants.FileType.PDF) or it.endsWith(Constants.FileType.PPT)) {
                        listener?.onFileDownLoad(it)
                        return true
                    }
                }
            }
        }
        return super.shouldOverrideUrlLoading(view, request)
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }

    override fun onSafeBrowsingHit(
        view: WebView?,
        request: WebResourceRequest?,
        threatType: Int,
        callback: SafeBrowsingResponse?
    ) {
        super.onSafeBrowsingHit(view, request, threatType, callback)
    }

}