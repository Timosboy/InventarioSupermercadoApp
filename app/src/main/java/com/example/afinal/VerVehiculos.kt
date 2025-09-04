package com.example.afinal

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VerVehiculos : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_vehiculos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnVolver: TextView = findViewById(R.id.volverbt)
        btnVolver.setOnClickListener(){
            val iratras = Intent(this, CLIENTESVEHICULOS::class.java)
            startActivity(iratras)
        }
        var dueno: EditText = findViewById(R.id.etDueno)
        var placa: EditText = findViewById(R.id.etPlaca)
        var modelo: EditText = findViewById(R.id.etModelo)
        var anio: EditText = findViewById(R.id.etAnio)
        var marca = findViewById<EditText>(R.id.etMarca)
        var btnRegistrar: Button = findViewById(R.id.btnRegistro)
        btnRegistrar.setOnClickListener(){
            if (dueno.text.isEmpty() || placa.text.isEmpty() || modelo.text.isEmpty() || anio.text.isEmpty() || marca.text.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val cidueno = dueno.text.toString()
                val numerodeplaca = placa.text.toString()
                val model = modelo.text.toString()
                val year = anio.text.toString()
                val mark = marca.text.toString()
                val instanciaBD: SQLiteDatabase = conexion.readableDatabase
                val consulta1 = "SELECT CI FROM Cliente WHERE CI = ?"
                val cursor: Cursor = instanciaBD.rawQuery(consulta1, arrayOf(cidueno))

                if (cursor.moveToFirst()) {
                    val consulta = "INSERT INTO Vehiculo (Placa, Modelo, Marca, Año, Cliente_Id_Cliente) VALUES ('$numerodeplaca', '$model', '$mark', '$year', $cidueno)"
                    val instanciaBD2: SQLiteDatabase = conexion.writableDatabase
                    instanciaBD2.execSQL(consulta)
                    instanciaBD2.close()
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
                    dueno.text.clear()
                    placa.text.clear()
                    modelo.text.clear()
                    anio.text.clear()
                    marca.text.clear()
                    dueno.requestFocus()
                } else {
                    Toast.makeText(this, "El dueño ingresado no está registrado", Toast.LENGTH_SHORT).show()
                    dueno.text.clear()
                    placa.text.clear()
                    modelo.text.clear()
                    anio.text.clear()
                    marca.text.clear()
                }
                cursor.close()
                instanciaBD.close()
            }
        }

    }
}