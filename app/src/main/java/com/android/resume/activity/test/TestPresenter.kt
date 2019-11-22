package com.android.resume.activity.test

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.android.common.http.RxSimpleObserver
import com.android.common.lifecycle.LifecyclePresenter
import com.android.common.utils.register
import com.android.common.utils.unregister
import com.android.resume.activity.test.TestContract
import com.android.resume.bean.ShareData
import com.android.resume.constants.MainConstants
import com.android.resume.db.DBManager

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class TestPresenter(private val context: Context, private val view: TestContract.View, lifecycleOwner: LifecycleOwner)  : LifecyclePresenter(
    lifecycleOwner
), TestContract.Presenter {
    override fun getShares(search: String, type: Int) {
        if (search.isEmpty()) {
            val reversed = DBManager.getHistory().reversed()
            view.setShares(reversed, true)
        } else {
            when(type){
                MainConstants.A-> searchA(search)
                MainConstants.H-> searchH(search)
                MainConstants.M-> searchM(search)
            }
        }
    }

    private fun searchA(search: String){

    }
    private fun searchH(search: String){
        val readRecord = DBManager.searchRecord(MainConstants.HK,search)
        val mutableListOf = mutableListOf<ShareData>()
        for (item in readRecord){
            mutableListOf.add(ShareData(symbol = item.mId.toString(),describe =item.symbol, name = item.name,type = MainConstants.HK))
        }
        view.setShares(mutableListOf)
    }
    private fun searchM(search: String){
        val readRecord = DBManager.searchRecord(MainConstants.USA,search)
        val mutableListOf = mutableListOf<ShareData>()
        for (item in readRecord){
            mutableListOf.add(ShareData(symbol = item.mId.toString(),describe =item.symbol,name = item.name,type = MainConstants.USA))
        }
        view.setShares(mutableListOf)
    }

    override fun start() {

    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.register()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.unregister()
        super.onDestroy(owner)
    }

}