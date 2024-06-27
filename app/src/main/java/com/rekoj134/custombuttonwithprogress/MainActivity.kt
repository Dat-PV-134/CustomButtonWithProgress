package com.rekoj134.custombuttonwithprogress

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rekoj134.circlebuttonwithprogress.CustomButtonWithProgress

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            findViewById<CustomButtonWithProgress>(R.id.customButtonWithProgress).nextStep()
        }

        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            findViewById<CustomButtonWithProgress>(R.id.customButtonWithProgress).previousStep()
        }
    }
}