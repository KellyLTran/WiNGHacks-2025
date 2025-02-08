package com.example.barcodescannerapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class BoundingBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.parseColor("#96FFCA")
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val fillBox = Paint().apply {
        color = Color.parseColor("#99000000")
        style = Paint.Style.FILL
    }

    private val rect = Rect()
    private val leftVerticalRect = Rect()
    private val rightVerticalRect = Rect()
    private val topHorizontalRect = Rect()
    private val bottomHorizontalRect = Rect()

    private val height = Resources.getSystem().displayMetrics.heightPixels
    private val width = Resources.getSystem().displayMetrics.widthPixels

    fun setCoordinates(topLeftX: Int, topLeftY: Int, bottomRightX: Int, bottomRightY: Int) {
        rect.set(topLeftX, topLeftY, bottomRightX, bottomRightY)
        leftVerticalRect.set(0, topLeftY, topLeftX, bottomRightY)
        rightVerticalRect.set(bottomRightX, topLeftY, width, bottomRightY)
        topHorizontalRect.set(0, 0, width, topLeftY)
        bottomHorizontalRect.set(0, bottomRightY, width, height)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(leftVerticalRect, fillBox)
        canvas.drawRect(rightVerticalRect, fillBox)
        canvas.drawRect(topHorizontalRect, fillBox)
        canvas.drawRect(bottomHorizontalRect, fillBox)
        canvas.drawRect(rect, paint)
    }
}
