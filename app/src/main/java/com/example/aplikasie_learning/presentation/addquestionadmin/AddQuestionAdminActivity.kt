package com.example.aplikasie_learning.presentation.addquestionadmin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.aplikasie_learning.databinding.ActivityAddQuestionAdminBinding
import com.example.aplikasie_learning.model.QuestionAdmin
import com.example.aplikasie_learning.presentation.kuis.KuisAdminActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.startActivity

class AddQuestionAdminActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var questionDatabase : DatabaseReference
    private lateinit var addQuestionAdminBinding: ActivityAddQuestionAdminBinding
    private var correct = -1
    private var idKuis = 0
    private var questionId = 0
    private var question = ""
    private var titleKuis = ""
    private var a : Int
        get() = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getInt("questionId", 0)
        set(value) = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("questionId", value).apply()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addQuestionAdminBinding = ActivityAddQuestionAdminBinding.inflate(layoutInflater)
        setContentView(addQuestionAdminBinding.root)

        questionDatabase = FirebaseDatabase.getInstance().getReference("questionKuis")
        titleKuis = intent.getStringExtra(EXTRA_KUIS).toString()
        addQuestionAdminBinding.tvTitleKuis.text = titleKuis

        onAction()
        getDataIntent()

    }

    private fun getDataIntent() {
        if (intent != null){
            idKuis = intent.getIntExtra(EXTRA_POSITION, 0)
        }
    }

    private fun saveQuestionData() {
        correct = -1
        for(i in 0 until addQuestionAdminBinding.optionContainer.childCount){
            val answer = addQuestionAdminBinding.answerContainer.getChildAt(i) as EditText
            if (answer.text.toString().isEmpty()){
                answer.error = "Required"
                return
            }

            val option = addQuestionAdminBinding.optionContainer.getChildAt(i) as RadioButton
            if (option.isChecked){
                correct = i
                break
            }
        }

        if (correct == -1){
            Toast.makeText(this, "mohon untuk memilih jawaban yang benar", Toast.LENGTH_SHORT).show()
            return
        }


        val option1 = (addQuestionAdminBinding.answerContainer.getChildAt(0) as EditText).text.toString()
        val option2 = (addQuestionAdminBinding.answerContainer.getChildAt(1) as EditText).text.toString()
        val option3 = (addQuestionAdminBinding.answerContainer.getChildAt(2) as EditText).text.toString()
        val option4 = (addQuestionAdminBinding.answerContainer.getChildAt(3) as EditText).text.toString()
        val answer = (addQuestionAdminBinding.answerContainer.getChildAt(correct) as EditText).text.toString()
        question = addQuestionAdminBinding.inputQuestion.text.toString()
        titleKuis = intent.getStringExtra(EXTRA_KUIS).toString()
        questionId = a++

        val questionAdmin = QuestionAdmin(titleKuis, answer,option1, option2, option3, option4, question, questionId)

        questionDatabase.child(titleKuis).child("question").child(questionId.toString()).setValue(questionAdmin)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Data not inserted", Toast.LENGTH_LONG).show()
            }
    }

    private fun onAction() {
        addQuestionAdminBinding.apply {
            btnUploadQuestion.setOnClickListener {
                saveQuestionData()
            }
            btnCloseQuestionAdmin.setOnClickListener {
                finish()
            }
            btnDoneQuestion.setOnClickListener {
                startActivity<KuisAdminActivity>(
                    KuisAdminActivity.EXTRA_ID to questionId
                )
            }
        }
    }


}