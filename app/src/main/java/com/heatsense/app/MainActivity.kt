package com.heatsense.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.heatsense.app.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.BufferedReader
import kotlin.math.max
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isMonitoring = false
    
    // Peak temperature tracking
    private var peakCpuTemp = 0f
    private var peakBatteryTemp = 0f
    
    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateBatteryTemperature(intent)
            updateBatteryStatus(intent)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        startTemperatureMonitoring()
    }
    
    private fun setupUI() {
        // Initialize gauges
        binding.cpuGauge.setTemperature(0f, false)
        binding.batteryGauge.setTemperature(0f, false)
    }
    
    private fun startTemperatureMonitoring() {
        isMonitoring = true
        
        // Register battery receiver
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
        
        // Reset peak temps
        peakCpuTemp = 0f
        peakBatteryTemp = 0f
        
        // Clear chart data
        binding.temperatureChart.clearData()
        
        // Start CPU temperature monitoring
        lifecycleScope.launch {
            while (isMonitoring) {
                updateCpuTemperature()
                delay(1000) // Update every second
            }
        }
    }
    
    private fun stopMonitoring() {
        isMonitoring = false
        
        try {
            unregisterReceiver(batteryReceiver)
        } catch (e: Exception) {
            // Receiver might not be registered
        }
    }
    
    private fun updateBatteryTemperature(intent: Intent?) {
        if (intent != null && isMonitoring) {
            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            if (temperature != -1) {
                val tempCelsius = temperature / 10.0f // Temperature is in tenths of degree Celsius
                
                // Update battery gauge with animation
                binding.batteryGauge.setTemperature(tempCelsius, true)
                
                // Track peak temperature
                peakBatteryTemp = max(peakBatteryTemp, tempCelsius)
                updatePeakDisplay()
            }
        }
    }
    
    private fun updateBatteryStatus(intent: Intent?) {
        if (intent != null) {
            // Get battery level
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = (level * 100 / scale.toFloat()).toInt()
            
            binding.batteryLevel.text = "$batteryPct%"
            
            // Get charging status
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            
            // Update gauge charging status for lightning bolt
            binding.batteryGauge.setCharging(isCharging)
            
            if (isCharging) {
                binding.chargingStatus.text = "Charging"
                binding.chargingIcon.visibility = android.view.View.VISIBLE
            } else {
                binding.chargingStatus.text = "Discharging"
                binding.chargingIcon.visibility = android.view.View.GONE
            }
        }
    }
    
    private suspend fun updateCpuTemperature() {
        if (!isMonitoring) return
        
        val cpuTemp = getCpuTemperature()
        
        runOnUiThread {
            if (cpuTemp != -1f) {
                // Update CPU gauge with animation
                binding.cpuGauge.setTemperature(cpuTemp, true)
                
                // Track peak temperature
                peakCpuTemp = max(peakCpuTemp, cpuTemp)
                updatePeakDisplay()
                
                // Get current battery temp for chart
                val batteryTemp = getCurrentBatteryTemp()
                
                // Add data point to chart
                binding.temperatureChart.addDataPoint(cpuTemp, batteryTemp)
            } else {
                binding.cpuGauge.setTemperature(0f, false)
            }
        }
    }
    
    private fun getCurrentBatteryTemp(): Float {
        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val temperature = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1
        return if (temperature != -1) temperature / 10.0f else 0f
    }
    
    private fun updatePeakDisplay() {
        val overallPeak = max(peakCpuTemp, peakBatteryTemp)
        if (overallPeak > 0) {
            binding.peakTemp.text = "${overallPeak.roundToInt()}°C"
        }
    }
    
    private fun getCpuTemperature(): Float {
        val thermalPaths = listOf(
            "/sys/class/thermal/thermal_zone0/temp",
            "/sys/class/thermal/thermal_zone1/temp",
            "/sys/class/thermal/thermal_zone2/temp",
            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
            "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
            "/sys/class/hwmon/hwmon0/temp1_input",
            "/sys/class/hwmon/hwmon1/temp1_input",
            "/sys/class/hwmon/hwmon2/temp1_input",
            "/proc/hisi/sensor_temp",
            "/sys/devices/virtual/thermal/thermal_zone0/temp",
            "/sys/devices/virtual/thermal/thermal_zone1/temp"
        )
        
        var maxTemp = -1f
        
        for (path in thermalPaths) {
            try {
                val file = File(path)
                if (file.exists() && file.canRead()) {
                    val reader = BufferedReader(FileReader(file))
                    val tempStr = reader.readLine()
                    reader.close()
                    
                    if (tempStr != null && tempStr.isNotEmpty()) {
                        val temp = tempStr.trim().toFloatOrNull()
                        if (temp != null) {
                            // Most thermal zones report in millidegrees, convert to Celsius
                            val tempCelsius = if (temp > 1000) temp / 1000f else temp
                            if (tempCelsius > maxTemp && tempCelsius < 150) { // Reasonable range
                                maxTemp = tempCelsius
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Continue to next path if this one fails
                continue
            }
        }
        
        return maxTemp
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        handler.removeCallbacksAndMessages(null)
    }
    
    override fun onPause() {
        super.onPause()
        // Keep monitoring in background but reduce frequency
    }
    
    override fun onResume() {
        super.onResume()
        // Resume normal monitoring frequency
        if (!isMonitoring) {
            startTemperatureMonitoring()
        }
    }
} 