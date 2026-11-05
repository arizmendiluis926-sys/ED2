package com.example.ed2

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.abs
import kotlin.math.max

class MagneticActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensorMagnetico: Sensor? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var tvTitulo: TextView
    private lateinit var tvMensaje: TextView
    private lateinit var tvValores: TextView
    private lateinit var ivEstado: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magnetic)

        layout = findViewById(R.id.rootMagnetic)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvMensaje = findViewById(R.id.tvMensaje)
        tvValores = findViewById(R.id.tvValores)
        ivEstado = findViewById(R.id.ivEstado)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorMagnetico = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (sensorMagnetico == null) {
            tvMensaje.text = "Este dispositivo no tiene sensor de campo magnético"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorMagnetico?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Método que evalúa los cambios del sensor y maneja la lógica
    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        tvValores.text = "X: %.1f | Y: %.1f | Z: %.1f (µT)".format(x, y, z)

        // Valor absoluto porque el campo puede ser negativo según la orientación
        val mayor = max(abs(x), max(abs(y), abs(z)))
        when {
            mayor > 100 -> {
                aplicarEstado(
                    fondo = Color.BLACK,
                    mensaje = "Campo magnético fuerte detectado",
                    imagen = R.drawable.ic_iman
                )
            }
            mayor >= 50 -> {
                aplicarEstado(
                    fondo = Color.BLUE,
                    mensaje = "Campo magnético débil",
                    imagen = R.drawable.ic_iman
                )
            }
            else -> {
                aplicarEstado(
                    fondo = Color.rgb(46, 125, 50),
                    mensaje = "Campo magnético normal",
                    imagen = R.drawable.ic_normal
                )
            }
        }
    }

    private fun aplicarEstado(fondo: Int, mensaje: String, imagen: Int) {
        layout.setBackgroundColor(fondo)
        tvTitulo.setTextColor(Color.WHITE)
        tvMensaje.setTextColor(Color.WHITE)
        tvValores.setTextColor(Color.WHITE)
        tvMensaje.text = mensaje
        ivEstado.setImageResource(imagen)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}