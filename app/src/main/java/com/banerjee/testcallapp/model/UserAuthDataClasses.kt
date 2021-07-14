package com.banerjee.testcallapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class UserAuthDataClasses {

    @Parcelize
    data class SignUpModel(
        val Name: String,
        val Mobile: String,
        val Email: String,
        val Password: String,
        val CountryCode: String,
        val isTerms: String,
        val IpAddress: String? = "",
        val AffiliateId: String? = "",
        val oneSignalId: String? = "0",
        val Location: String? = "Android"
    ): Parcelable

    @Parcelize
    data class SignUpResponse(
        val StatusCode: String,
        val userMobile: String,
        val userid: String,
        val userName: String,
        val Package_type: String,
        val Data: String,
        val userType: String,
        val TodaysCalls: String,
        val Redial: String,
        val PayPalSubscriptionId: String,
        val SubscriptionExpiryDate: String,
        val SubscriptionCancelDate: String,
        val parentAccount: String,
        val subscriptionType: String,
        val Message: String
    ):Parcelable

}