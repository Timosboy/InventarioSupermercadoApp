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

class ReporteCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val conexion: AdminSQL = AdminSQL(this, "universidad.db")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reporte_cliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listaClientes: ListView = findViewById(R.id.lvClientes)
        val listaServicios : ListView = findViewById(R.id.lvServicios)
        val instanciaBD: SQLiteDatabase = conexion.readableDatabase
        val resumen: TextView = findViewById(R.id.tvResumen2)
        val consulta1 = "SELECT Nombre_Completo FROM Cliente"
        val cursor = instanciaBD.rawQuery(consulta1, null)
        val nombresClientes = mutableListOf<String>()
        val gogatras: TextView = findViewById(R.id.tvGogoatras)

        gogatras.setOnClickListener {
            val iratras = Intent(this, VerReportes::class.java)
            startActivity(iratras)
        }

        if (cursor.moveToFirst()) {
            do {
                val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("Nombre_Completo"))
                nombresClientes.add(nombreCompleto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        instanciaBD.close()
        val z = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresClientes)
        listaClientes.adapter= z

        listaClientes.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = parent.getItemAtPosition(position) as String

            Log.d("Mantenimiento", "Cliente seleccionado: $nombreSeleccionado")

            val instanciaBD2: SQLiteDatabase = conexion.readableDatabase
            val consulta2 = """
                SELECT Id_Servicio, Tipodeservicio 
                FROM Servicios 
                WHERE Cliente_Id_Cliente = (
                    SELECT CI 
                    FROM Cliente 
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
                Log.d("Mantenimiento", "No se encontraron servicios para el cliente seleccionado.")
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
                val tipoServicioSeleccionado = parent.getItemAtPosition(position).toString()
                Toast.makeText(this, "Seleccionaste: $tipoServicioSeleccionado", Toast.LENGTH_SHORT).show()

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