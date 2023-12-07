package com.example.aplikasie_learning.presentation.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.SubScoreAdapter
import com.example.aplikasie_learning.databinding.ActivityScoreUserBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.ScoreUser
import com.google.firebase.database.*

class ScoreUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var scoreUserBinding: ActivityScoreUserBinding
    private lateinit var scoreList : ArrayList<ScoreUser>
    private lateinit var scoreDatabase : DatabaseReference
    private lateinit var scoreAdapter : SubScoreAdapter
    private var kuisTitle: Kuis? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreUserBinding = ActivityScoreUserBinding.inflate(layoutInflater)
        setContentView(scoreUserBinding.root)

        scoreList = arrayListOf<ScoreUser>()
        scoreDatabase = FirebaseDatabase.getInstance().getReference("scoreKuis")
        scoreAdapter = SubScoreAdapter(scoreList)

        getDataFirebase()
        getDataIntent()
        onAction()
    }

    private fun onAction() {
        scoreUserBinding.apply {
            btnBackScoreUser.setOnClickListener {
                finish()
            }
        }
    }

    private var listenerScore = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                scoreList.clear()
                for (scoreSnap in snapshot.children){
                    val scoreData = scoreSnap.getValue(ScoreUser::class.java)
                    scoreList.add(scoreData!!)
                }
                scoreUserBinding.rvMainScore.adapter = scoreAdapter
                scoreAdapter.notifyDataSetChanged()
                Log.e("ScoreUserActivity", "score user: ${scoreList}")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ScoreUserActivity", "Firebase Database Error: ${error.message}")
        }

    }

    private fun getDataIntent() {
        if (intent != null){
            kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)

            scoreUserBinding.tvTitleKuis.text = kuisTitle?.titleKuis
        }
    }

    private fun getDataFirebase() {
        kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)
        scoreDatabase.child(kuisTitle?.titleKuis.toString())
            .addValueEventListener(listenerScore)

        scoreUserBinding.rvMainScore.adapter = scoreAdapter
    }
}