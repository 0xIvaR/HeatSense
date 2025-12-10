package com.heatsense.app.views

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout

class GlassmorphicView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val glassPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#30121212") // 30% opacity dark
    }
    
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.parseColor("#40FFFFFF") // 40% opacity white
    }
    
    private val cornerRadius = 48f
    
    init {
        setWillNotDraw(false)
        
        // Apply blur effect if API supports it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val blurEffect = android.graphics.RenderEffect.createBlurEffect(
                    20f, 20f,
                    android.graphics.Shader.TileMode.CLAMP
                )
                setRenderEffect(blurEffect)
            } catch (e: Exception) {
                // Fallback if blur not supported - just use translucent background
            }
        }
    }
    
    override fun onDraw(canvas: Canvas) {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        
        // Draw glass background
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, glassPaint)
        
        // Draw border
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)
        
        super.onDraw(canvas)
    }
}

