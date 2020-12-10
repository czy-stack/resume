package com.example.sign.activity

import android.content.Intent
import androidx.core.widget.addTextChangedListener
import com.android.common.base.BaseActivity
import com.example.sign.R
import com.example.sign.service.SignService
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : BaseActivity<SignContract.Presenter>(), SignContract.View {

    override lateinit var presenter: SignContract.Presenter

    override fun getLayoutId(): Int {
        return R.layout.activity_sign
    }

    override fun initView() {
        presenter = SignPresenter(this,this,this)
        startService(Intent(this,SignService::class.java))
    }

    override fun initData() {
    }

    override fun initListener() {
        edit_hour.addTextChangedListener {
            it.let {
                SignService.temp1 = it.toString().toInt()
            }
        }
        edit_minute.addTextChangedListener {
            it.let {
                SignService.temp2 = it.toString().toInt()
            }
        }
    }
}