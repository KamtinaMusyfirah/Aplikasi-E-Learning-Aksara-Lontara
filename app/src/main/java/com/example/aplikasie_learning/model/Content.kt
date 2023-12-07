package com.example.aplikasie_learning.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(
    @SerializedName("pages")
    val pages: List<Page>? = null,

    @SerializedName("id_content")
    val idContent: Int? = null
) : Parcelable