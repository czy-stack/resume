package com.android.resume.http

import androidx.collection.ArrayMap
import com.example.kotlin.bean.Record
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface AppApi {
    //获取所有股票信息
    @GET
    fun getAll(@Url url: String): Observable<List<Record>>

    //A股搜索
    @GET
    fun getSearchShares(@Url url: String): Observable<String>

    //添加自选
    @POST("")
    fun addOptional(@Body map: ArrayMap<String, String>): Observable<String>

    //删除自选
    @POST("")
    fun removeOptional(@Body map: ArrayMap<String, String>): Observable<String>

    //K线
    @GET("")
    fun getKData(@QueryMap map: ArrayMap<String, String>): Observable<String>

    //A股点买差异化显示价格数值
    @GET("")
    fun getFeeInfo(@QueryMap map: ArrayMap<String, String>): Observable<String>

}