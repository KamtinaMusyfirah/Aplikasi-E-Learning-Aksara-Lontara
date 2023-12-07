package com.example.aplikasie_learning.presentation.question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.aplikasie_learning.databinding.ActivityQuestionNewBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.QuestionAdmin
import com.example.aplikasie_learning.model.ScoreUser
import com.example.aplikasie_learning.presentation.result.ResultActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class QuestionNewActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var questionBinding : ActivityQuestionNewBinding
    private lateinit var questionDatabase : DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var scoreDatabase : DatabaseReference
    private var questionList = ArrayList<QuestionAdmin>()
    private var currentUser: FirebaseUser? = null
    private var position = 0
    private var userCorrect = 0
    private var userWrong = 0
    private var currentPosition = 0
    private var totalScore = 0
    private var totalQuestion = 0
    private var kuisTitle: Kuis? = null
    private var username = ""
    private var lastClickedOption: AppCompatButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionBinding = ActivityQuestionNewBinding.inflate(layoutInflater)
        setContentView(questionBinding.root)

        questionDatabase = FirebaseDatabase.getInstance().getReference("questionKuis")
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        scoreDatabase = FirebaseDatabase.getInstance().getReference("scoreKuis")
        questionList = arrayListOf<QuestionAdmin>()

        getDataFirebase()
        onAction()
//        checkAnsw()
    }

    private fun getDataFirebase() {
        kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)
        questionDatabase.child(kuisTitle?.titleKuis.toString()).child("question").orderByChild("titleKuis").equalTo(kuisTitle?.titleKuis.toString())
            .addValueEventListener(listenerQuestion)

    }

    private var listenerQuestion = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                for (questionSnap in snapshot.children){
                    val questionData = questionSnap.getValue(QuestionAdmin::class.java)
                    questionList.add(questionData!!)

                    questionBinding.question.text = questionList[position].question
                    questionBinding.option1.text = questionList[position].option1
                    questionBinding.option2.text = questionList[position].option2
                    questionBinding.option3.text = questionList[position].option3
                    questionBinding.option4.text = questionList[position].option4

                    updateCurrentPositionText()

                }
                if (questionList.size > 0) {
                    val optionContainers = questionBinding.optionContainers

                    for (i in 0 until 4) {
                        optionContainers.getChildAt(i).setOnClickListener { view ->
                            clickedOption(view as AppCompatButton)

                        }
                    }
                }
                else{
                    Toast.makeText(this@QuestionNewActivity, "Pertanyaan Tidak Tersedia", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@QuestionNewActivity, "Pertanyaan Tidak Tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(this@QuestionNewActivity, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onAction() {
        questionBinding.apply {
            questionBinding.btnNextKuis.setOnClickListener {
                if (position < questionList.size - 1){
                    position++
                    loadNextQuestion()
                    updateCurrentPositionText()
                    checkAnsw()
                }else{
                    checkAnsw()
                    totalQuestion = questionList.size
                    totalScore = (userCorrect.toDouble() / totalQuestion * 100).toInt()
                    intent = Intent(this@QuestionNewActivity, ResultActivity::class.java)
                    intent.putExtra("score", totalScore)
                    intent.putExtra("totalQuestion", totalQuestion)
                    intent.putExtra("userCorrect", userCorrect)
                    intent.putExtra("userWrong", userWrong)
                    startActivity(intent)
                    sendScore()
                    finish()
                }
            }

            questionBinding.btnPrevKuis.setOnClickListener {
                if (position > 0) {
                    position--
                    loadPreviousQuestion()
                    updateCurrentPositionText()
                }
            }
        }
    }

    private fun updateCurrentPositionText() {
        val totalIndex = questionList.size
        currentPosition = position
        val textIndex = "${currentPosition + 1} / $totalIndex"
        questionBinding.tvIndexKuis.text = textIndex
    }

    private fun loadPreviousQuestion() {
        if (position >= 0 && position < questionList.size) {
            val previousQuestion = questionList[position]

            questionBinding.question.text = previousQuestion.question
            questionBinding.option1.text = previousQuestion.option1
            questionBinding.option2.text = previousQuestion.option2
            questionBinding.option3.text = previousQuestion.option3
            questionBinding.option4.text = previousQuestion.option4
        }
    }

    private fun loadNextQuestion() {
        resetOptionBackgrounds()
        questionBinding.question.text = questionList[position].question
        questionBinding.option1.text = questionList[position].option1
        questionBinding.option2.text = questionList[position].option2
        questionBinding.option3.text = questionList[position].option3
        questionBinding.option4.text = questionList[position].option4
    }

    private fun clickedOption(option: AppCompatButton){
        resetOptionBackgrounds()

        // Set the background of the clicked option
        option.setBackgroundResource(com.example.aplikasie_learning.R.drawable.selected_btn)

        lastClickedOption = option
    }


    private fun checkAnsw() {
        if (lastClickedOption != null) {
            if (lastClickedOption?.text.toString().equals(questionList[position].answer)) {
                userCorrect++
            } else {
                userWrong++
            }
        }
    }

    private fun resetOptionBackgrounds() {
        questionBinding.option1.setBackgroundResource(com.example.aplikasie_learning.R.drawable.btn_option_back)
        questionBinding.option2.setBackgroundResource(com.example.aplikasie_learning.R.drawable.btn_option_back)
        questionBinding.option3.setBackgroundResource(com.example.aplikasie_learning.R.drawable.btn_option_back)
        questionBinding.option4.setBackgroundResource(com.example.aplikasie_learning.R.drawable.btn_option_back)
    }

    private fun sendScore(){
        val user = Firebase.auth.currentUser
        val userUID = user?.uid
        userDatabase.child(userUID.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    username = snapshot.child("name_user").value.toString()

                    sendUserToDatabase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendUserToDatabase() {
        val user = Firebase.auth.currentUser

        val score = ScoreUser(username,totalScore,kuisTitle?.titleKuis.toString())
        user?.let {
            scoreDatabase.child(kuisTitle?.titleKuis.toString()).push().setValue(score)
        }
    }
}