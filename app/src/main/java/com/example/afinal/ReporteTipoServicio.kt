package com.example.afinal

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReporteTipoServicio : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")
    lateinit var resumen: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reporte_tipo_servicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backk: TextView = findViewById(R.id.tvBackk)
        backk.setOnClickListener {
            val irse : Intent = Intent(this, VerReportes::class.java)
            startActivity(irse)
        }
        resumen= findViewById(R.id.lvFin)
        cargarResumen()
    }
    fun cargarResumen() {
        val instanciaBD: SQLiteDatabase = conexion.readableDatabase
        val consulta = """
            SELECT Tipodeservicio, COUNT(*) AS TotalServicios, SUM(Costo) AS TotalIngresos
            FROM Servicios
            GROUP BY Tipodeservicio
        """
        val cursor: Cursor = instanciaBD.rawQuery(consulta, null)
        val resumenList = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                val tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow("Tipodeservicio"))
                val totalServicios = cursor.getInt(cursor.getColumnIndexOrThrow("TotalServicios"))
                val totalIngresos = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalIngresos"))
                resumenList.add("Tipo de Servicio: $tipoServicio\nTotal Servicios: $totalServicios\nTotal Ingresos: $totalIngresos")
            } while (cursor.moveToNext())
        } else {
            Log.d("ResumenServicios", "No se encontraron datos.")
        }

        cursor.close()
        instanciaBD.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resumenList)
        resumen.adapter = adapter
    }
}