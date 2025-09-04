package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VerReportes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_reportes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val volveratras:TextView = findViewById(R.id.tvBack)
        volveratras.setOnClickListener {
            val intencion = Intent(this, CLIENTESVEHICULOS::class.java)
            startActivity(intencion)
        }
        val reporteE: Button = findViewById(R.id.btREmpleado)
        val reporteC: Button = findViewById(R.id.btRCliente)
        val reporteTS : Button = findViewById(R.id.btRTipoS)
        reporteE.setOnClickListener(){
            val intencion = Intent(this, ReporteEmpleado::class.java)
            startActivity(intencion)
        }
        reporteC.setOnClickListener(){
            val intencion = Intent(this, ReporteCliente::class.java)
            startActivity(intencion)
        }
        reporteTS.setOnClickListener(){
            val intencion = Intent(this, ReporteTipoServicio::class.java)
            startActivity(intencion)
        }
    }
}