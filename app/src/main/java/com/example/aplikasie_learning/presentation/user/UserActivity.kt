package com.example.aplikasie_learning.presentation.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.aplikasie_learning.databinding.ActivityUserBinding
import com.example.aplikasie_learning.model.User
import com.example.aplikasie_learning.presentation.allmateri.changepassword.ChangePasswordActivity
import com.example.aplikasie_learning.presentation.login.LoginActivity
import com.example.aplikasie_learning.utils.showDialogError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var userBinding: ActivityUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference
    private var currentUser: FirebaseUser? = null

    private var listenerUser = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                userBinding.tvNameUser.text = it.nameUser
                userBinding.tvEmailUser.text = it.emailUser

                Glide
                    .with(this@UserActivity)
                    .load(it.avatarUser)
                    .placeholder(android.R.color.white)
                    .into(userBinding.ivAvatarUser)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            showDialogError(this@UserActivity, error.message)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        currentUser = firebaseAuth.currentUser

        getDataFirebase()
        onAction()
    }

    private fun onAction() {
        userBinding.apply {
            btnCloseUser.setOnClickListener {
                finish()
            }

            btnChangePasswordUser.setOnClickListener {
                startActivity<ChangePasswordActivity>()
            }

            btnLogoutUser.setOnClickListener {
                firebaseAuth.signOut()
                startActivity<LoginActivity>()
            }

            swipeUser.setOnRefreshListener {
                getDataFirebase()
            }
        }
    }

    private fun getDataFirebase() {
        showLoading()
        userDatabase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)
    }

    private fun showLoading(){
        userBinding.swipeUser.isRefreshing = true
    }

    private fun hideLoading(){
        userBinding.swipeUser.isRefreshing = false
    }
}