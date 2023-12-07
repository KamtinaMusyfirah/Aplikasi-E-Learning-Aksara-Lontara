package com.example.aplikasie_learning.presentation.penerjemah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.aplikasie_learning.databinding.ActivityPenerjemahBinding
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
                startActivity<MainActivity>()
            }

            btnTranslate.setOnClickListener {
                translateLatin()
            }
        }
    }

    fun translateLatin() {
        val text = editTextLontara.text.toString().trim()
        val inputStream : InputStream = assets.open("csv/corpus.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val csvHeaders = reader.readLine().split(",")

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val row = line!!.split(",").toTypedArray()
            val pattern = row[csvHeaders.indexOf("aksara_lontara")]
            val replacement = row[csvHeaders.indexOf("aksara_latin")]
            val output = kmpReplace(text, pattern, replacement)
            if (replacement == output) {
                tvOutputLatin.text = output
            }
        }
        reader.close()

        translateArti(tvOutputLatin.text.toString())
    }

    private fun translateArti(latin: String) {
        val inputStream : InputStream = assets.open("csv/corpus2.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val csvHeaders = reader.readLine().split(",")

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            val row = line!!.split(",").toTypedArray()
            val pattern = row[csvHeaders.indexOf("aksara_latin")]
            val replacement = row[csvHeaders.indexOf("arti")]
            val output = kmpReplace(latin, pattern, replacement)

            if (replacement == output) {
                tvOutputArti.text = output
            }
        }
        reader.close()
    }

    fun kmpReplace(text: String, pattern: String, replacement: String): String {
        if (pattern.isEmpty() || replacement.isEmpty()) {
            return text
        }

        fun buildKmpTable(pattern: String): IntArray {
            val table = IntArray(pattern.length)
            var i = 0
            var j = 1
            while (j < pattern.length) {
                if (pattern[i] == pattern[j]) {
                    i++
                    table[j] = i
                    j++
                } else if (i > 0) {
                    i = table[i - 1]
                } else {
                    table[j] = 0
                    j++
                }
            }
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