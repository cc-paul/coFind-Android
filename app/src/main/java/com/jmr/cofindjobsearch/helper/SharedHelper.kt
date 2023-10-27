package com.jmr.cofindjobsearch.helper

import android.content.Context
import android.content.SharedPreferences
import com.jmr.cofindjobsearch.MyApp
import java.lang.String.format
import java.text.DecimalFormat

object SharedHelper {
    private const val PREFERENCES_NAME = "cf_preferences"
    private val sharedPreferences: SharedPreferences = MyApp.instance.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun formatNumber(amount: Double): String {
        val amount: Double = amount
        val formatter = DecimalFormat("#,###.00")

        return formatter.format(amount)
    }

}