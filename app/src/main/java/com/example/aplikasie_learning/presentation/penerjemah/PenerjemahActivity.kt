package com.example.aplikasie_learning.presentation.penerjemah

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.aplikasie_learning.databinding.ActivityPenerjemahBinding
import com.example.aplikasie_learning.databinding.LayoutDialogPenerjemahanBinding
import com.example.aplikasie_learning.presentation.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import org.jetbrains.anko.startActivity
import java.io.*

class PenerjemahActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenerjemahBinding
    private lateinit var editTextLontara : TextInputEditText
    private lateinit var tvOutputLatin : TextView
    private lateinit var tvOutputArti : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenerjemahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextLontara = binding.etLontara
        tvOutputLatin = binding.outputLatin
        tvOutputArti = binding.outputArti


        // init
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnBackTranslate.setOnClickListener {
                finish()
            }

            btnTranslate.setOnClickListener {
                translateLatin()
            }
        }
    }

    fun translateLatin() {
        val startTime = System.currentTimeMillis()

        val text = editTextLontara.text.toString().trim()
        val words = text.split("\\s+".toRegex())
        val inputStream : InputStream = assets.open("csv/corpus.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val csvHeaders = reader.readLine().split(",")
        var foundMatch = false
        val listOfResults = mutableListOf<String>()

        for (word in words) {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val row = line!!.split(",").toTypedArray()
                val teks = row[csvHeaders.indexOf("aksara_lontara")]
                val replacement = row[csvHeaders.indexOf("aksara_latin")]
                val output = kmpReplace(word, teks, replacement)
                if (replacement == output) {
                    listOfResults.add(output)
                    foundMatch = true
                    break
                }
            }
            inputStream.reset()
        }
        reader.close()

        if (!foundMatch) {
            showAlertDialog("Kata \"$text\" yang anda cari tidak ditemukan " +
                    "\nSilahkan cari kata lain " +
                    "\natau periksa dengan baik kata yang anda masukkan")
        } else {
            val finalSentence = listOfResults.joinToString(" ")
            tvOutputLatin.text = finalSentence
            translateArti(finalSentence)

            Log.d("text", "text yang sudah di split $finalSentence")

            val endTime = System.currentTimeMillis() // Waktu akhir
            val timeTranslateLatin = endTime - startTime // Selisih waktu dalam milidetik
            Log.d("TranslationTime", "Total waktu terjemahan Latin: $timeTranslateLatin ms")
        }
    }

    private fun translateArti(latin: String) {
        val startTime = System.currentTimeMillis() // Waktu awal

        val inputStream : InputStream = assets.open("csv/corpus2.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val csvHeaders = reader.readLine().split(",")
        var foundMatch = false

        val listOfResults = mutableListOf<String>()

        val latinWords = latin.split("\\s+".toRegex())

        for (latinWord in latinWords) {
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val row = line!!.split(",").toTypedArray()
                val teks = row[csvHeaders.indexOf("aksara_latin")]
                val replacement = row[csvHeaders.indexOf("arti")]
                val output = kmpReplace(latinWord, teks, replacement)

                if (replacement == output) {
                    listOfResults.add(output)
                    foundMatch = true
                    break
                }
            }
            inputStream.reset()
        }
        reader.close()

        if (foundMatch) {
            val finalSentence = listOfResults.joinToString(" ")
            tvOutputArti.text = finalSentence

            val endTime = System.currentTimeMillis() // Waktu akhir
            val timeTranslateArti = endTime - startTime // Selisih waktu dalam milidetik

            Log.d("TranslationTime", "Total waktu terjemahan Arti: $timeTranslateArti ms")
        }

    }

    private fun showAlertDialog(message: String?) {
        val dialogOke = LayoutDialogPenerjemahanBinding.inflate(layoutInflater)
        val dialogView = AlertDialog.Builder(this)
            .setView(dialogOke.root)

        val alertDialog = dialogView.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var tvMessage = dialogOke.tvMessage

        tvMessage.text = message

        dialogOke.btnOke.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun kmpReplace(text: String, pattern: String, replacement: String): String {
        if (pattern.isEmpty() || replacement.isEmpty()) {
            return text
        }

        fun buildKmpTable(pattern: String): IntArray {
            val table = IntArray(pattern.length) {0}
            var i = 0
            var j = 1
            while (j < pattern.length) {
                if (pattern[i] == pattern[j]) {
                    i++
                    table[j] = i
                    j++
                } else {
                    if (i > 0) {
                        i = table[i - 1]
                    } else {
                        table[j] = 0
                        j++
                    }
                }
            }
            Log.d("KMP_TABLE_PATTERN", "pattern : $pattern")
            Log.d("KMP_TABLE_TEXT", "text : $text")
            Log.d("KMP_TABLE", "Table at step $j: ${table.joinToString()}")
            return table
        }

        val kmpTable = buildKmpTable(pattern)
        var i = 0
        var j = 0
        val result = StringBuilder()
        while (i < text.length) {
            if (text[i] == pattern[j]) {
                i++
                j++
                if (j == pattern.length) {
                    result.append(replacement)
                    j = 0
                }
            } else {
                if (j > 0) {
                    j = kmpTable[j - 1]
                } else {
                    result.append(text[i])
                    i++
                }
            }
        }
        return result.toString()
    }
}