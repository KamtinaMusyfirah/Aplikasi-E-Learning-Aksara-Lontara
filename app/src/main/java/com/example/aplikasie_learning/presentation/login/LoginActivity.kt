package com.example.aplikasie_learning.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.databinding.ActivityLoginBinding
import com.example.aplikasie_learning.presentation.forgotpassword.ForgotPasswordActivity
import com.example.aplikasie_learning.presentation.main.MainActivity
import com.example.aplikasie_learning.presentation.register.RegisterActivity
import com.example.aplikasie_learning.utils.hideSoftKeyboard
import com.example.aplikasie_learning.utils.showDialogError
import com.example.aplikasie_learning.utils.showDialogLoading
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialogLoading : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // Init
        firebaseAuth = FirebaseAuth.getInstance()
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun onAction() {
        loginBinding.apply {
            btnLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val pass = etPasswordLogin.text.toString().trim()

                if (checkValidation(email, pass))
                {
                    hideSoftKeyboard(this@LoginActivity, loginBinding.root)
                    loginToServer(email, pass)
                }
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        dialogLoading.show()
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                dialogLoading.dismiss()
                startActivity<MainActivity>()
                finishAffinity()
            }
            .addOnFailureListener {
                dialogLoading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        loginBinding.apply {
            when{
                email.isEmpty() -> {
                    etEmailLogin.error = getString(R.string.please_field_your_email)
                    etEmailLogin.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailLogin.error = getString(R.string.please_use_valid_email)
                    etEmailLogin.requestFocus()
                }
                pass.isEmpty() -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_password)
                    etPasswordLogin.requestFocus()
                }
                pass.length < 8 -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_password_more_than_8)
                    etPasswordLogin.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}