package com.example.chudeckil4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import java.util.Locale
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var etA1: EditText
    private lateinit var etA2: EditText
    private lateinit var etB1: EditText
    private lateinit var etB2: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnMultiply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etA1 = findViewById(R.id.etA1)
        etA2 = findViewById(R.id.etA2)
        etB1 = findViewById(R.id.etB1)
        etB2 = findViewById(R.id.etB2)
        btnAdd = findViewById(R.id.btnAdd)
        btnMultiply = findViewById(R.id.btnMultiply)

        btnAdd.setOnClickListener {
            calculateAndShowResult(Operation.ADDITION)
        }

        btnMultiply.setOnClickListener {
            calculateAndShowResult(Operation.MULTIPLICATION)
        }
    }

    private enum class Operation {
        ADDITION,
        MULTIPLICATION
    }

    private fun parsePart(text: String): Double? {
        val trimmed = text.trim().replace(',', '.')
        if (trimmed.isEmpty()) return 0.0
        return trimmed.toDoubleOrNull()
    }

    private fun calculateAndShowResult(operation: Operation) {
        val aRe = parsePart(etA1.text.toString())
        val aIm = parsePart(etA2.text.toString())
        val bRe = parsePart(etB1.text.toString())
        val bIm = parsePart(etB2.text.toString())

        if (aRe == null || aIm == null || bRe == null || bIm == null) {
            Toast.makeText(applicationContext, "Wprowadź poprawne liczby!", Toast.LENGTH_SHORT).show()
            return
        }

        val (resRe, resIm) = when (operation) {
            Operation.ADDITION -> {
                val re = aRe + bRe
                val im = aIm + bIm
                Pair(re, im)
            }
            Operation.MULTIPLICATION -> {
                val re = (aRe * bRe) - (aIm * bIm)
                val im = (aRe * bIm) + (aIm * bRe)
                Pair(re, im)
            }
        }

        val resultMessage = "Wynik: ${formatComplex(resRe, resIm)}"
        Toast.makeText(applicationContext, resultMessage, Toast.LENGTH_LONG).show()
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            String.format(Locale.US, "%.2f", value)
        }
    }

    private fun formatComplex(re: Double, im: Double): String {
        val reStr = formatNumber(re)
        val absIm = abs(im)
        val absImStr = formatNumber(absIm)

        return if (im >= 0) {
            "$reStr + ${absImStr}i"
        } else {
            "$reStr - ${absImStr}i"
        }
    }
}