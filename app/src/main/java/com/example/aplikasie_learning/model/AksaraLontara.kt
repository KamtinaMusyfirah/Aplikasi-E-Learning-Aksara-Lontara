package com.example.aplikasie_learning.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AksaraLontara(

	@field:SerializedName("aksara_latin")
	val aksaraLatin: String? = null,

	@field:SerializedName("aksara_lontara")
	val aksaraLontara: String? = null,

	@field:SerializedName("id_aksara")
	val idAksara: Int? = null
) : Parcelable
