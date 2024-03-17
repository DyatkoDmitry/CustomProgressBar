package com.example.customprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.Integer.min

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private var progressValue = 42

    private val paintBackCircle = Paint().apply{
        color = ContextCompat.getColor(context, R.color.blue_white)
        style = Paint.Style.STROKE
        strokeWidth = 30f
    }

    private val paintProgressCircle = Paint().apply {
        color = ContextCompat.getColor(context,R.color.blue)
        style = Paint.Style.STROKE
        strokeWidth = 55f
    }

    private val paintButton = Paint().apply {
        color = ContextCompat.getColor(context,R.color.blue)
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = 600
        val desiredHeight = 800

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rectF = RectF(40f,40f, 560f,560f)
        val button = RectF(0f,600f, 600f,800f)

        val degreeProgress = getDegreeProgress()

        canvas.drawArc(rectF,270f,360f,false, paintBackCircle )
        canvas.drawArc(rectF,270f,degreeProgress,false, paintProgressCircle )
        canvas.drawRect(button, paintButton)
    }

    private fun getDegreeProgress():Float{
        return (360f / 100 ) * progressValue
    }

    fun setProgressValue(value:Int){
        progressValue = value
        invalidate()
    }
}
