package com.example.aplikasie_learning.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Response(
	val response: List<ResponseItem?>? = null
) : Parcelable

@Parcelize
data class ResponseItem(
	val kuis: List<Kuis?>? = null,
	val idKuis: Int? = null
) : Parcelable



@Parcelize
data class Answers(
	val answer: String? = null,
	val correctAnswer: Boolean? = null,
	val isClick: Boolean? = null,
	val option: String? = null
) : Parcelable
