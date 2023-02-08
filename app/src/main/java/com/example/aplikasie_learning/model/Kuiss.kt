package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kuiss(

    @field:SerializedName("kuis")
    val kuis: List<Kuis?>? = null,

    @field:SerializedName("id_kuis")
    val idKuis: Int? = null
) : Parcelable
