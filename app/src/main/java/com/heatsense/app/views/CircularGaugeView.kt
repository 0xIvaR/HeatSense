package com.heatsense.app.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.heatsense.app.R
import kotlin.math.min

class CircularGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Temperature data
    private var currentTemp = 0f
    private var animatedTemp = 0f
    private var maxTemp = 100f
    private var minTemp = 0f
    
    // Labels
    private var label = "SENSOR"
    private var unit = "°C"
    
    // Colors
    private var primaryColor = Color.parseColor("#00E5FF")
    private var secondaryColor = Color.parseColor("#2979FF")
    
    // Paints
    private val backgroundArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 28f
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#1A1A1A")
    }
    
    private val progressArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 26f
        strokeCap = Paint.Cap.ROUND
    }
    
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 32f
        strokeCap = Paint.Cap.ROUND
    }
    
    private val tempTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 72f
        textAlign = Paint.Align.CENTER
    }
    
    private val labelTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E0E0E0")
        textSize = 22f
        textAlign = Paint.Align.CENTER
        letterSpacing = 0.08f
    }
    
    private var animator: ValueAnimator? = null
    
    enum class GaugeType {
        CPU, BATTERY
    }
    
    private var gaugeType: GaugeType = GaugeType.CPU
    private var isCharging = false
    
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularGaugeView,
            0, 0
        ).apply {
            try {
                val typeValue = getInt(R.styleable.CircularGaugeView_gaugeType, 0)
                gaugeType = if (typeValue == 0) GaugeType.CPU else GaugeType.BATTERY
                label = getString(R.styleable.CircularGaugeView_gaugeLabel) ?: 
                    if (gaugeType == GaugeType.CPU) "CPU CORE" else "BATTERY PACK"
            } finally {
                recycle()
            }
        }
        
        // Apply SF Pro fonts
        try {
            val sfProBold = ResourcesCompat.getFont(context, R.font.sf_pro_bold)
            tempTextPaint.typeface = sfProBold
            
            val sfProMedium = ResourcesCompat.getFont(context, R.font.sf_pro_medium)
            labelTextPaint.typeface = sfProMedium
        } catch (e: Exception) {
            tempTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        
        setupColors()
    }
    
    private fun setupColors() {
        when (gaugeType) {
            GaugeType.CPU -> {
                primaryColor = Color.parseColor("#00E5FF")
                secondaryColor = Color.parseColor("#2979FF")
                maxTemp = 100f
            }
            GaugeType.BATTERY -> {
                primaryColor = Color.parseColor("#FFC400")
                secondaryColor = Color.parseColor("#FF5722")
                maxTemp = 60f
            }
        }
        
        progressArcPaint.color = primaryColor
        glowPaint.color = primaryColor
        glowPaint.alpha = 80
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateGradient()
    }
    
    private fun updateGradient() {
        if (width == 0 || height == 0) return
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        val gradient = SweepGradient(
            centerX,
            centerY,
            intArrayOf(primaryColor, secondaryColor, primaryColor),
            floatArrayOf(0f, 0.5f, 1f)
        )
        
        progressArcPaint.shader = gradient
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (width == 0 || height == 0) return
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f - 50f
        
        if (radius <= 0) return
        
        val rectF = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        
        // Draw background arc
        canvas.drawArc(rectF, 135f, 270f, false, backgroundArcPaint)
        
        // Draw glow (simple)
        if (animatedTemp > 0) {
            val sweepAngle = (animatedTemp / maxTemp) * 270f
            canvas.drawArc(rectF, 135f, sweepAngle, false, glowPaint)
        }
        
        // Draw progress arc
        if (animatedTemp > 0) {
            val sweepAngle = (animatedTemp / maxTemp) * 270f
            canvas.drawArc(rectF, 135f, sweepAngle, false, progressArcPaint)
        }
        
        // Draw temperature text
        val tempText = "${animatedTemp.toInt()}$unit"
        canvas.drawText(tempText, centerX, centerY + 20f, tempTextPaint)
        
        // Draw charging bolt for battery
        if (gaugeType == GaugeType.BATTERY && isCharging) {
            drawBolt(canvas, centerX, centerY + 55f)
        }
        
        // Draw label text
        canvas.drawText(label, centerX, centerY + radius + 50f, labelTextPaint)
    }
    
    private fun drawBolt(canvas: Canvas, cx: Float, cy: Float) {
        val boltPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryColor
            style = Paint.Style.FILL
        }
        
        val path = Path().apply {
            moveTo(cx, cy - 12f)
            lineTo(cx - 5f, cy)
            lineTo(cx + 1f, cy)
            lineTo(cx - 3f, cy + 12f)
            lineTo(cx + 6f, cy - 3f)
            lineTo(cx + 1f, cy - 3f)
            close()
        }
        
        canvas.drawPath(path, boltPaint)
    }
    
    fun setTemperature(temp: Float, animate: Boolean = true) {
        currentTemp = temp.coerceIn(minTemp, maxTemp)
        
        if (animate) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(animatedTemp, currentTemp).apply {
                duration = 600
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    animatedTemp = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            animatedTemp = currentTemp
            invalidate()
        }
        
        updateTextColor()
    }
    
    private fun updateTextColor() {
        val ratio = currentTemp / maxTemp
        tempTextPaint.color = when {
            ratio > 0.85f -> Color.parseColor("#FF3D00")
            ratio > 0.7f -> Color.parseColor("#FF9100")
            else -> Color.WHITE
        }
    }
    
    fun setCharging(charging: Boolean) {
        isCharging = charging
        invalidate()
    }
    
    fun setLabel(newLabel: String) {
        label = newLabel
        invalidate()
    }
    
    fun setGaugeType(type: GaugeType) {
        gaugeType = type
        setupColors()
        updateGradient()
        invalidate()
    }
    
    fun setMaxTemperature(max: Float) {
        maxTemp = max
        invalidate()
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }
}
