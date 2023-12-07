package com.example.aplikasie_learning.presentation.kuis

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aplikasie_learning.adapter.KuisAdminAdapter
import com.example.aplikasie_learning.databinding.ActivityKuisAdminBinding
import com.example.aplikasie_learning.databinding.LayoutItemAddKuisBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.presentation.listquestionadmin.ListQuestionAdminActivity
import com.example.aplikasie_learning.utils.*
import com.google.firebase.database.*
import org.jetbrains.anko.startActivity

class KuisAdminActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var kuisBinding: ActivityKuisAdminBinding
    private lateinit var kuisAdapter: KuisAdminAdapter
    private lateinit var kuisDatabase: DatabaseReference
    private lateinit var questionDatabase : DatabaseReference
    private lateinit var kuisList: ArrayList<Kuis>
    private var titleKuis = ""
    private var questionId = 0
    private var i: Int
        get() = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getInt("titleId", 0)
        set(value) = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("titleId", value).apply()

    private var listenerMaterials = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                kuisList.clear()
                showData()
                for (kuisSnap in snapshot.children) {
                    val kuisData = kuisSnap.getValue(Kuis::class.java)
                    kuisList.add(kuisData!!)
                }
                kuisAdapter.notifyDataSetChanged()
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@KuisAdminActivity, error.message)
        }

    }

    private fun showData() {
        kuisBinding.apply {
            ivEmptyData.gone()
            rvAllKuisAdmin.visiable()
        }
    }

    private fun showEmptyData() {
        kuisBinding.apply {
            ivEmptyData.visiable()
            rvAllKuisAdmin.gone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kuisBinding = ActivityKuisAdminBinding.inflate(layoutInflater)
        setContentView(kuisBinding.root)

        // Init
        kuisList = arrayListOf<Kuis>()
        kuisAdapter = KuisAdminAdapter(kuisList)
        kuisDatabase = FirebaseDatabase.getInstance().getReference("kuisAdmin")
        questionDatabase = FirebaseDatabase.getInstance().getReference("questionKuis")

        getDataFirebase()
        onAction()
        getDataIntent()
    }

    private fun getDataIntent() {
        if (intent != null) {
            questionId = intent.getIntExtra(EXTRA_ID, 0)
        }
    }

    private fun getDataFirebase() {
        kuisDatabase
            .addValueEventListener(listenerMaterials)

        kuisBinding.rvAllKuisAdmin.adapter = kuisAdapter
    }

    private fun showDialogAddTitle() {
        val dialogAddKuis = LayoutItemAddKuisBinding.inflate(layoutInflater)
        val dialogView = AlertDialog.Builder(this@KuisAdminActivity)
            .setView(dialogAddKuis.root)

        val alertDialog = dialogView.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogAddKuis.btnAddTitleKuis.setOnClickListener {
            alertDialog.dismiss()

            titleKuis = dialogAddKuis.etJudulKuis.text.toString()

            val titleId = i++
            val titleKuisCrud = Kuis(titleId, titleKuis)

            kuisDatabase.child(titleId.toString()).setValue(titleKuisCrud)
                .addOnCompleteListener {
                    Toast.makeText(this, "Kategori kuis berhasil ditambahkan", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Kategori kuis tidak berhasil ditambahkan", Toast.LENGTH_LONG).show()
                }
        }

        dialogAddKuis.btnCancelTitleKuis.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun onAction() {
        kuisBinding.apply {
            btnBackAllKuis.setOnClickListener {
                finish()
            }
            btnAddKuis.setOnClickListener {
                showDialogAddTitle()
            }
        }
        kuisAdapter.onClick { titleKuis, titleId ->
            startActivity<ListQuestionAdminActivity>(
                ListQuestionAdminActivity.EXTRA_KUIS to titleKuis,
                ListQuestionAdminActivity.EXTRA_POSITION to titleId,
                ListQuestionAdminActivity.EXTRA_ID to questionId
            )
        }

        kuisAdapter.setOnClickDelete { idKuis, adapterPosition ->
            AlertDialog.Builder(this@KuisAdminActivity)
                .setTitle("Hapus Pertanyaan")
                .setMessage("Apakah kamu yakin akan menghapus pertanyaan ini?")
                .setPositiveButton("Ya") { dialog, which ->
                    val selectedId = idKuis.titleId.toString()
                    val selectedTitle = idKuis.titleKuis

                    kuisDatabase.child(selectedId).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@KuisAdminActivity,
                                "Data berhasil dihapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@KuisAdminActivity,
                                "Data tidak terhapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    questionDatabase.child(selectedTitle.toString()).removeValue()
                    Log.d("title kuis", "title kuisnya $selectedTitle")
                    kuisList.removeAt(adapterPosition)
                    kuisAdapter.notifyItemRemoved(adapterPosition)
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}