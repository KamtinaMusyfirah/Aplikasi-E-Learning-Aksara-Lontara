package com.example.aplikasie_learning.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.aplikasie_learning.adapter.*
import com.example.aplikasie_learning.databinding.ActivityMainAdminBinding
import com.example.aplikasie_learning.model.*
import com.example.aplikasie_learning.presentation.result.ScoreAdminActivity
import com.example.aplikasie_learning.presentation.kuis.KuisAdminActivity
import com.example.aplikasie_learning.presentation.listquestionadmin.ListQuestionAdminActivity
import com.example.aplikasie_learning.presentation.user.UserActivity
import com.example.aplikasie_learning.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.jetbrains.anko.startActivity

class MainAdminActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var mainBinding: ActivityMainAdminBinding
    private lateinit var userDatabase: DatabaseReference
    private lateinit var kuisAdapter: KuisAdminAdapter
    private lateinit var kuisDatabase : DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var kuisList : ArrayList<Kuis>


    private var listenerUser = object  : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                mainBinding.apply {
                    tvNameUserMain.text = it.nameUser

                    Glide
                        .with(this@MainAdminActivity)
                        .load(it.avatarUser)
                        .placeholder(android.R.color.white)
                        .into(ivAvatarMain)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainAdminActivity, error.message)
        }
    }

    private var listenerKuis = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.exists()){
                kuisList.clear()
                showData()
                for (kuisSnap in snapshot.children){
                    val kuisData = kuisSnap.getValue(Kuis::class.java)
                    kuisList.add(kuisData!!)
                }
                kuisAdapter.notifyDataSetChanged()
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[OnCancelled] - ${error.message}")
            showDialogError(this@MainAdminActivity, error.message)
        }

    }

    private fun showData() {
        mainBinding.apply {
            ivEmptyData.gone()
            etSearchMain.enabled()
            rvKuisMain.visiable()
        }
    }

    private fun showEmptyData() {
        mainBinding.apply {
            ivEmptyData.visiable()
            etSearchMain.disabled()
            rvKuisMain.gone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Init
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        kuisDatabase = FirebaseDatabase.getInstance().getReference("kuisAdmin")
        kuisList = arrayListOf<Kuis>()
        kuisAdapter = KuisAdminAdapter(kuisList)
        currentUser = FirebaseAuth.getInstance().currentUser

        getDataFirebase()
        onAction()
    }

    private fun getDataFirebase() {
        showLoading()
        userDatabase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)

        kuisDatabase
            .addValueEventListener(listenerKuis)

        mainBinding.rvKuisMain.adapter = kuisAdapter
    }

//    override fun onResume() {
//        super.onResume()
//        if (intent != null){
//            val position = intent.getIntExtra(EXTRA_POSITION, 0)
//        }
//    }

    private fun onAction() {
        mainBinding.apply {
            ivAvatarMain.setOnClickListener{
                startActivity<UserActivity>()
            }

            swipeMain.setOnRefreshListener {
                getDataFirebase()
            }

            btnSeeMoreKuis.setOnClickListener {
                startActivity<KuisAdminActivity>()
            }
            btnScore.setOnClickListener {
                startActivity<ScoreAdminActivity>()
            }
        }

        kuisAdapter.onClick { material, position ->
            startActivity<ListQuestionAdminActivity>(
                ListQuestionAdminActivity.EXTRA_KUIS to material,
                ListQuestionAdminActivity.EXTRA_POSITION to position
            )
        }

        kuisAdapter.setOnClickDelete { idKuis, adapterPosition ->
            AlertDialog.Builder(this@MainAdminActivity)
                .setTitle("Hapus Pertanyaan")
                .setMessage("Apakah kamu yakin akan menghapus pertanyaan ini?")
                .setPositiveButton("Ya") { dialog, which ->
                    val selectedId = idKuis.titleId.toString()

                    kuisDatabase.child(selectedId).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@MainAdminActivity,
                                "Data berhasil dihapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@MainAdminActivity,
                                "Data tidak terhapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    Log.d("Title Id", "title id $selectedId")
                    kuisList.removeAt(adapterPosition)
                    kuisAdapter.notifyItemRemoved(adapterPosition)
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun showLoading() {
        mainBinding.swipeMain.isRefreshing = true
    }

    private fun hideLoading() {
        mainBinding.swipeMain.isRefreshing = false
    }

}