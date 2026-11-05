package com.example.ed2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMagnetico = findViewById<Button>(R.id.btnMagnetico)
        val btnGiroscopio = findViewById<Button>(R.id.btnGiroscopio)

        btnMagnetico.setOnClickListener {
            startActivity(Intent(this, MagneticActivity::class.java))
        }

        btnGiroscopio.setOnClickListener {
            startActivity(Intent(this, GyroActivity::class.java))
        }
    }
}