package com.example.afinal

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReporteEmpleado : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reporte_empleado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listaEmplados: ListView = findViewById(R.id.lvEmpleados)
        val listaServicios : ListView = findViewById(R.id.lvServicios)
        val instanciaBD: SQLiteDatabase = conexion.readableDatabase
        val resumen: TextView = findViewById(R.id.tvResumen)
        val consulta1 = "SELECT Nombre_Completo FROM Empleado"
        val cursor = instanciaBD.rawQuery(consulta1, null)
        val nombreEmpleado = mutableListOf<String>()
        val regreat: TextView = findViewById(R.id.tvGoBack)

        regreat.setOnClickListener {
            val iratras = Intent(this, VerReportes::class.java)
            startActivity(iratras)
        }
        if (cursor.moveToFirst()) {
            do {
                val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("Nombre_Completo"))
                nombreEmpleado.add(nombreCompleto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        instanciaBD.close()
        val z = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombreEmpleado)
        listaEmplados.adapter= z

        listaEmplados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Seleccionaste: $position", Toast.LENGTH_SHORT).show()

            Log.d("Mantenimiento", "Cliente seleccionado: $nombreSeleccionado")

            val instanciaBD2: SQLiteDatabase = conexion.readableDatabase
            val consulta2 = """
                SELECT Id_Servicio, Tipodeservicio 
                FROM Servicios 
                WHERE Empleado_Id_Empleado = (
                    SELECT Nombre_Usuario 
                    FROM Empleado 
                    WHERE Nombre_Completo = ?
                )
            """
            val cursor2 = instanciaBD2.rawQuery(consulta2, arrayOf(nombreSeleccionado))
            val servicios = mutableListOf<Pair<Int, String>>()

            if (cursor2.moveToFirst()) {
                do {
                    val idServicio = cursor2.getInt(cursor2.getColumnIndexOrThrow("Id_Servicio"))
                    val tipoServicio = cursor2.getString(cursor2.getColumnIndexOrThrow("Tipodeservicio"))
                    servicios.add(Pair(idServicio, tipoServicio))
                } while (cursor2.moveToNext())
            } else {
                Log.d("Mantenimiento", "No se encontraron servicios para el empleado seleccionado.")
                Toast.makeText(this, "No se encontraron servicios para el empleado seleccionado.", Toast.LENGTH_SHORT).show()
            }

            cursor2.close()
            instanciaBD2.close()

            if (servicios.isEmpty()) {
                Log.d("Mantenimiento", "No se encontraron servicios para el cliente seleccionado.")
            } else {
                val serviciosNombres = servicios.map { it.second }
                Log.d("Mantenimiento", "Servicios encontrados: ${serviciosNombres.joinToString()}")
            }

            val adapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_1, servicios.map { it.second })
            listaServicios.adapter = adapter2
            listaServicios.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val idServicioSeleccionado = servicios[position].first

                val instanciaBD3: SQLiteDatabase = conexion.readableDatabase
                val consulta3 = """
                    SELECT Descripción, Costo
                    FROM Servicios
                    WHERE Id_Servicio = ?
                """
                val cursor3 = instanciaBD3.rawQuery(consulta3, arrayOf(idServicioSeleccionado.toString()))

                var descripcion = ""
                var costo = 0
                if (cursor3.moveToFirst()) {
                    descripcion = cursor3.getString(cursor3.getColumnIndexOrThrow("Descripción"))
                    costo = cursor3.getInt(cursor3.getColumnIndexOrThrow("Costo"))
                } else {
                    Log.d("Mantenimiento", "No se encontraron detalles para el servicio seleccionado.")
                }

                cursor3.close()
                instanciaBD3.close()

                resumen.text = descripcion
                resumen.append("\nCosto: $costo")
            }
        }

    }
}