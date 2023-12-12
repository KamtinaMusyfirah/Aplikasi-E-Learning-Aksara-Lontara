package com.example.aplikasie_learning.presentation.listquestionadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aplikasie_learning.adapter.ListQuestionAdapter
import com.example.aplikasie_learning.databinding.ActivityListQuestionAdminBinding
import com.example.aplikasie_learning.model.*
import com.example.aplikasie_learning.presentation.addquestionadmin.AddQuestionAdminActivity
import com.example.aplikasie_learning.utils.gone
import com.example.aplikasie_learning.utils.showDialogError
import com.example.aplikasie_learning.utils.visiable
import com.google.firebase.database.*
import org.jetbrains.anko.startActivity

class ListQuestionAdminActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var listQuestionAdminBinding: ActivityListQuestionAdminBinding
    private lateinit var listQuestionAdapter: ListQuestionAdapter
    private lateinit var listQuestionDatabase : DatabaseReference
    private lateinit var questionList : ArrayList<QuestionAdmin>
    private var kuisPoistion = 0


    private var listenerListQuestion = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                questionList.clear()
                showData()
                for (questionSnap in snapshot.children){
                    val questionData = questionSnap.getValue(QuestionAdmin::class.java)
                    questionList.add(questionData!!)
                }
                listQuestionAdminBinding.rvListQuestion.adapter = listQuestionAdapter
                listQuestionAdapter.notifyDataSetChanged()
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
        }

    }

    private fun showData() {
        listQuestionAdminBinding.apply {
            ivEmptyDataKuis.gone()
            rvListQuestion.visiable()
        }
    }

    private fun showEmptyData() {
        listQuestionAdminBinding.apply {
            ivEmptyDataKuis.visiable()
            rvListQuestion.gone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listQuestionAdminBinding = ActivityListQuestionAdminBinding.inflate(layoutInflater)
        setContentView(listQuestionAdminBinding.root)

        //Init
        questionList = arrayListOf<QuestionAdmin>()
        listQuestionAdapter = ListQuestionAdapter(questionList)
        listQuestionDatabase = FirebaseDatabase.getInstance().getReference("questionKuis")



        onAction()
        getDataIntent()
        getDataFirebase()
    }

    private fun getDataIntent() {
        if (intent != null){
            kuisPoistion = intent.getIntExtra(EXTRA_POSITION,0)
            val kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)

            listQuestionAdminBinding.tvTitleKuis.text = kuisTitle?.titleKuis

            kuisTitle?.let { getDataKuis(kuisTitle) }
        }

    }

    private fun getDataKuis(kuis: Kuis) {
        listQuestionDatabase
            .child(kuis.titleId.toString())
            .addValueEventListener(listenerListQuestion)
    }

    private fun getDataFirebase() {
        if (intent != null){
            val kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)

            listQuestionDatabase.child(kuisTitle?.titleKuis.toString()).child("question").orderByChild("titleKuis").equalTo(kuisTitle?.titleKuis.toString())
                .addValueEventListener(listenerListQuestion)

            listQuestionAdminBinding.rvListQuestion.adapter = listQuestionAdapter
        }
    }

    private fun onAction() {
        val kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)
        listQuestionAdminBinding.apply {
            btnAddQuestion.setOnClickListener {
                startActivity<AddQuestionAdminActivity>(
                    AddQuestionAdminActivity.EXTRA_KUIS to kuisTitle?.titleKuis,
                    AddQuestionAdminActivity.EXTRA_POSITION to kuisPoistion
                )
            }
            btnCloseListQuestion.setOnClickListener {
                finish()
            }
        }

        listQuestionAdapter.setOnClickDelete { idQuestion, adapterPosition ->
            AlertDialog.Builder(this@ListQuestionAdminActivity)
                .setTitle("Hapus Pertanyaan")
                .setMessage("Apakah kamu yakin akan menghapus pertanyaan ini?")
                .setPositiveButton("Ya") { dialog, which ->
                    val selectedId = idQuestion.questionId.toString()
                    val kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)

                    listQuestionDatabase.child(kuisTitle?.titleKuis.toString()).child("question").child(selectedId).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@ListQuestionAdminActivity,
                                "Data berhasil dihapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@ListQuestionAdminActivity,
                                "Data tidak terhapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    Log.d("Title Id", "title id $selectedId")
                    questionList.removeAt(adapterPosition)
                    listQuestionAdapter.notifyItemRemoved(adapterPosition)
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}