package com.example.aplikasie_learning.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.presentation.login.LoginActivity
import com.example.aplikasie_learning.presentation.main.MainActivity
import com.example.aplikasie_learning.presentation.main.MainAdminActivity
import com.example.aplikasie_learning.utils.showDialogLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var adminDatabase : DatabaseReference
    private var currentUser: FirebaseUser? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Cek apakah pengguna saat ini adalah admin
        firebaseAuth = FirebaseAuth.getInstance()
        adminDatabase = FirebaseDatabase.getInstance().getReference("admins")
        currentUser = FirebaseAuth.getInstance().currentUser

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            adminDatabase.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // The user is an admin
                        Log.d("AdminStatus", "User is an admin")
                        startActivity<MainAdminActivity>()
                    } else {
                        // The user is not an admin
                        Log.d("AdminStatus", "User is not an admin")
                        startActivity<MainActivity>()
                    }
                    finish() // Menutup SplashActivity
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error here
                    // Misalnya, jika terjadi kesalahan saat mengakses database
                }
            })
        } else {
            // Jika tidak ada pengguna yang masuk, langsung pindahkan ke LoginActivity
            startActivity<LoginActivity>()
            finish() // Menutup SplashActivity
        }
        //delayAndGoToLogin()
    }

}

//    private fun delayAndGoToLogin() {
//        Handler(Looper.getMainLooper())
//            .postDelayed({
//                startActivity<LoginActivity>()
//                finish()
//            }, 3000)
//    }
