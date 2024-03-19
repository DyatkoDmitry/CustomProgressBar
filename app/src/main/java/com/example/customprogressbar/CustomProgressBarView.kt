package com.example.customprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlinx.coroutines.NonCancellable.start
import java.lang.Integer.min

class CustomProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    private var progressValue = 0
    private var intermediateProgress = 0f

    private val valueAnimator = ValueAnimator().apply {
        setDuration(1000)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener(this@CustomProgressBarView)
    }

    private val paintBackCircle = Paint().apply{
        color = ContextCompat.getColor(context, R.color.blue_white)
        style = Paint.Style.STROKE
        strokeWidth = 35f
    }

    private val paintProgressCircle = Paint().apply {
        color = ContextCompat.getColor(context,R.color.blue)
        style = Paint.Style.STROKE
        strokeWidth = 60f
    }

    /*private val paintButton = Paint().apply {
        color = ContextCompat.getColor(context,R.color.blue)
        style = Paint.Style.FILL
    }*/

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.DEFAULT_BOLD
        color = Color.BLACK
        textSize = 70F
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = 600
        val desiredHeight = 600

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

        Log.d("TAG","onDraw")

        val rectF = RectF(40f,40f, 560f,560f)
        //val button = RectF(0f,600f, 600f,800f)

        //val degreeProgress = getDegreeFromProgress(progressValue)

        canvas.drawArc(rectF,270f,360f,false, paintBackCircle )
        canvas.drawArc(rectF,270f,intermediateProgress,false, paintProgressCircle )
        //canvas.drawRect(button, paintButton)



       drawProgressText(canvas)
    }

    private fun getDegreeFromProgress(progress:Int):Float{
        return (360f / 100 ) * progress
    }

    fun setProgressValue(value:Int){
        progressValue = value
        Log.d("TAG","before valueanimator start")
        valueAnimator.setIntValues(0,progressValue)
        valueAnimator.start()
        Log.d("TAG","after valueanimator start")
    }

    private fun drawProgressText(canvas: Canvas){
        val textCenterX = (width/2f) - (textPaint.measureText(progressValue.toString()))/2f
        val textCenterY = (height/2f) + textPaint.textSize/4f

        canvas.drawText(progressValue.toString(),textCenterX,textCenterY, textPaint)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        val animatedValue = valueAnimator.getAnimatedValue() as Int
        intermediateProgress = getDegreeFromProgress(animatedValue)
        invalidate()
    }
}
