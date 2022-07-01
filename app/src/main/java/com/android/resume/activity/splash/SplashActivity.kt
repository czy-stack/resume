package com.android.resume.activity.splash

import androidx.viewbinding.ViewBinding
import com.android.common.base.BaseActivity
import com.android.resume.R
import com.android.resume.databinding.ActivitySplashBinding
import com.example.sign.databinding.ActivitySignBinding


/**
 * @作者 陈忠岳
 * @主要功能  启动页
 * @创建日期  2019-11-21
 */
class SplashActivity : BaseActivity<SplashContract.Presenter,ActivitySplashBinding>(), SplashContract.View {
    override lateinit var presenter: SplashContract.Presenter

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        presenter = SplashPresenter(this, this, this)
    }

    override fun initData() {
        presenter.saveInfoAll()
    }

    override fun initListener() {

    }

    override fun setBinding(): ActivitySplashBinding {
        return  ActivitySplashBinding.inflate(layoutInflater)
    }
}