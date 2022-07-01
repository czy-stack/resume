package com.example.sign.activity

import android.content.Intent
import androidx.core.widget.addTextChangedListener
import com.android.common.base.BaseActivity
import com.example.sign.R
import com.example.sign.databinding.ActivitySignBinding
import com.example.sign.service.SignService

class SignActivity: BaseActivity<SignContract.Presenter,ActivitySignBinding>(), SignContract.View {

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
        binding.editHour.addTextChangedListener {
            it.let {
                SignService.temp1 = it.toString().toInt()
            }
        }
        binding.editMinute.addTextChangedListener {
            it.let {
                SignService.temp2 = it.toString().toInt()
            }
        }
    }

    override fun setBinding(): ActivitySignBinding {
        return ActivitySignBinding.inflate(layoutInflater)
    }
}