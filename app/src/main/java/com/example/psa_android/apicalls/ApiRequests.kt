package com.example.psa_android.apicalls

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiRequests {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body loginRequest: SendRequestData): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun SignUpUser(@Body signUpRequest: SendRegisterData): Call<RegisterResponse>
}