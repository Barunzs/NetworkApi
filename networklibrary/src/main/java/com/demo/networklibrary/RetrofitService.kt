package com.demo.networklibrary

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    val TAG = RetrofitService::class.java.canonicalName

    fun <T> createService(clazz: Class<T>, endpoint: String): T {

        // no read timeout
        val contentType = MediaType.get("application/json")
        Log.d(TAG,"baseurl::$endpoint")
        Log.d(TAG,"clazz${clazz.canonicalName}")
        val okHttpClient =
            OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS)
                .build()
        return Retrofit.Builder()
            .baseUrl(endpoint)
            //.addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            /*.addConverterFactory(
                Json(JsonConfiguration(strictMode = false)).asConverterFactory(
                    contentType
                )
            )*/
            .client(okHttpClient)
            .build()
            .create(clazz)
    }

}