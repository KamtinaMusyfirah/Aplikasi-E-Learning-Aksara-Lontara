package com.example.aplikasie_learning.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasie_learning.databinding.LayoutDialogErrorBinding
import com.example.aplikasie_learning.databinding.LayoutDialogLoadingBinding
import com.example.aplikasie_learning.databinding.LayoutDialogSuccessBinding
import com.example.aplikasie_learning.presentation.allmateri.changepassword.ChangePasswordActivity

fun View.visiable(){visibility = View.VISIBLE}
fun View.gone(){visibility = View.GONE}
fun View.invisible(){visibility = View.INVISIBLE}
fun View.enabled(){ isEnabled = true}
fun View.disabled(){ isEnabled = false}

fun showDialogLoading(context: Context): AlertDialog{
    val binding = LayoutDialogLoadingBinding.inflate(LayoutInflater.from(context))
    return AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(false)
        .create()
}

fun showDialogError(context: Context, message: String){
    val binding = LayoutDialogErrorBinding.inflate(LayoutInflater.from(context))
    binding.tvMessage.text = message

    return AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(true)
        .create()
        .show()
}

fun showDialogSuccess(context: Context, message: String): AlertDialog{
    val binding = LayoutDialogSuccessBinding.inflate(LayoutInflater.from(context))
    binding.tvMessage.text = message

    return AlertDialog
        .Builder(context)
        .setView(binding.root)
        .setCancelable(true)
        .create()
}
fun hideSoftKeyboard(context: Context, view: View){
    val inn = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inn.hideSoftInputFromWindow(view.windowToken, 8)
}