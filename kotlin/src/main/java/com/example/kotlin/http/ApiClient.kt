package com.android.resume.http

import com.android.common.http.ApiHttp
import com.example.kotlin.bean.Record
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
object ApiClient {
    fun saveInfoAll(observer: Observer<List<Record>>) {
        ApiHttp.instance.createAppApi(service = AppApi::class.java)
            .getAll("https://qchart.oss-cn-hangzhou.aliyuncs.com/infos/coms.txt")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    fun getSearchShares(search: String, observer: Observer<String>) {
//        ApiHttp.instance.createScalarsAppApi(
//            baseUrl = BaseUrls.CONFIG_URL,
//            service = AppApi::class.java
//        )
//            .getSearchShares(StringBuilder(Constants.SEARCH).append(search).toString())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(observer)
    }
}