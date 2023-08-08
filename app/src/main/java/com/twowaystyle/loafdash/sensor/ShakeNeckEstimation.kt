package com.twowaystyle.loafdash.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.stream.IntStream.range
import kotlin.math.abs

class ShakeNeckEstimation(context: Context): SensorEventListener {
    companion object{
        const val LOG_NAME = "ShakeNeckEstimation"
        const val HZ = 50
        const val WINDOW_SIZE = 1
        const val SHAKE_TH = 1.5
        const val SLANT_TH = 1

        const val NON = "non"
        const val HEIGHT = "height"
        const val WIDTH = "width"
        const val SLANT = "slant"

        fun create(context: Context): ShakeNeckEstimation{
            return ShakeNeckEstimation(context).apply { init() }
        }
    }

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    var sensor: Sensor? = null
    var run = false

    private val windowX: ArrayDeque<Float> = ArrayDeque(listOf())
    private val windowY: ArrayDeque<Float> = ArrayDeque(listOf())
    private val windowZ: ArrayDeque<Float> = ArrayDeque(listOf())

    val shakeNeck = MutableLiveData<String>()
    val shakeNeckState = mutableStateOf(NON)

    fun init(){
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    fun start(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        run = true
    }

    fun stop(){
        run = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        windowX.add(x)
        windowY.add(y)
        windowZ.add(z)

        if(windowX.size >= WINDOW_SIZE * HZ) shakeNeckEstimation()
        Log.d(LOG_NAME,"${event.values.toList()}")
    }

    private fun shakeNeckEstimation(){
        val maxX = absMax(windowX)
        val maxY = absMax(windowY)
        val maxZ = absMax(windowZ)
        val maxList = listOf(maxX, maxY, maxZ)

        val estimation: String = when{
            maxList.max() >= SLANT_TH && maxList.max() == maxX
            -> SLANT
            maxList.max() >= SHAKE_TH ->{
                when(maxList.max()){
                    maxY -> HEIGHT
                    maxZ -> WIDTH
                    else -> NON
                }
            }
            else -> NON
        }

        shakeNeck.postValue(estimation)
        shakeNeckState.value = estimation

        windowX.clear()
        windowY.clear()
        windowZ.clear()
    }

    private fun absMax(list: List<Float>): Float = when {
        list.max() >= abs(list.min()) -> list.max()
        else -> abs(list.min())
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {
    }
}