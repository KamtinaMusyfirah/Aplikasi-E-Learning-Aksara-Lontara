package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Materials(
    @SerializedName("thumbnail_material")
    val thumbnailMaterial: String? = null,
    @SerializedName("title_material")
    val titleMaterial: String? = null,
    @SerializedName("id_material")
    val idMaterial: Int? = null
) : Parcelable