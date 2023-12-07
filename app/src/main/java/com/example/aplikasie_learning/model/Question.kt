package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    @SerializedName("pages_kuis")
    val pagesKuis: List<PagesKuis>? = null,

    @SerializedName("id_kuis")
    val idKuis: Int? = null
) : Parcelable
