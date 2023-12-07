package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartsKuis(
    @SerializedName("answer")
    var answer: String = "",

    @SerializedName("option3")
    var option3: String = "",

    @SerializedName("option4")
    var option4: String = "",

    @SerializedName("option1")
    var option1: String = "",

    @SerializedName("option2")
    var option2: String = "",

    @SerializedName("user_answer")
    var userAnswer: String = "",
) : Parcelable