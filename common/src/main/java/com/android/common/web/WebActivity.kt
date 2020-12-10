package com.android.common.web

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.lifecycle.LifecycleOwner
import com.android.common.R
import com.android.common.base.BaseActivity
import com.android.common.constants.Constants
import com.android.common.utils.LogUtils
import kotlinx.android.synthetic.main.activity_web.*


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class WebActivity : BaseActivity<WebContract.Presenter>(), WebContract.View, LifecycleOwner {
    override lateinit var presenter: WebContract.Presenter


    companion object {
        fun intentWeb(context: Context, url: String,title:String) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(Constants.INTENT_STRING, url)
            intent.putExtra(Constants.INTENT_TITLE,title)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }


    override fun initView() {
        presenter         = WebPresenter(this, this, this)

        web.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            javaScriptCanOpenWindowsAutomatically = true
            loadsImagesAutomatically = true
            domStorageEnabled = true
            databaseEnabled = true
//            setAppCacheEnabled(true)
//            setAppCachePath(applicationContext.getDir("web", Context.MODE_PRIVATE).path)
            defaultTextEncodingName = "utf-8";//设置编码格式
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
        }
        web.webViewClient = WebViewClient()
    }

    override fun initData() {
        tv_title.text = intent.getStringExtra(Constants.INTENT_TITLE)
        intent.getStringExtra(Constants.INTENT_STRING)?.let {
            LogUtils.i("WebActivity",msg = it)
            web.loadUrl(it) }
    }

    override fun initListener() {
        toolbar.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        web?.let {
            it.loadDataWithBaseURL(null, "", "tex/html", "utf-8", null)
            it.clearHistory()
            (it.parent as ViewGroup).removeView(it)
            it.destroy()
        }
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}