package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartsPage(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("content")
    val content: String = ""
) : Parcelable