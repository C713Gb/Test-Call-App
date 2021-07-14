package com.banerjee.testcallapp.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "http://apiv17.getcalley.com"


    fun getRetrofitInstance(): Retrofit? {
        val gson = GsonBuilder().serializeNulls().create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }

}