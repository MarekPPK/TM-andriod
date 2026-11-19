package com.example.chudeckil4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val p = findViewById<Button>(R.id.p)
        val e = findViewById<EditText>(R.id.e)
        p.setOnClickListener {
            if(e.text.isEmpty()){
                Toast.makeText(applicationContext, "Wpisz swoje imie", Toast.LENGTH_SHORT).show()}
            else{
                Toast.makeText(applicationContext, "Czesc"+e.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}