package com.uacm.cm.puntodeventa.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table productos(codigo text primary key, nombre text, descripcion text, precio float, cantidad integer)");
        db.execSQL("create table ventas(id_venta integer primary key autoincrement, fecha_venta date, monto float, id_vendedor integer, foreign key(id_vendedor) references usuarios(id_usuario))");
        db.execSQL("create table venta_detalles(id_venta integer primary key autoincrement, venta integer, producto integer, cantidad integer, precio_venta float, foreign key(venta) references ventas(id_venta), foreign key(producto) references productos(codigo))");
        db.execSQL("create table usuarios(id_usuario integer primary key autoincrement, nombre text, apellidos text, edad integer, correo text, contrasenia text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
        db.execSQL("drop table if exists productos");
        db.execSQL("create table productos(codigo text primary key, nombre text, descripcion text, precio float, cantidad integer)");
        db.execSQL("drop table if exists ventas");
        db.execSQL("create table ventas(id_venta integer primary key autoincrement, fecha_venta date, monto float, id_vendedor integer, foreign key(id_vendedor) references usuarios(id_usuario))");
        db.execSQL("drop table if exists venta_detalles");
        db.execSQL("create table venta_detalles(id_venta integer primary key autoincrement, venta integer, producto integer, cantidad integer, precio_venta float, foreign key(venta) references ventas(id_venta), foreign key(producto) references productos(codigo))");
        db.execSQL("drop table if exists usuarios");
        db.execSQL("create table usuarios(id_usuario integer primary key autoincrement, nombre text, apellidos text, edad integer, correo text, contrasenia text)");

    }
}