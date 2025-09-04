package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CLIENTESVEHICULOS : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clientesvehiculos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnVolver: Button = findViewById(R.id.btVolver)
        btnVolver.setOnClickListener{
            var atras = Intent(this, MainActivity::class.java)
            startActivity(atras)
        }
        var btnClientes: Button = findViewById(R.id.btClientes)
        btnClientes.setOnClickListener{
            var irclientes = Intent(this, VerClientes::class.java)
            startActivity(irclientes)
        }
        var btnVehiculos: Button = findViewById(R.id.btVehiculo)
        btnVehiculos.setOnClickListener{
            var irvehiculos = Intent(this, VerVehiculos::class.java)
            startActivity(irvehiculos)
        }
        var btnServicios : Button = findViewById(R.id.btServicios)
        btnServicios.setOnClickListener{
            var irservicios = Intent(this, VerServicios::class.java)
            startActivity(irservicios)
        }
        var btnReportes : Button = findViewById(R.id.btReportes)
        btnReportes.setOnClickListener{
            var irreportes = Intent(this, VerReportes::class.java)
            startActivity(irreportes)
        }

    }
}