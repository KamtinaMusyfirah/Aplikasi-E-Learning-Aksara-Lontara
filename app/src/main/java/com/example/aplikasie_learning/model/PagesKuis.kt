package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagesKuis(
    @SerializedName("options")
    val options: List<Options>? = null,

    @SerializedName("question")
    val question: String? = null
) : Parcelable