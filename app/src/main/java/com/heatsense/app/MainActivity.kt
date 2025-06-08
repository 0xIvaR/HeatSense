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
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isMonitoring = false
    
    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateBatteryTemperature(intent)
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
        binding.toggleButton.setOnClickListener {
            if (isMonitoring) {
                stopMonitoring()
            } else {
                startTemperatureMonitoring()
            }
        }
    }
    
    private fun startTemperatureMonitoring() {
        isMonitoring = true
        binding.toggleButton.text = "Stop Monitoring"
        binding.statusText.text = "Monitoring Active"
        
        // Register battery receiver
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
        
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
        binding.toggleButton.text = "Start Monitoring"
        binding.statusText.text = "Monitoring Stopped"
        
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
                val tempCelsius = temperature / 10.0 // Temperature is in tenths of degree Celsius
                binding.batteryTempValue.text = "${tempCelsius.roundToInt()}°C"
                
                // Update battery temp status
                val tempStatus = when {
                    tempCelsius > 40 -> "Hot"
                    tempCelsius > 35 -> "Warm"
                    tempCelsius > 25 -> "Normal"
                    else -> "Cool"
                }
                binding.batteryTempStatus.text = tempStatus
                
                // Change color based on temperature
                val color = when {
                    tempCelsius > 40 -> android.graphics.Color.RED
                    tempCelsius > 35 -> android.graphics.Color.parseColor("#FF8C00")
                    tempCelsius > 25 -> android.graphics.Color.GREEN
                    else -> android.graphics.Color.BLUE
                }
                binding.batteryTempValue.setTextColor(color)
            }
        }
    }
    
    private suspend fun updateCpuTemperature() {
        if (!isMonitoring) return
        
        val cpuTemp = getCpuTemperature()
        
        runOnUiThread {
            if (cpuTemp != -1f) {
                binding.cpuTempValue.text = "${cpuTemp.roundToInt()}°C"
                
                // Update CPU temp status
                val tempStatus = when {
                    cpuTemp > 70 -> "Very Hot"
                    cpuTemp > 60 -> "Hot"
                    cpuTemp > 50 -> "Warm"
                    cpuTemp > 30 -> "Normal"
                    else -> "Cool"
                }
                binding.cpuTempStatus.text = tempStatus
                
                // Change color based on temperature
                val color = when {
                    cpuTemp > 70 -> android.graphics.Color.RED
                    cpuTemp > 60 -> android.graphics.Color.parseColor("#FF4500")
                    cpuTemp > 50 -> android.graphics.Color.parseColor("#FF8C00")
                    cpuTemp > 30 -> android.graphics.Color.GREEN
                    else -> android.graphics.Color.BLUE
                }
                binding.cpuTempValue.setTextColor(color)
            } else {
                binding.cpuTempValue.text = "N/A"
                binding.cpuTempStatus.text = "Unable to read"
            }
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