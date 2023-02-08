package com.example.aplikasie_learning.presentation.question

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasie_learning.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {

    private lateinit var questionBinding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionBinding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(questionBinding.root)
    }
}