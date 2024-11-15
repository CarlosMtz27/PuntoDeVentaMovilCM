package com.uacm.cm.puntodeventa.conexion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.uacm.cm.puntodeventa.modelo.Producto;
import java.util.ArrayList;

public class ProductoDao {
    private SQLiteDatabase bd;

    public ProductoDao(Context context) throws Exception {
        try{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "punto_de_venta", null,1);
            this.bd = admin.getWritableDatabase();
        }catch (Exception e){
            throw new Exception("Error en la conexion con la base de datos. "+e);
        }
    }

    //Metodo para agregar un producto a la base de datos
    public long agregarProducto(Producto producto){
        ContentValues registro = new ContentValues();
        registro.put("codigo", producto.dameCodigo());
        registro.put("nombre",producto.dameNombre());
        registro.put("descripcion", producto.dameDescripcion());
        registro.put("precio", producto.damePrecio());
        registro.put("cantidad", producto.dameCantidad());
        long resultado = bd.insert("productos", null,registro);

        return resultado;
    }

    //Metodo que obtiene todos los productos registrados en la bd
    public ArrayList<Producto> obtenerProductos() throws Exception {
        ArrayList<Producto> productos =  new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT codigo, nombre, descripcion, precio, cantidad FROM productos", null);
        if (cursor.moveToFirst()) {
            do {
                String codigo = cursor.getString(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                float precio = cursor.getFloat(3);
                int cantidad = cursor.getInt(4);
                Producto producto = new Producto(codigo, nombre, descripcion, precio, cantidad);
                productos.add(producto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    //Metodo para eliminar un producto
    public int eliminarProducto(Producto producto){
        String[] codigo = new String[] { String.valueOf(producto.dameCodigo()) };
        int resultado = bd.delete("productos","codigo = ?",codigo);
        return resultado;
    }

    //Metodo para buscar un producto por el codigo
    public Producto buscarProductoCodigo(String codigo) throws Exception {
        Cursor cursor = bd.rawQuery("SELECT nombre, descripcion, precio, cantidad FROM productos WHERE codigo = ?", new String[]{codigo});
        Producto producto = null;
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String descripcion = cursor.getString(1);
            float precio = cursor.getFloat(2);
            int cantidad = cursor.getInt(3);
            producto = new Producto(codigo, nombre, descripcion, precio, cantidad);
        }
        return producto;
    }

    //Metodo para saber si existe un producto con el codigo ingresado
    public boolean existeProducto(String codigo) {
        Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM Productos WHERE codigo = ?", new String[]{codigo});
        boolean existe = false;
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0;
        }
        cursor.close();
        return existe;
    }

    //Metodo para actualizar un producto de la base de datos
    public int actualizarProducto(Producto producto) {
        ContentValues values = new ContentValues();
        values.put("nombre", producto.dameNombre());
        values.put("descripcion", producto.dameDescripcion());
        values.put("precio", producto.damePrecio());
        values.put("cantidad", producto.dameCantidad());
        return bd.update("Productos", values, "codigo = ?", new String[]{producto.dameCodigo()});
    }

    public void editarCantidadProducto(int cantidad, String codigo){
        ContentValues values = new ContentValues();
        values.put("cantidad", cantidad);
        bd.update("Productos", values,"codigo = ?", new String[]{codigo});
    }
}
