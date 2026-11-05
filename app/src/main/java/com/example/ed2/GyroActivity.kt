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

class GyroActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var giroscopio: Sensor? = null

    private lateinit var layout: ConstraintLayout
    private lateinit var tvTitulo: TextView
    private lateinit var tvMensaje: TextView
    private lateinit var tvValores: TextView
    private lateinit var ivGiro: ImageView

    // Umbral que pide la actividad: valor de 2 en el eje
    private val umbral = 2f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyro)

        layout = findViewById(R.id.rootGyro)
        tvTitulo = findViewById(R.id.tvTituloGiro)
        tvMensaje = findViewById(R.id.tvMensajeGiro)
        tvValores = findViewById(R.id.tvValoresGiro)
        ivGiro = findViewById(R.id.ivGiro)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (giroscopio == null) {
            tvMensaje.text = "Este dispositivo no tiene giroscopio"
        }
    }

    override fun onResume() {
        super.onResume()
        giroscopio?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
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

        tvValores.text = "X: %.2f | Y: %.2f | Z: %.2f (rad/s)".format(x, y, z)

        val ax = abs(x)
        val ay = abs(y)
        val az = abs(z)

        when {
            // Si varios ejes superan el umbral a la vez, gana el de mayor giro
            ax >= umbral && ax >= ay && ax >= az -> {
                aplicarEstado(
                    fondo = Color.rgb(46, 125, 50),   // VERDE
                    mensaje = "Giro fuerte en el eje X",
                    imagen = R.drawable.ic_giro_x,
                    textoBlanco = true
                )
            }
            ay >= umbral && ay >= ax && ay >= az -> {
                aplicarEstado(
                    fondo = Color.rgb(255, 152, 0),   // NARANJA
                    mensaje = "Giro fuerte en el eje Y",
                    imagen = R.drawable.ic_giro_y,
                    textoBlanco = true
                )
            }
            az >= umbral && az >= ax && az >= ay -> {
                aplicarEstado(
                    fondo = Color.BLUE,               // AZUL
                    mensaje = "Giro fuerte en el eje Z",
                    imagen = R.drawable.ic_giro_z,
                    textoBlanco = true
                )
            }
            else -> {
                aplicarEstado(
                    fondo = Color.WHITE,              // BLANCO
                    mensaje = "Ningún movimiento detectado",
                    imagen = R.drawable.ic_giro_none,
                    textoBlanco = false
                )
            }
        }
    }

    private fun aplicarEstado(fondo: Int, mensaje: String, imagen: Int, textoBlanco: Boolean) {
        val colorTexto = if (textoBlanco) Color.WHITE else Color.BLACK

        layout.setBackgroundColor(fondo)
        tvTitulo.setTextColor(colorTexto)
        tvMensaje.setTextColor(colorTexto)
        tvValores.setTextColor(colorTexto)
        tvMensaje.text = mensaje
        ivGiro.setImageResource(imagen)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}