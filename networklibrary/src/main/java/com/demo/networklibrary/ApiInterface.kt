package com.demo.networklibrary

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable


interface ApiInterface {

    @GET
    fun getDataFromServer(@Url url: String?): Observable<ResponseBody>
}