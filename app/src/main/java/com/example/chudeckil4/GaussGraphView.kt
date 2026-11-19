package com.example.chudeckil4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GaussGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var resultRe: Double? = null
    private var resultIm: Double? = null

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#1C1B1F".toColorInt()
        strokeWidth = 4f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#1C1B1F".toColorInt()
        textSize = 36f
        isFakeBoldText = true
    }

    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#002B49".toColorInt()
        style = Paint.Style.FILL
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#002B49".toColorInt()
        style = Paint.Style.FILL
    }

    fun setResult(re: Double, im: Double) {
        resultRe = re
        resultIm = im
        visibility = VISIBLE
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if ((w <= 0) || (h <= 0)) return

        val padding = 50f
        val cX = w / 2f
        val cY = h / 2f

        // Draw Axes (Re - Horizontal, Im - Vertical)
        canvas.drawLine(padding, cY, w - padding, cY, axisPaint)
        canvas.drawLine(cX, h - padding, cX, padding, axisPaint)

        // Open Arrowhead for Re (Right) -> symbol >
        val arrowSize = 16f
        canvas.drawLine(w - padding - arrowSize, cY - arrowSize, w - padding, cY, axisPaint)
        canvas.drawLine(w - padding - arrowSize, cY + arrowSize, w - padding, cY, axisPaint)
        canvas.drawText("Re", w - padding - 15f, cY + 40f, labelPaint)

        // Open Arrowhead for Im (Top) -> symbol ^
        canvas.drawLine(cX - arrowSize, padding + arrowSize, cX, padding, axisPaint)
        canvas.drawLine(cX + arrowSize, padding + arrowSize, cX, padding, axisPaint)
        canvas.drawText("Im", cX + 20f, padding + 25f, labelPaint)

        // Mark Origin (0,0) point only (no text label)
        canvas.drawCircle(cX, cY, 8f, centerPaint)

        // Draw Complex Result Point if available (no text label, no line from origin)
        val re = resultRe
        val im = resultIm
        if ((re != null) && (im != null)) {
            val maxVal = max(abs(re), abs(im))
            val range = max(maxVal * 1.3, 5.0)

            val usableWidth = (w / 2f) - padding - 60f
            val usableHeight = (h / 2f) - padding - 60f
            val scale = min(usableWidth, usableHeight) / range.toFloat()

            val pX = cX + (re.toFloat() * scale)
            val pY = cY - (im.toFloat() * scale)

            // Draw Result Point
            canvas.drawCircle(pX, pY, 14f, pointPaint)
        }
    }
}
