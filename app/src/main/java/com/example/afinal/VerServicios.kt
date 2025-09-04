package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VerServicios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_servicios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val volver :TextView = findViewById(R.id.tvVolver)
        volver.setOnClickListener {
            val back = Intent(this, CLIENTESVEHICULOS::class.java)
            startActivity(back)
        }
        var lvServicios: ListView = findViewById(R.id.lvServicios)
        var menu = arrayOf("Mantenimiento", "Reparación", "Diagnóstico")
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, menu)
        lvServicios.adapter = adapter
        lvServicios.setOnItemClickListener { parent, view, position, id ->
            when(position) {
                0 -> {
                    var valor = "Mantenimiento"
                    var irmantenimiento = Intent(this, Mantenimiento::class.java)
                    irmantenimiento.putExtra("variable", valor)
                    startActivity(irmantenimiento)
                }

                1 -> {
                    var valor = "Reparación"
                    var irreparacion = Intent(this, Reparacion::class.java)
                    irreparacion.putExtra("variable", valor)
                    startActivity(irreparacion)
                }

                2 -> {
                    var valor = "Diagnóstico"
                    var irdiagnostico = Intent(this, Diagnostico::class.java)
                    irdiagnostico.putExtra("variable", valor)
                    startActivity(irdiagnostico)
                }
            }
        }

    }
}