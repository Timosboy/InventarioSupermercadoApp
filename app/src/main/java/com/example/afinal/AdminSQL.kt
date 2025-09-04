package com.example.afinal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminSQL(context: Context, nombreBD: String) : SQLiteOpenHelper(context, nombreBD, null, 1) {

    companion object {
        const val CREAR_CLIENTES = """
            CREATE TABLE Cliente (
                CI INTEGER PRIMARY KEY,
                Nombre_Completo VARCHAR(30),
                Telefono VARCHAR(30)
            )
        """
        const val CREAR_EMPLEADOS = """
            CREATE TABLE Empleado (
                Nombre_Usuario VARCHAR(20) PRIMARY KEY,
                Nombre_Completo VARCHAR(30),
                Contraseña TEXT
            )

        """
        const val CREAR_REPORTES = """
            CREATE TABLE Reportes (
                Id_Reporte INTEGER PRIMARY KEY AUTOINCREMENT,
                Empleado_Id_Empleado INTEGER NOT NULL,
                Cliente_Id_Cliente INTEGER NOT NULL,
                Vehiculo_Id_Vehiculo INTEGER NOT NULL,
                Servicios_Id_Servicio INTEGER NOT NULL,
                FOREIGN KEY (Empleado_Id_Empleado) REFERENCES Empleado (Nombre_Usuario),
                FOREIGN KEY (Cliente_Id_Cliente) REFERENCES Cliente (CI),
                FOREIGN KEY (Vehiculo_Id_Vehiculo) REFERENCES Vehiculo (Placa),
                FOREIGN KEY (Servicios_Id_Servicio) REFERENCES Servicios (Id_Servicio)
            )       

        """
        const val CREAR_SERVICIOS = """
            CREATE TABLE Servicios (
                Id_Servicio INTEGER PRIMARY KEY AUTOINCREMENT,
                Tipodeservicio VARCHAR(50),
                Descripción TEXT,
                Costo INTEGER,
                Fecha DATE,
                Cliente_Id_Cliente INTEGER,
                Empleado_Id_Empleado INTEGER,
                Vehiculo_Id_Vehiculo VARCHAR(20),
                FOREIGN KEY (Cliente_Id_Cliente) REFERENCES Cliente (CI),
                FOREIGN KEY (Empleado_Id_Empleado) REFERENCES Empleado (Nombre_Usuario),
                FOREIGN KEY (Vehiculo_Id_Vehiculo) REFERENCES Vehiculo (Placa)
            )
        """
        const val CREAR_VEHICULOS = """
            CREATE TABLE Vehiculo (
                Placa VARCHAR(10) PRIMARY KEY,
                Modelo VARCHAR(10),
                Marca VARCHAR(20),
                Año VARCHAR(10),
                Cliente_Id_Cliente INTEGER NOT NULL,
                FOREIGN KEY (Cliente_Id_Cliente) REFERENCES Cliente (CI)
            )
        """

        const val POBLAR_CLIENTES = """
            INSERT INTO Cliente (CI, Nombre_Completo, Telefono)
            VALUES (7517128, 'Timosboy', 78680058)
        """
        const val POBLAR_VEHICULOS = """
            INSERT INTO Vehiculo (Placa, Modelo, Marca, Año, Cliente_Id_Cliente)
            VALUES ('ABC123', 'Chiron', 'Bugatti', '2024', 7517128)
        """
        const val POBLAR_SERVICIOS = """
            INSERT INTO Servicios (Tipodeservicio, Descripción, Costo, Fecha, Cliente_Id_Cliente, Empleado_Id_Empleado, Vehiculo_Id_Vehiculo)
            VALUES ('Mantenimiento', 'Cambio de aceite', 100, '2024-06-01', 7517128, 'admin', 'ABC123')
        """
        const val POBLAR_REPORTES = """
            INSERT INTO Reportes (Empleado_Id_Empleado, Cliente_Id_Cliente, Vehiculo_Id_Vehiculo, Servicios_Id_Servicio)
            VALUES ('admin', 7517128, 'ABC123', 1)
        """
        const val POBLAR_EMPLEADOS = """
            INSERT INTO Empleado (Nombre_Usuario, Nombre_Completo, Contraseña)
            VALUES ('admin', 'Administrador', 'admin')
        """

        const val DROP_CLIENTES = "DROP TABLE IF EXISTS Cliente"
        const val DROP_VEHICULOS = "DROP TABLE IF EXISTS Vehiculo"
        const val DROP_SERVICIOS = "DROP TABLE IF EXISTS Servicios"
        const val DROP_REPORTES = "DROP TABLE IF EXISTS Reportes"
        const val DROP_EMPLEADOS = "DROP TABLE IF EXISTS Empleado"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREAR_CLIENTES)
            db?.execSQL(CREAR_EMPLEADOS)
            db?.execSQL(CREAR_REPORTES)
            db?.execSQL(CREAR_SERVICIOS)
            db?.execSQL(CREAR_VEHICULOS)

            db?.execSQL(POBLAR_CLIENTES)
            db?.execSQL(POBLAR_EMPLEADOS)
            db?.execSQL(POBLAR_VEHICULOS)
            db?.execSQL(POBLAR_SERVICIOS)
            db?.execSQL(POBLAR_REPORTES)
        } catch (e: Exception) {
            Log.e("AdminSQL", "Error creating database: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(DROP_CLIENTES)
        db?.execSQL(DROP_VEHICULOS)
        db?.execSQL(DROP_SERVICIOS)
        db?.execSQL(DROP_REPORTES)
        db?.execSQL(DROP_EMPLEADOS)
        onCreate(db)

    }
}
