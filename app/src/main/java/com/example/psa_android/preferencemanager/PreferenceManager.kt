package com.example.psa_android.preferencemanager

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "PSAPreferenceManager", Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var INSTANCE: PreferenceManager? = null

        fun getInstance(context: Context): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceManager(context).also { INSTANCE = it }
            }
        }
    }

    // Example function to save a string preference
    fun saveString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    // Example function to retrieve a string preference
    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Add a function to clear all preferences
    fun clearPreferences() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    // Add more functions for other data types as needed
}