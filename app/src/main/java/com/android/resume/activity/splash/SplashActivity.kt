package com.android.resume.activity.splash

import com.android.common.base.BaseActivity
import com.android.resume.R


/**
 * @作者 陈忠岳
 * @主要功能  股票宝启动页
 * @创建日期  2019-11-21
 */
class SplashActivity : BaseActivity<SplashContract.Presenter>(), SplashContract.View {
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
}