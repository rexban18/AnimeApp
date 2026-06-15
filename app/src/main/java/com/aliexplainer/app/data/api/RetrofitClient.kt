package com.aliexplainer.app.data.api

import com.aliexplainer.app.utils.SettingsManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var okHttpClient: OkHttpClient = createOkHttpClient()
    private var retrofit: Retrofit = createRetrofit()
    private var _apiService: ApiService = retrofit.create(ApiService::class.java)

    val apiService: ApiService get() = _apiService

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SettingsManager.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun rebuildClient() {
        okHttpClient = createOkHttpClient()
        retrofit = createRetrofit()
        _apiService = retrofit.create(ApiService::class.java)
    }
}
