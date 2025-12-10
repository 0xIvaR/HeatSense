package com.heatsense.app.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.heatsense.app.R

class TemperatureChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {

    private val cpuEntries = ArrayList<Entry>()
    private val batteryEntries = ArrayList<Entry>()
    
    private val maxDataPoints = 300
    private var currentTimeIndex = 0f
    
    private lateinit var cpuDataSet: LineDataSet
    private lateinit var batteryDataSet: LineDataSet
    
    init {
        setupChart()
    }
    
    private fun setupChart() {
        description.isEnabled = false
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        setDrawGridBackground(false)
        setBackgroundColor(Color.TRANSPARENT)
        
        // X Axis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.parseColor("#B0B0B0")
            textSize = 10f
            setDrawGridLines(true)
            gridColor = Color.parseColor("#20FFFFFF")
            setDrawAxisLine(true)
            axisLineColor = Color.parseColor("#808080")
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val seconds = value.toInt()
                    val minutes = seconds / 60
                    val secs = seconds % 60
                    return String.format("%d:%02d", minutes, secs)
                }
            }
            setLabelCount(6, true)
        }
        
        // Y Axis
        axisLeft.apply {
            textColor = Color.parseColor("#B0B0B0")
            textSize = 10f
            setDrawGridLines(true)
            gridColor = Color.parseColor("#20FFFFFF")
            setDrawAxisLine(true)
            axisLineColor = Color.parseColor("#808080")
            axisMinimum = 20f
            axisMaximum = 100f
            setLabelCount(5, false)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}°"
                }
            }
        }
        
        axisRight.isEnabled = false
        
        // Legend
        legend.apply {
            isEnabled = true
            textColor = Color.parseColor("#E0E0E0")
            textSize = 11f
        }
        
        setupDataSets()
    }
    
    private fun setupDataSets() {
        val cpuColor = ContextCompat.getColor(context, R.color.cpu_cyan)
        val batteryColor = ContextCompat.getColor(context, R.color.battery_amber)
        
        cpuDataSet = LineDataSet(cpuEntries, "CPU").apply {
            color = cpuColor
            lineWidth = 2.5f
            setDrawCircles(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.25f // Smoother curves
            setDrawFilled(true)
            fillColor = cpuColor
            fillAlpha = 50
            isHighlightEnabled = false
        }
        
        batteryDataSet = LineDataSet(batteryEntries, "BATTERY").apply {
            color = batteryColor
            lineWidth = 2.5f
            setDrawCircles(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.25f // Smoother curves
            setDrawFilled(true)
            fillColor = batteryColor
            fillAlpha = 50
            isHighlightEnabled = false
        }
        
        data = LineData(cpuDataSet, batteryDataSet)
    }
    
    fun addDataPoint(cpuTemp: Float, batteryTemp: Float) {
        if (cpuTemp.isNaN() || batteryTemp.isNaN()) return
        
        cpuEntries.add(Entry(currentTimeIndex, cpuTemp))
        batteryEntries.add(Entry(currentTimeIndex, batteryTemp))
        
        if (cpuEntries.size > maxDataPoints) {
            cpuEntries.removeAt(0)
            batteryEntries.removeAt(0)
        }
        
        currentTimeIndex += 1f
        
        cpuDataSet.notifyDataSetChanged()
        batteryDataSet.notifyDataSetChanged()
        data?.notifyDataChanged()
        notifyDataSetChanged()
        
        setVisibleXRangeMaximum(300f)
        moveViewToX(currentTimeIndex)
        
        invalidate()
    }
    
    fun clearData() {
        cpuEntries.clear()
        batteryEntries.clear()
        currentTimeIndex = 0f
        
        cpuDataSet.notifyDataSetChanged()
        batteryDataSet.notifyDataSetChanged()
        data?.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }
    
    fun getDataPointCount(): Int = cpuEntries.size
}
