package com.example.aplikasie_learning.presentation.kuis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasie_learning.databinding.ActivityKuisBinding

class KuisActivity : AppCompatActivity() {

    private lateinit var kuisBinding: ActivityKuisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kuisBinding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(kuisBinding.root)
    }
}