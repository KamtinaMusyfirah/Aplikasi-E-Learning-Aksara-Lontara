package com.example.aplikasie_learning.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Materials(

	@field:SerializedName("thumbnail_material")
	val thumbnailMaterial: String? = null,

	@field:SerializedName("thumbnail_kuis")
	val thumbnailKuis: String? = null,

	@field:SerializedName("title_material")
	val titleMaterial: String? = null,

	@field:SerializedName("part_material")
	val partMaterial: String? = null,

	@field:SerializedName("id_material")
	val idMaterial: Int? = null,
) : Parcelable
