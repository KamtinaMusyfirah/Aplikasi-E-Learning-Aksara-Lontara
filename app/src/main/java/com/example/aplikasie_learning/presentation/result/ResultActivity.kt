package com.example.aplikasie_learning.presentation.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasie_learning.databinding.ActivityResultBinding
import com.example.aplikasie_learning.presentation.main.MainActivity
import org.jetbrains.anko.startActivity
import com.example.aplikasie_learning.R

class ResultActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_CORRECT = "extra_correct"
        const val EXTRA_WRONG = "extra_wrong"
    }

    private lateinit var resultBinding: ActivityResultBinding
    var score = 0
    var user_wrong = 0
    var user_correct = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(resultBinding.root)

        onAction()
        getDataIntent()
        imageResult()
        textResult()
    }

    private fun getDataIntent() {
        if (intent != null){
            score = intent.getIntExtra("score",0)
            user_wrong = intent.getIntExtra("userWrong",0)
            user_correct = intent.getIntExtra("userCorrect", 0)

            resultBinding.scoreQuiz.text = score.toString()
            resultBinding.tvCorrectAnswer.text = user_correct.toString()
            resultBinding.tvWrongAnswer.text = user_wrong.toString()
        }
    }

    private fun imageResult() {
        if (score <= 70){
            resultBinding.ivImageResult.setImageResource(R.drawable.crying)
        }else{
            resultBinding.ivImageResult.setImageResource(R.drawable.trophy)
        }
    }

    private fun textResult() {
        if (score <= 70){
            resultBinding.tvUmpanBalik.setText("COBA LAGI YAA \n terus belajar! Skor Kamu yaitu")
        }else{
            resultBinding.tvUmpanBalik.setText("KEREEN \n Score yang kamu dapatkan yaitu")
        }
    }


    private fun onAction() {
        resultBinding.apply {
            btnCloseScore.setOnClickListener {
                startActivity<MainActivity>()
            }
        }
    }
}