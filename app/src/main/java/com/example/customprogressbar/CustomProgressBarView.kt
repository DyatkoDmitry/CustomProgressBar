package com.example.customprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import java.lang.Integer.min

class CustomProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    private var topValue = 0
    private var arcAngle = 0f
    private var animatedValue = 0

    private val valueAnimator = ValueAnimator().apply{
        setDuration(1000)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener(this@CustomProgressBarView)
    }

    private val anglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        color = ContextCompat.getColor(context, R.color.blue)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.widthAngle)
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context,R.color.pale_blue)
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.widthCircle)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.DEFAULT_BOLD
        textSize = resources.getDimension(R.dimen.textSize)
        style = Paint.Style.FILL
    }

    private val rootRectF = getRootRectF()

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

        canvas.drawArc(rootRectF,270f,360f,false, circlePaint )
        canvas.drawArc(rootRectF,270f, arcAngle,false, anglePaint )

        val textCenterX = (width/2f) - (textPaint.measureText(animatedValue.toString()))/2f
        val textCenterY = (height/2f) + textPaint.textSize/4f
        canvas.drawText(animatedValue.toString(),textCenterX, textCenterY, textPaint)
    }

    private fun getAngleFromProgress(progress:Int):Float{
        return (360f / 100 ) * progress
    }

    fun setTopValue(value:Int){
        topValue = value

        valueAnimator.setIntValues(0,topValue)
        valueAnimator.start()
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        animatedValue = valueAnimator.getAnimatedValue() as Int
        arcAngle = getAngleFromProgress(animatedValue)
        invalidate()
    }

    private fun getRootRectF():RectF{
        val pointXLeft = resources.getDimension(R.dimen.pointXLeft)
        val pointYTop = resources.getDimension(R.dimen.pointYTop)
        val pointXRight = resources.getDimension(R.dimen.pointXRight)
        val pointYBottom = resources.getDimension(R.dimen.pointYBottom)
        return RectF(pointXLeft,pointYTop,pointXRight,pointYBottom)
    }
}
