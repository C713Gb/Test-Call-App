package com.banerjee.testcallapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CallDataModel(
    val phone: String,
    val type: String?,
    val date: Date,
    val duration: String
): Parcelable