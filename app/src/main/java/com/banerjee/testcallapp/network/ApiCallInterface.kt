package com.banerjee.testcallapp.network

import com.banerjee.testcallapp.model.UserAuthDataClasses
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiCallInterface {

    @POST(value = "/api/service/Signup")
    fun signUpUser(@Body model: UserAuthDataClasses.SignUpModel):
            Call<UserAuthDataClasses.SignUpResponse>

    @GET(value = "/api/service/userloginV1")
    fun logInUser(
        @Query("emailid") emailid: String,
        @Query("password") password: String,
        @Query("oneSignalId") oneSignalId:String
    ):
            Call<UserAuthDataClasses.SignUpResponse>

}