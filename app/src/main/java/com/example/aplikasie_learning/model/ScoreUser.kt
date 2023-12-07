package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreUser(
    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("score")
    val score: Int = 0,

    @SerializedName("title_kuis")
    val title_kuis : String = ""
): Parcelable
