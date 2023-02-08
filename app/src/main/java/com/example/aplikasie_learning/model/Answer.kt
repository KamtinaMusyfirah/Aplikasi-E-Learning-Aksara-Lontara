package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(

    @field:SerializedName("answer")
    val answer: String? = null,

    @field:SerializedName("correct_answer")
    val correctAnswer: Boolean? = null,

    @field:SerializedName("is_click")
    val isClick: Boolean? = null,

    @field:SerializedName("optionf")
    val option: String? = null
) : Parcelable
