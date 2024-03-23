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

    private var topValue = 0
    private var arcCorner = 0f
    private var animatedValue = 0

    private val valueAnimator = ValueAnimator().apply {
        setDuration(1000)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener(this@CustomProgressBarView)
    }

    private val paintBackCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        color = ContextCompat.getColor(context, R.color.blue_white)
        style = Paint.Style.STROKE
        strokeWidth = 35f
    }

    private val paintProgressCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context,R.color.blue)
        style = Paint.Style.STROKE
        strokeWidth = 60f
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.DEFAULT_BOLD
        textSize = 70F
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = resources.getDimension(R.dimen.rootWidth).toInt()
        val desiredHeight = resources.getDimension(R.dimen.rootHeight).toInt()

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

        canvas.drawArc(rectF,270f,360f,false, paintBackCircle )
        canvas.drawArc(rectF,270f,arcCorner,false, paintProgressCircle )

        val textCenterX = (width/2f) - (textPaint.measureText(animatedValue.toString()))/2f
        val textCenterY = (height/2f) + textPaint.textSize/4f

        canvas.drawText(topValue.toString(),textCenterX,textCenterY, textPaint)
    }

    private fun getCornerFromProgress(progress:Int):Float{
        return (360f / 100 ) * progress
    }

    fun setProgressValue(value:Int){
        topValue = value

        valueAnimator.setIntValues(0,topValue)
        valueAnimator.start()
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        animatedValue = valueAnimator.getAnimatedValue() as Int
        arcCorner = getCornerFromProgress(animatedValue)
        invalidate()
    }
}
