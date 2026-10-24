package com.example.sign.activity

import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import com.android.common.base.BaseActivity
import com.example.sign.databinding.ActivitySignBinding
import com.example.sign.service.SignService

/**
 * @作者 陈忠岳
 * @主要功能 定时唤起目标 App（签到）的设置页
 * @创建日期  2020/12/9
 *
 * 修正：输入框清空或输入非数字时，原来的 `it.toString().toInt()`
 * 会抛 NumberFormatException；改为 `toIntOrNull()` 并做范围校验。
 */
class SignActivity : BaseActivity<SignContract.Presenter, ActivitySignBinding>(), SignContract.View {

    override lateinit var presenter: SignContract.Presenter

    override fun setBinding(): ActivitySignBinding {
        return ActivitySignBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = SignPresenter(this, this, this)
        startService(Intent(this, SignService::class.java))
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.editHour.doAfterTextChanged { text ->
            val hour = text?.toString()?.toIntOrNull() ?: return@doAfterTextChanged
            if (hour in 0..23) {
                SignService.hour = hour
            }
        }
        binding.editMinute.doAfterTextChanged { text ->
            val minute = text?.toString()?.toIntOrNull() ?: return@doAfterTextChanged
            if (minute in 0..59) {
                SignService.minute = minute
            }
        }
    }
}
