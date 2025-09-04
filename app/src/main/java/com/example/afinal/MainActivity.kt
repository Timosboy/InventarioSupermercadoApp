package com.example.afinal

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val conexion: AdminSQL = AdminSQL(this, "universidad.db")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val tvLogin: TextView = findViewById(R.id.tvLogear)
        val btnIngresar: Button = findViewById(R.id.btIngresar)
        val btnCrearUsuario: Button = findViewById(R.id.btCrear)
        val etNombre: EditText = findViewById(R.id.etNombre)
        val etUsuario: EditText = findViewById(R.id.etUsuario)
        val etContrasena: EditText = findViewById(R.id.etContraseña)
        val tvCrear: TextView = findViewById(R.id.tvCrear)
        var tittle: TextView = findViewById(R.id.tvTITULOC)
        tvLogin.visibility = View.GONE
        btnCrearUsuario.visibility = View.GONE
        etNombre.visibility = View.GONE
        tittle.text = "INICIAR SESIÓN"
        tvLogin.setOnClickListener {
            tittle.text = "INICIAR SESIÓN"
            etNombre.visibility = View.GONE
            btnIngresar.visibility = View.VISIBLE
            btnCrearUsuario.visibility = View.GONE
            tvCrear.visibility = View.VISIBLE
            tvLogin.visibility = View.GONE
        }

        btnIngresar.setOnClickListener {

            if (etUsuario.text.isEmpty() || etContrasena.text.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = etUsuario.text.toString()
                val instanciaBD: SQLiteDatabase = conexion.readableDatabase
                val consulta = "SELECT Nombre_Usuario FROM Empleado WHERE Nombre_Usuario = ?"
                val cursor: Cursor = instanciaBD.rawQuery(consulta, arrayOf(usuario))
                if (cursor.moveToFirst()) {
                    var almacen = getSharedPreferences("registro", MODE_PRIVATE)
                    var editor = almacen.edit()
                    editor.putString("usuario", usuario)
                    editor.apply()

                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    etUsuario.text.clear()
                    etContrasena.text.clear()
                    etUsuario.requestFocus()
                    val intencion = Intent(this, CLIENTESVEHICULOS::class.java)
                    startActivity(intencion)

                } else {
                    Toast.makeText(this, "El usuario ingresado no está registrado", Toast.LENGTH_SHORT).show()
                    etUsuario.text.clear()
                    etContrasena.text.clear()
                    etUsuario.requestFocus()
                }
                cursor.close()
                instanciaBD.close()
            }
        }

        tvCrear.setOnClickListener {
            tittle.text = "CREAR USUARIO"
            etNombre.visibility = View.VISIBLE
            btnIngresar.visibility = View.GONE
            btnCrearUsuario.visibility = View.VISIBLE
            tvCrear.visibility = View.GONE
            tvLogin.visibility = View.VISIBLE
        }

        btnCrearUsuario.setOnClickListener {

            if (etUsuario.text.isEmpty() || etContrasena.text.isEmpty() || etNombre.text.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = etUsuario.text.toString()
                val instanciaBD: SQLiteDatabase = conexion.readableDatabase
                val consulta = "SELECT Nombre_Usuario FROM Empleado WHERE Nombre_Usuario = ?"
                val cursor: Cursor = instanciaBD.rawQuery(consulta, arrayOf(usuario))
                if (cursor.moveToFirst()) {
                    Toast.makeText(this, "El usuario ingresado ya está registrado", Toast.LENGTH_SHORT).show()
                    etUsuario.text.clear()
                    etNombre.text.clear()
                    etContrasena.text.clear()
                    etUsuario.requestFocus()
                } else {
                    val usuario = etUsuario.text.toString()
                    val nombreCompleto = etNombre.text.toString()
                    val contrasena = etContrasena.text.toString()
                    val consulta =
                        "INSERT INTO Empleado (Nombre_Usuario, Nombre_Completo, Contraseña) VALUES ('$usuario', '$nombreCompleto', '$contrasena')"
                    val instanciaBD: SQLiteDatabase = conexion.writableDatabase
                    instanciaBD.execSQL(consulta)
                    instanciaBD.close()
                    Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
                    etNombre.text.clear()
                    etContrasena.text.clear()
                    etUsuario.text.clear()
                    etUsuario.requestFocus()
                }
            }
        }
    }
}
