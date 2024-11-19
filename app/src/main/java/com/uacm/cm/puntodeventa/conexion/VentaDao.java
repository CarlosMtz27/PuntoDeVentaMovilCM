package com.uacm.cm.puntodeventa.conexion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uacm.cm.puntodeventa.modelo.ProductoDato;
import com.uacm.cm.puntodeventa.modelo.Usuario;
import com.uacm.cm.puntodeventa.modelo.Venta;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VentaDao {
    private SQLiteDatabase bd;
    Context context;
    public VentaDao(Context context) throws Exception{
        try{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "punto_de_venta", null,1);
            this.bd = admin.getWritableDatabase();
            this.context=context;
        }catch (Exception e){
            throw new Exception("Error en la conexion con la base de datos. "+e);
        }
    }

    //Metodo para registrar una venta en la bd
    public long registrarVenta(Venta venta) {
        ContentValues registro = new ContentValues();
        String fecha = formatoFecha(venta.dameFechaVenta());
        registro.put("fecha_venta", fecha);
        registro.put("monto", venta.dameMontoTotal());
        registro.put("id_vendedor",venta.dameVendedor().dameId());
        Long resultado = bd.insert("ventas",null, registro);
        bd.close();
        return resultado;
    }

    //Metodo para dar formato a una fecha
    public String formatoFecha(Date fechaVenta){
        fechaVenta = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fechaVenta);
    }

    //Metodo para obtener las ventas registradas
    public ArrayList<Venta> obtenerVentas() throws Exception {
        ArrayList<Venta> ventas = new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT id_venta, fecha_venta, monto, id_vendedor FROM ventas", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String fechaVentaStr = cursor.getString(1);
                float monto = cursor.getFloat(2);
                int idVendedor = cursor.getInt(3);
                Date fechaVenta = null;
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                if (fechaVentaStr != null && !fechaVentaStr.isEmpty()) {
                    try {
                        fechaVenta = formato.parse(fechaVentaStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new Exception("Error al convertir la fecha: " + fechaVentaStr, e);
                    }
                }
                UsuarioDao usuarioDao = new UsuarioDao(this.context);
                Usuario vendedor = usuarioDao.obtenerUsuario(idVendedor);
                Venta venta = new Venta(id, fechaVenta, monto, vendedor);
                ventas.add(venta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ventas;
    }


    public ArrayList<ProductoDato> obtenerVentasPorDia() {
        ArrayList<ProductoDato> ventasPorDia = new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT fecha_venta, SUM(monto) AS total_venta " +
                "FROM ventas " +
                "GROUP BY fecha_venta " +
                "ORDER BY fecha_venta ASC LIMIT 5", null);
        if (cursor.moveToFirst()) {
            do {
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_venta"));
                float totalVenta = cursor.getFloat(cursor.getColumnIndexOrThrow("total_venta"));
                ventasPorDia.add(new ProductoDato(fecha, (int) totalVenta));
            } while (cursor.moveToNext());
        }
        cursor.close();
        bd.close();
        return ventasPorDia;
    }
}
