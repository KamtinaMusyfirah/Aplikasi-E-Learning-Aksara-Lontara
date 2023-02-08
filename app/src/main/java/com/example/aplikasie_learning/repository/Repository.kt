package com.example.aplikasie_learning.repository

import android.content.Context
import com.example.aplikasie_learning.model.Content
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.Kuiss
import com.example.aplikasie_learning.model.Materials
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object Repository {
    fun getMaterials(context : Context?) : MutableList<Materials>?{
        val json : String
        try {
            val inputStream = context?.assets?.open("json/materials.json")
            json = inputStream?.bufferedReader().use { it?.readText().toString() }
        } catch (e: IOException){
            e.printStackTrace()
            return null
        }

        val type = object : TypeToken<MutableList<Materials>>() {}.type
        val materials = Gson().fromJson<MutableList<Materials>>(json,type)
        materials?.let { return it }

        return null
    }

    fun getContents(context : Context?) : MutableList<Content>?{
        val json : String
        try {
            val inputStream = context?.assets?.open("json/content.json")
            json = inputStream?.bufferedReader().use { it?.readText().toString() }
        } catch (e: IOException){
            e.printStackTrace()
            return null
        }

        val type = object : TypeToken<MutableList<Content>>() {}.type
        val contents = Gson().fromJson<MutableList<Content>>(json,type)
        contents?.let { return it }

        return null
    }
    fun getDataKuis(context: Context?): List<Kuis?>? {
        val json : String?
        try {
            val inputStream = context?.assets?.open("json/kuis.json")
            json = inputStream?.bufferedReader().use { it?.readText() }
        } catch (e: IOException){
            return null
        }
        val kuis = Gson().fromJson(json, Kuiss::class.java)
        return kuis.kuis
    }
}