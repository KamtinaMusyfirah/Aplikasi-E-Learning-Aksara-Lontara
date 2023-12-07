package com.example.aplikasie_learning.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kuis(
    val titleId: Int? = null,
    val titleKuis: String? = null
): Parcelable
