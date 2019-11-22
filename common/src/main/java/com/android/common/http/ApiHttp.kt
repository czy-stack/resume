package com.android.common.http

import com.android.common.BuildConfig
import com.android.common.base.BaseUrls
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class ApiHttp private constructor() {
    companion object {
        val instance: ApiHttp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiHttp()
        }
    }

    private val client: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        }
        client = builder.retryOnConnectionFailure(true)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    fun <T> createAppApi(baseUrl: String = BaseUrls.BASE_URL, service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(service)
    }

    fun <T> createScalarsAppApi(baseUrl: String = BaseUrls.BASE_URL, service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(service)
    }

}