package com.example.aplikasie_learning.presentation.allmateri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.MaterialsAdapter
import com.example.aplikasie_learning.databinding.ActivityAllMaterialsBinding
import com.example.aplikasie_learning.model.Materials
import com.example.aplikasie_learning.presentation.content.ContentActivity
import com.example.aplikasie_learning.utils.showDialogError
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class AllMaterials : AppCompatActivity() {

    companion object{
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var allMaterialsBinding : ActivityAllMaterialsBinding
    private lateinit var allMaterialsAdapter: MaterialsAdapter
    private lateinit var allMaterialsDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allMaterialsBinding = ActivityAllMaterialsBinding.inflate(layoutInflater)
        setContentView(allMaterialsBinding.root)

        // Init
        allMaterialsAdapter = MaterialsAdapter()
        allMaterialsDatabase = FirebaseDatabase.getInstance().getReference("materials")

        getDataFirebase()
        onAction()
    }

    private fun onAction() {
        allMaterialsBinding.apply {
            btnBackAllMateri.setOnClickListener {
                finish()
            }
        }
        allMaterialsAdapter.onClick { material, position ->
            startActivity<ContentActivity>(
                ContentActivity.EXTRA_MATERIAL to material,
                ContentActivity.EXTRA_POSITION to position
            )
        }
    }

    private var listenerMaterials = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null){
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<Materials>>() {}.type
                val materials = Gson().fromJson<MutableList<Materials>>(json, type)

                materials?.let { allMaterialsAdapter.materials = it }
            }else{
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@AllMaterials, error.message)
        }

    }

    private fun getDataFirebase() {
        allMaterialsDatabase
            .addValueEventListener(listenerMaterials)

        allMaterialsBinding.rvAllMaterials.adapter = allMaterialsAdapter
    }
}