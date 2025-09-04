package com.example.afinal

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Mantenimiento : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mantenimiento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cambioA: CheckBox = findViewById(R.id.cbCambioA3)
        val llenadoLR3: CheckBox = findViewById(R.id.cbLlenadoLR3)
        val llenadoLF3: CheckBox = findViewById(R.id.cbLlenadoLF3)
        val cambioF: CheckBox = findViewById(R.id.cbCambioF3)
        val descripcion: EditText = findViewById(R.id.etDescripcion)
        val costo: EditText = findViewById(R.id.etcosto)
        val registro: Button = findViewById(R.id.botonRegistrar)
        val todo: TextView = findViewById(R.id.tvTodo)
        val clientess: Spinner = findViewById(R.id.spCliente3)
        val vehiculoss: ListView = findViewById(R.id.lvVehiculos)
        val resumen: Button = findViewById(R.id.btVolverserv3)

        val i: Intent = intent
        val texto: String? = i.getStringExtra("variable")

        val volver: TextView = findViewById(R.id.tvRegresar)
        volver.setOnClickListener {
            val back = Intent(this, VerServicios::class.java)
            startActivity(back)
        }

        val instanciaBD: SQLiteDatabase = conexion.readableDatabase
        val consulta1 = "SELECT Nombre_Completo FROM Cliente"
        val cursor = instanciaBD.rawQuery(consulta1, null)
        val nombresClientes = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("Nombre_Completo"))
                nombresClientes.add(nombreCompleto)
            } while (cursor.moveToNext())
        } else {
            Log.d("Mantenimiento", "No se encontraron clientes.")
        }

        cursor.close()
        instanciaBD.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresClientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        clientess.adapter = adapter

        clientess.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val nombreSeleccionado = parent.getItemAtPosition(position) as String
                Log.d("Mantenimiento", "Cliente seleccionado: $nombreSeleccionado")

                val instanciaBD2: SQLiteDatabase = conexion.readableDatabase
                val consulta2 = """
                    SELECT Placa 
                    FROM Vehiculo 
                    WHERE Cliente_Id_Cliente = (
                        SELECT CI 
                        FROM Cliente 
                        WHERE Nombre_Completo = ?
                    )
                """
                val cursor2 = instanciaBD2.rawQuery(consulta2, arrayOf(nombreSeleccionado))
                val placasVehiculos = mutableListOf<String>()

                if (cursor2.moveToFirst()) {
                    do {
                        val placa = cursor2.getString(cursor2.getColumnIndexOrThrow("Placa"))
                        placasVehiculos.add(placa)
                    } while (cursor2.moveToNext())
                } else {
                    Log.d("Mantenimiento", "No se encontraron vehículos para el cliente seleccionado.")
                }

                cursor2.close()
                instanciaBD2.close()

                if (placasVehiculos.isEmpty()) {
                    Log.d("Mantenimiento", "No se encontraron vehículos para el cliente seleccionado.")
                } else {
                    Log.d("Mantenimiento", "Vehículos encontrados: ${placasVehiculos.joinToString()}")
                }

                val adapter2 = ArrayAdapter(this@Mantenimiento, android.R.layout.simple_list_item_1, placasVehiculos)
                vehiculoss.adapter = adapter2
                vehiculoss.setOnClickListener{}

                resumen.setOnClickListener {
                    val descripcionTexto = descripcion.text.toString()
                    val costoTexto = costo.text.toString()
                    var costoTotal = costoTexto.toDoubleOrNull() ?: 0.0
                    val serviciosExtras = mutableListOf<String>()

                    if (cambioA.isChecked) {
                        serviciosExtras.add("Cambio de Aceite")
                        costoTotal += 50
                    }
                    if (llenadoLR3.isChecked) {
                        serviciosExtras.add("Llenado de Líquidos de Refrigerante")
                        costoTotal += 25
                    }
                    if (llenadoLF3.isChecked) {
                        serviciosExtras.add("Llenado de Líquidos de Frenos")
                        costoTotal += 20
                    }
                    if (cambioF.isChecked) {
                        serviciosExtras.add("Cambio de Filtro")
                        costoTotal += 30
                    }

                    val serviciosTexto = serviciosExtras.joinToString(", ")
                    val resultadoFinal = "Descripción: $descripcionTexto\nServicios: $serviciosTexto\nCosto Total: $costoTotal"

                    todo.text = resultadoFinal
                }

                registro.setOnClickListener {
                    val descripcionTexto = descripcion.text.toString()
                    val costoTexto = costo.text.toString()
                    var costoTotal = costoTexto.toDoubleOrNull() ?: 0.0
                    val serviciosExtras = mutableListOf<String>()

                    if (cambioA.isChecked) {
                        serviciosExtras.add("Cambio de Aceite")
                        costoTotal += 50
                    }
                    if (llenadoLR3.isChecked) {
                        serviciosExtras.add("Llenado de Líquidos de Refrigerante")
                        costoTotal += 25
                    }
                    if (llenadoLF3.isChecked) {
                        serviciosExtras.add("Llenado de Líquidos de Frenos")
                        costoTotal += 20
                    }
                    if (cambioF.isChecked) {
                        serviciosExtras.add("Cambio de Filtro")
                        costoTotal += 30
                    }

                    val serviciosTexto = serviciosExtras.joinToString(", ")
                    val resultadoFinal = "Descripción: $descripcionTexto\nServicios: $serviciosTexto"

                    val clienteSeleccionado = clientess.selectedItem as String
                    val empleadoId = getSharedPreferences("registro", MODE_PRIVATE).getString("usuario", "")
                    val placa = vehiculoss.selectedItem as String


                    val instanciaBD: SQLiteDatabase = conexion.writableDatabase
                    val insertQuery = """
                        INSERT INTO Servicios (Tipodeservicio, Descripción, Costo, Fecha, Cliente_Id_Cliente, Empleado_Id_Empleado, Vehiculo_Id_Vehiculo) 
                        VALUES (?, ?, ?, date('now'), (SELECT CI FROM Cliente WHERE Nombre_Completo = ?), ?, ?)
                    """
                    try {
                        instanciaBD.execSQL(insertQuery, arrayOf(texto, resultadoFinal, costoTotal.toString(), clienteSeleccionado, empleadoId, placa))
                        Toast.makeText(this@Mantenimiento, "Registro guardado con éxito", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("Mantenimiento", "Error al insertar servicio: ${e.message}")
                    } finally {
                        instanciaBD.close()
                    }
                    var iratras = Intent(this@Mantenimiento, VerServicios::class.java)
                    startActivity(iratras)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                TODO("Not yet implemented")
            }
        }
    }
}
