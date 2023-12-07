package com.example.aplikasie_learning.presentation.allmateri.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.aplikasie_learning.R
import com.example.aplikasie_learning.databinding.ActivityChangePasswordBinding
import com.example.aplikasie_learning.utils.hideSoftKeyboard
import com.example.aplikasie_learning.utils.showDialogError
import com.example.aplikasie_learning.utils.showDialogLoading
import com.example.aplikasie_learning.utils.showDialogSuccess
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private lateinit var dialogLoading: AlertDialog
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        // Init
        currentUser = FirebaseAuth.getInstance().currentUser
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    private fun onAction() {
        changePasswordBinding.apply {
            btnCloseChangePassword.setOnClickListener {
                finish()
            }

            btnChangePassword.setOnClickListener {
                val oldPass = etOldPasswordChangePassword.text.toString().trim()
                val newPass = etNewPasswordChangePassword.text.toString().trim()
                val confirmNewPass = etConfirmNewPasswordChangePassword.text.toString().trim()

                if (checkValidation(oldPass, newPass, confirmNewPass)){
                    hideSoftKeyboard(this@ChangePasswordActivity, changePasswordBinding.root)
                    changePasswordToServe(oldPass, newPass)
                }
            }
        }
    }

    private fun changePasswordToServe(oldPass: String, newPass: String) {
        dialogLoading.show()
        currentUser?.let { nCurrentUser ->
            val credential = EmailAuthProvider.getCredential(nCurrentUser.email.toString(), oldPass)

            nCurrentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    nCurrentUser.updatePassword(newPass)
                        .addOnSuccessListener {
                            dialogLoading.dismiss()
                            val dialogSuccess = showDialogSuccess(this, getString(R.string.success_change_pass))
                            dialogSuccess.show()

                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    dialogSuccess.dismiss()
                                    finish()
                                }, 3000)
                        }
                        .addOnFailureListener {
                            dialogLoading.dismiss()
                            showDialogError(this@ChangePasswordActivity, it.message.toString())
                        }
                }
                .addOnFailureListener {
                    dialogLoading.dismiss()
                    showDialogError(this@ChangePasswordActivity, it.message.toString())
                }
        }
    }

    private fun checkValidation(oldPass: String, newPass: String, confirmNewPass: String): Boolean {
        changePasswordBinding.apply {
            when{
                oldPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_old_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                oldPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass.isEmpty() -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_new_password)
                    etNewPasswordChangePassword.requestFocus()
                }
                newPass.length < 8 -> {
                    etNewPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etNewPasswordChangePassword.requestFocus()
                }
                confirmNewPass.isEmpty() -> {
                    etConfirmNewPasswordChangePassword.error = getString(R.string.please_field_your_confirm_new_password)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                confirmNewPass.length < 8 -> {
                    etConfirmNewPasswordChangePassword.error = getString(R.string.please_field_your_password_more_than_8)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                newPass != confirmNewPass -> {
                    etNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etNewPasswordChangePassword.requestFocus()
                    etConfirmNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}