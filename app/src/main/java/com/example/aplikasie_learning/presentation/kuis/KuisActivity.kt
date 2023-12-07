package com.example.aplikasie_learning.presentation.kuis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.KuisAdapter
import com.example.aplikasie_learning.databinding.ActivityKuisBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.presentation.question.QuestionNewActivity
import com.example.aplikasie_learning.utils.showDialogError
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class KuisActivity : AppCompatActivity() {

    private lateinit var kuisBinding : ActivityKuisBinding
    private lateinit var kuisAdapter: KuisAdapter
    private lateinit var kuisDatabase : DatabaseReference
    private lateinit var kuisList : ArrayList<Kuis>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kuisBinding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(kuisBinding.root)

        // Init
        kuisList = arrayListOf<Kuis>()
        kuisAdapter = KuisAdapter()
        kuisDatabase = FirebaseDatabase.getInstance().getReference("kuisAdmin")

        getDataFirebase()
        onAction()
    }

    private fun onAction() {
        kuisBinding.apply {
            btnBackAllKuis.setOnClickListener {
                finish()
            }
        }
        kuisAdapter.onClick { material, position ->
            startActivity<QuestionNewActivity>(
                QuestionNewActivity.EXTRA_KUIS to material,
                QuestionNewActivity.EXTRA_POSITION to position
            )
        }
    }

    private var listenerMaterials = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null){
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<Kuis>>() {}.type
                val kuis = Gson().fromJson<MutableList<Kuis>>(json, type)

                kuis?.let { kuisAdapter.materials = it }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@KuisActivity, error.message)
        }

    }

    private fun getDataFirebase() {
        kuisDatabase
            .addValueEventListener(listenerMaterials)

        kuisBinding.rvAllKuis.adapter = kuisAdapter
    }
}