package com.example.customprogressbar

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import kotlin.random.Random

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var randomValue = 0
    private val customProgressBarView: CustomProgressBarView

    init{
        LayoutInflater.from(context).inflate(R.layout.custom_progress_bar,this, true)
        customProgressBarView = findViewById(R.id.CustomProgressBarView)

        findViewById<Button>(R.id.RandomValueButton).setOnClickListener{
            randomValue = getRandomTopValue()
            customProgressBarView.setTopValue(randomValue)
        }
    }

    private fun getRandomTopValue(): Int{
        return Random.nextInt(0,100)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putInt(Key.RANDOM_VALUE, randomValue)
            putParcelable(Key.SUPER_STATE, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle){
            randomValue = state.getInt(Key.RANDOM_VALUE)
            customProgressBarView.setTopValue(randomValue)
            super.onRestoreInstanceState(state.getParcelable(Key.SUPER_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

object Key{
    const val RANDOM_VALUE = "randomValue"
    const val SUPER_STATE = "superState"
}
