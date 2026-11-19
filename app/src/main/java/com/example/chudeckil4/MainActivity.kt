package com.example.chudeckil4

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
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
    private lateinit var spinnerOperation: AlwaysSelectSpinner
    private lateinit var gaussGraphView: GaussGraphView

    private var currentToast: Toast? = null
    private var isFirstSpinnerCall = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etA1 = findViewById(R.id.etA1)
        etA2 = findViewById(R.id.etA2)
        etB1 = findViewById(R.id.etB1)
        etB2 = findViewById(R.id.etB2)
        btnAdd = findViewById(R.id.btnAdd)
        btnMultiply = findViewById(R.id.btnMultiply)
        spinnerOperation = findViewById(R.id.spinnerOperation)
        gaussGraphView = findViewById(R.id.gaussGraphView)

        btnAdd.setOnClickListener {
            performCalculation(Operation.ADDITION)
        }

        btnMultiply.setOnClickListener {
            performCalculation(Operation.MULTIPLICATION)
        }

        spinnerOperation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSpinnerCall) {
                    isFirstSpinnerCall = false
                    return
                }
                val operation = if (position == 1) Operation.MULTIPLICATION else Operation.ADDITION
                performCalculation(operation)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun isAnyFieldEntered(): Boolean {
        return etA1.text.toString().trim().isNotEmpty() ||
               etA2.text.toString().trim().isNotEmpty() ||
               etB1.text.toString().trim().isNotEmpty() ||
               etB2.text.toString().trim().isNotEmpty()
    }

    private fun performCalculation(operation: Operation) {
        if (!isAnyFieldEntered()) {
            currentToast?.cancel()
            currentToast = Toast.makeText(applicationContext, "Wprowadź liczby przed wykonaniem operacji!", Toast.LENGTH_SHORT)
            currentToast?.show()
            return
        }

        calculateAndShowResult(operation)
    }

    private enum class Operation {
        ADDITION,
        MULTIPLICATION
    }

    private fun parsePart(text: String): Double? {
        val trimmed = text.trim().replace(',', '.')
        if (trimmed.isEmpty() || trimmed == "-" || trimmed == "+" || trimmed == "." || trimmed == "-.") return 0.0
        return trimmed.toDoubleOrNull()
    }

    private fun calculateAndShowResult(operation: Operation) {
        val aRe = parsePart(etA1.text.toString())
        val aIm = parsePart(etA2.text.toString())
        val bRe = parsePart(etB1.text.toString())
        val bIm = parsePart(etB2.text.toString())

        if (aRe == null || aIm == null || bRe == null || bIm == null) {
            currentToast?.cancel()
            currentToast = Toast.makeText(applicationContext, "Wprowadź poprawne liczby!", Toast.LENGTH_SHORT)
            currentToast?.show()
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
        currentToast?.cancel()
        currentToast = Toast.makeText(applicationContext, resultMessage, Toast.LENGTH_SHORT)
        currentToast?.show()

        gaussGraphView.setResult(resRe, resIm)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
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