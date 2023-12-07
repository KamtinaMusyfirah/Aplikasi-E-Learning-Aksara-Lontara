package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionAdmin(
    @SerializedName("title_kuis")
    val titleKuis: String? = null,

    @SerializedName("answer")
    val answer: String? = null,

    @SerializedName("option1")
    val option1: String? = null,

    @SerializedName("option2")
    val option2: String? = null,

    @SerializedName("option3")
    val option3: String? = null,

    @SerializedName("option4")
    val option4: String? = null,

    @SerializedName("question")
    val question: String? = null,

    @SerializedName("question_id")
    var questionId: Int? = null
): Parcelable
