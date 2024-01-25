package com.example.psa_android.apicalls

data class SendRequestData(
    val email: String,
    val password: String,
)

data class RegisterResponse(
    val `data`: UserData,
    val message: String,
    val success: Boolean
)
data class SendRegisterData(
    val city: String,
    val country: String,
    val dob: String,
    val email: String,
    val firstname: String,
    val gender: String,
    val lastname: String,
    val password: String,
    val phone: String,
    val state: String,
    val username: String
)

data class LoginResponse(
    val `data`: UserData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class UserData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val password: String,
    val token: String,
    val updatedAt: String,
    val wishlist: List<Any>
)
