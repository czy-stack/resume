package com.android.common.web

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.android.common.R
import com.android.common.base.BaseActivity
import com.android.common.constants.Constants
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_agent_web.*


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class AgentWebActivity : BaseActivity<AgentWebContract.Presenter>(), AgentWebContract.View,
    LifecycleOwner {
    override lateinit var presenter: AgentWebContract.Presenter


    companion object {
        fun intentWeb(context: Context, url: String, title: String) {
            val intent = Intent(context, AgentWebActivity::class.java)
            intent.putExtra(Constants.INTENT_STRING, url)
            intent.putExtra(Constants.INTENT_TITLE, title)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_agent_web
    }


    override fun initView() {
        presenter          = AgentWebPresenter(this, this, this)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        AgentWeb.with(this)//传入Activity
            .setAgentWebParent(
                linear,
                LinearLayout.LayoutParams(-1, -1)
            )//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
            .useDefaultIndicator()// 使用默认进度条
//            .defaultProgressBarColor() // 使用默认进度条颜色
//            .setReceivedTitleCallback(object) //设置 Web 页面的 title 回调
            .createAgentWeb()
            .ready()
            .go(intent.getStringExtra(Constants.INTENT_STRING))

    }

    override fun initData() {

    }

    override fun initListener() {
    }


}