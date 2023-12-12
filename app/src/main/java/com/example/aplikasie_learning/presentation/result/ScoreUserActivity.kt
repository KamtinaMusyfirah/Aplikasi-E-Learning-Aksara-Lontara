package com.example.aplikasie_learning.presentation.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.SubScoreAdapter
import com.example.aplikasie_learning.databinding.ActivityScoreUserBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.model.ScoreUser
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ScoreUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var scoreUserBinding: ActivityScoreUserBinding
    private lateinit var scoreList : ArrayList<ScoreUser>
    private lateinit var scoreDatabase : DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var scoreAdapter : SubScoreAdapter
    private var kuisTitle: Kuis? = null
    private var userUID= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreUserBinding = ActivityScoreUserBinding.inflate(layoutInflater)
        setContentView(scoreUserBinding.root)

        kuisTitle = intent.getParcelableExtra<Kuis>(EXTRA_KUIS)
        scoreList = arrayListOf<ScoreUser>()
        scoreDatabase = FirebaseDatabase.getInstance().getReference("scoreKuis").child(kuisTitle?.titleKuis.toString())
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        scoreAdapter = SubScoreAdapter(scoreList)

        scoreDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterasi melalui setiap child di bawah "titleKuis"
                for (userSnapshot in dataSnapshot.children) {
                    // Mendapatkan userUID dari setiap child
                    userUID = userSnapshot.key.toString()
                    // Lakukan sesuatu dengan userUID (misalnya, tambahkan ke daftar atau cetak)
                    println("User UID: $userUID")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika diperlukan
                println("Database error: ${databaseError.message}")
            }
        })

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
        scoreDatabase.child(userUID)
            .addValueEventListener(listenerScore)

        scoreUserBinding.rvMainScore.adapter = scoreAdapter
    }
}