package com.example.note_task_app.data.utils

import android.app.ActivityManager
import android.content.Context

fun getDeviceMemoryInfo(context: Context): MemoryDetails {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)

    val totalRamBytes = memoryInfo.totalMem
    val availableRamBytes = memoryInfo.availMem
    val isLowMemory = memoryInfo.lowMemory
    val thresholdBytes = memoryInfo.threshold

    // Перевод в мегабайты для удобства
    val totalRamMb = totalRamBytes / (1024 * 1024)
    val availableRamMb = availableRamBytes / (1024 * 1024)
    val thresholdMb = thresholdBytes / (1024 * 1024)

    return MemoryDetails(
        totalRamMb = totalRamMb,
        availableRamMb = availableRamMb,
        isLowMemory = isLowMemory,
        thresholdMb = thresholdMb
    )
}

data class MemoryDetails(
    val totalRamMb: Long,
    val availableRamMb: Long,
    val isLowMemory: Boolean,
    val thresholdMb: Long
)