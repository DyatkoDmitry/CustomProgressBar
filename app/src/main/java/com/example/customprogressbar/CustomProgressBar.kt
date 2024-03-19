package com.example.customprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import kotlin.random.Random

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {



    init{
        LayoutInflater.from(context).inflate(R.layout.custom_progress_bar,this, true)
        val customProgressBarView = findViewById<CustomProgressBarView>(R.id.CustomProgressBarView)
        findViewById<Button>(R.id.RandomValueButton).setOnClickListener {
            val randomValue = getRandomProgress()
            customProgressBarView.setProgressValue(randomValue)
        }
    }

    private fun getRandomProgress(): Int{
        return Random.nextInt(0,100)
    }
}
