package com.example.aplikasie_learning.presentation.allaksaralontara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.AllAksaraLontaraAdapter
import com.example.aplikasie_learning.databinding.ActivityAllAksaraLontaraBinding
import com.example.aplikasie_learning.model.AksaraLontara
import com.example.aplikasie_learning.utils.showDialogError
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AllAksaraLontaraActivity : AppCompatActivity() {

    private lateinit var allAksaraLontaraBinding: ActivityAllAksaraLontaraBinding
    private lateinit var allAksaraLontaraAdapter: AllAksaraLontaraAdapter
    private lateinit var allAksaraLontaraDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allAksaraLontaraBinding = ActivityAllAksaraLontaraBinding.inflate(layoutInflater)
        setContentView(allAksaraLontaraBinding.root)

        // Init
        allAksaraLontaraAdapter = AllAksaraLontaraAdapter()
        allAksaraLontaraDatabase = FirebaseDatabase.getInstance().getReference("aksaraLontara")

        getDataFirebase()
        onAction()
    }

    private fun onAction() {
        allAksaraLontaraBinding.apply {
            btnBackAllAksara.setOnClickListener {
                finish()
            }
        }
    }

    private fun getDataFirebase() {
        allAksaraLontaraDatabase
            .addValueEventListener(listenerAllAksaraLontara)

        allAksaraLontaraBinding.rvAllAksaraLontara.adapter = allAksaraLontaraAdapter
    }

    private var listenerAllAksaraLontara = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null){
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<AksaraLontara>>() {}.type
                val aksaraLontara = Gson().fromJson<MutableList<AksaraLontara>>(json, type)

                aksaraLontara?.let { allAksaraLontaraAdapter.aksaraLontara = it }
            }else{
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@AllAksaraLontaraActivity, error.message)
        }
    }
}