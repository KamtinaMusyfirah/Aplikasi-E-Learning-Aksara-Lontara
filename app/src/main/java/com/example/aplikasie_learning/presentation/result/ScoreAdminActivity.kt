package com.example.aplikasie_learning.presentation.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aplikasie_learning.adapter.ScoreAdminAdapter
import com.example.aplikasie_learning.databinding.ActivityScoreAdminBinding
import com.example.aplikasie_learning.model.Kuis
import com.example.aplikasie_learning.utils.gone
import com.example.aplikasie_learning.utils.showDialogError
import com.example.aplikasie_learning.utils.visiable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.startActivity

class ScoreAdminActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_KUIS = "extra_kuis"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var scoreBinding : ActivityScoreAdminBinding
    private lateinit var kuisList : ArrayList<Kuis>
    private lateinit var scoreDatabase : DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var kuisDatabase : DatabaseReference
    private lateinit var scoreAdapter : ScoreAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreBinding = ActivityScoreAdminBinding.inflate(layoutInflater)
        setContentView(scoreBinding.root)

        kuisList = arrayListOf<Kuis>()
        scoreDatabase = FirebaseDatabase.getInstance().getReference("scoreKuis")
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        kuisDatabase = FirebaseDatabase.getInstance().getReference("questionKuis")
        kuisDatabase = FirebaseDatabase.getInstance().getReference("kuisAdmin")
        scoreAdapter = ScoreAdminAdapter(kuisList)

        onAction()
        getDataFirebase()
    }

    private fun onAction() {
        scoreBinding.apply {
            btnBackHasilKuis.setOnClickListener {
                finish()
            }
        }
        scoreAdapter.onClick { kuis, i ->
            startActivity<ScoreUserActivity>(
                ScoreUserActivity.EXTRA_KUIS to kuis,
                ScoreUserActivity.EXTRA_POSITION to i
            )
        }
    }

    private fun getDataFirebase() {
        kuisDatabase
            .addValueEventListener(listenerMaterials)

        scoreBinding.rvMainScore.adapter = scoreAdapter
    }

    private var listenerMaterials = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                kuisList.clear()
                showData()
                for (kuisSnap in snapshot.children) {
                    val kuisData = kuisSnap.getValue(Kuis::class.java)
                    kuisList.add(kuisData!!)
                }
                scoreAdapter.notifyDataSetChanged()
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@ScoreAdminActivity, error.message)
        }

    }

    private fun showData() {
        scoreBinding.apply {
            ivEmptyData.gone()
            rvMainScore.visiable()
        }
    }

    private fun showEmptyData() {
        scoreBinding.apply {
            ivEmptyData.visiable()
            rvMainScore.gone()
        }
    }

}