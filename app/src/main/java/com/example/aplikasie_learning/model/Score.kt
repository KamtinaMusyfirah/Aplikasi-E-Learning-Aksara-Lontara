package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Score(
    @SerializedName("title_kuis")
    val titleMaterial: String = "",

    @SerializedName("score_user")
    val score: List<ScoreUser>
):Parcelable
