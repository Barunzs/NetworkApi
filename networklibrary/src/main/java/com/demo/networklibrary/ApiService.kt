package com.demo.networklibrary

import android.util.Log
import com.demo.networklibrary.RetrofitService.createService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import rx.Observable


class ApiService<TYPE> {

    private val TAG = ApiService::class.java.canonicalName

    public fun getDataFromServer(
        url: String,
        clazz: Class<TYPE>,
        model: TYPE,
        baseurl: String
    ): Observable<TYPE>? {
        val gson = GsonBuilder().create()
        val apiInterface = createService(
            ApiInterface::class.java, baseurl
        )
        Log.d(TAG, "url$url")
        return apiInterface.getDataFromServer(url)
            .flatMap {
                convertObjectsStream(it, gson, clazz)
            }
        /* ?.subscribeOn(Schedulers.io())
         ?.observeOn(AndroidSchedulers.mainThread())
         ?.subscribe(object : Subscriber<TYPE>() {
             override fun onStart() {
                 super.onStart()
                 Log.d(TAG, "onStart for ApiService")
             }

             override fun onNext(feature: TYPE) {
                 Log.d(TAG, "onNext for model::${model}")
                 Log.d(TAG, "onNext for ApiService::${feature}")

             }

             override fun onCompleted() {
                 Log.d(TAG, "onCompleted for ApiService")
             }

             override fun onError(e: Throwable) {
                 Log.d(TAG, "onError for ApiService${e.message}")
             }
         })*/

    }

    private fun <TYPE> convertObjectsStream(
        responseBody: ResponseBody,
        gson: Gson,
        clazz: Class<TYPE>
    ): Observable<TYPE> {

        return Observable.create { subscriber ->
            try {
                val type = TypeToken.get(clazz).type
                val responseBodyString = responseBody.string()
                Log.d(TAG, "responseBodyString : $responseBodyString")
                val t: TYPE = gson.fromJson(responseBodyString, type)
                Log.d(TAG, "TYPE : ${t.toString()}")
                subscriber.onNext(t)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e) // Signal about the error to subscriber
            }
        }
    }

}