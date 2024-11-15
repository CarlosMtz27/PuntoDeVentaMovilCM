package com.uacm.cm.puntodeventa.conexion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.uacm.cm.puntodeventa.modelo.DetalleVenta;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.ProductoDato;
import java.util.ArrayList;

public class DetalleVentaDao {
    private SQLiteDatabase bd;
    private ProductoDao productoDao;

    public DetalleVentaDao(Context context) throws Exception{
        try{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "punto_de_venta", null,1);
            this.bd = admin.getWritableDatabase();
        }catch (Exception e){
            throw new Exception("Error en la conexion con la base de datos. "+e);
        }
    }

    //Metodo para registrar el detalle de una venta
    public Long registrarDetalleVenta(DetalleVenta detalleVenta, Context context) throws Exception {
        ContentValues registro = new ContentValues();
        registro.put("venta", detalleVenta.dameVentas());
        registro.put("producto",detalleVenta.dameProducto());
        registro.put("cantidad",detalleVenta.dameCantidad());
        registro.put("precio_venta",detalleVenta.damePrecioVenta());
        productoDao = new ProductoDao(context);
        Producto producto = productoDao.buscarProductoCodigo(detalleVenta.dameProducto());
        productoDao.editarCantidadProducto(producto.dameCantidad()-1, producto.dameCodigo());
        return bd.insert("venta_detalles", null,registro);
    }

    //Metodo que obtiene los productos de una venta
    public ArrayList<Producto> obtenerProductosVenta(int idDetalleVenta, Context context) {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            productoDao = new ProductoDao(context);
            Cursor cursor = bd.rawQuery("SELECT producto FROM venta_detalles WHERE venta=?", new String[]{String.valueOf(idDetalleVenta)});
            Log.d("mine", "Cantidad de productos encontrados: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Producto producto = productoDao.buscarProductoCodigo(cursor.getString(0));
                    if (producto != null) {
                        productos.add(producto);
                    } else {
                        Producto productoEliminado = new Producto("0","Producto eliminado","producto no disponible", 0,0);
                        productos.add(productoEliminado);

                    }
                } while (cursor.moveToNext());
            }
            for (Producto p: productos){
                Log.d("mine", p.dameNombre());
            }
            cursor.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los productos de una venta: " + e);
        }
        return productos;
    }

    public ArrayList<ProductoDato> obtenerProductosMasVendidos(){
        ArrayList<ProductoDato> productosMasVendidos = new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT p.nombre, SUM(vd.cantidad) AS total_vendido "+
                "FROM productos p "+
                "JOIN venta_detalles vd ON p.codigo = vd.producto "+
                "GROUP BY p.codigo "+
                "ORDER BY total_vendido DESC "+
                "LIMIT 5", null);

        if (cursor.moveToFirst()){
            do{
                String nombreProducto = cursor.getString(0);
                int cantidadVendida = cursor.getInt(1);
                productosMasVendidos.add(new ProductoDato(nombreProducto,cantidadVendida));
            }while(cursor.moveToNext());
        }

        cursor.close();
        bd.close();
        return productosMasVendidos;
    }

}
