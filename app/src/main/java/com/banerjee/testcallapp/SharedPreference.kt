package com.banerjee.testcallapp

import android.app.SearchManager.USER_QUERY
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class SharedPreference {

    private val USER_STATUS_CODE = "userStatusCode"
    private val USER_DETAILS = "userDetails"

    fun getUserStatusCode(context: Context): String? {
        val SPUserDetails: SharedPreferences =
            context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE)
        return SPUserDetails.getString(USER_STATUS_CODE, "")
    }

    fun setUserStatusCode(context: Context, value: String?) {
        val SPUserDetails: SharedPreferences =
            context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE)
        val editor = SPUserDetails.edit()
        editor.putString(USER_STATUS_CODE, value)
        editor.apply()
    }

}