package com.example.afinal

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VerClientes : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_clientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var botVolver: Button = findViewById(R.id.botVolver)
        botVolver.setOnClickListener {
            var intencion = Intent(this, CLIENTESVEHICULOS::class.java)
            startActivity(intencion)
        }

        var btnRegistrar: Button = findViewById(R.id.btRegistrar)
        var carnet : EditText = findViewById(R.id.etCarnet)
        var nombre : EditText = findViewById(R.id.etNombreCliente)
        var celular : EditText = findViewById(R.id.etCelular)

        btnRegistrar.setOnClickListener {
            if (carnet.text.isEmpty() || nombre.text.isEmpty() || celular.text.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val name = nombre.text.toString()
                val ci = carnet.text.toString()
                val phone = celular.text.toString()
                val consulta = "INSERT INTO Cliente (Nombre_Completo, CI, Telefono) VALUES ('$name', '$ci', '$phone')"
                val instanciaBD: SQLiteDatabase = conexion.writableDatabase
                instanciaBD.execSQL(consulta)
                instanciaBD.close()
                Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
                nombre.text.clear()
                carnet.text.clear()
                celular.text.clear()
                nombre.requestFocus()
            }
        }
    }
}