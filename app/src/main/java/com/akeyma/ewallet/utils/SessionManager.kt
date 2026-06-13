package com.akeyma.ewallet.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AKEYMA_SESSION", Context.MODE_PRIVATE)

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_EMAIL = "email"
        const val KEY_NAME = "name"
        const val KEY_PHONE = "phone"
        const val KEY_BALANCE = "balance"
    }

    fun saveLoginSession(email: String, name: String, phone: String, balance: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            putString(KEY_PHONE, phone)
            putString(KEY_BALANCE, balance)
        }.apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""
    fun getUserName(): String = prefs.getString(KEY_NAME, "User") ?: "User"
    fun getUserPhone(): String = prefs.getString(KEY_PHONE, "") ?: ""
    fun getBalance(): String = prefs.getString(KEY_BALANCE, "0") ?: "0"

    fun updateBalance(newBalance: String) {
        prefs.edit().putString(KEY_BALANCE, newBalance).apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}