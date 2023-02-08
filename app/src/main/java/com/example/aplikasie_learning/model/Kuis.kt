package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kuis(
    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("question")
    val question: String? = null,

    @field:SerializedName("answers")
    val answers: List<Answer>? = null
    ) : Parcelable

