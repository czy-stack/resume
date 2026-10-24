package com.android.resume.activity.splash

import com.android.common.base.BaseActivity
import com.android.resume.databinding.ActivitySplashBinding


/**
 * @作者 陈忠岳
 * @主要功能  启动页
 * @创建日期  2019-11-21
 */
class SplashActivity : BaseActivity<SplashContract.Presenter, ActivitySplashBinding>(), SplashContract.View {
    override lateinit var presenter: SplashContract.Presenter

    override fun setBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = SplashPresenter(this, this, this)
    }

    override fun initData() {
        // presenter.saveInfoAll()
    }

    override fun initListener() {
    }
}
