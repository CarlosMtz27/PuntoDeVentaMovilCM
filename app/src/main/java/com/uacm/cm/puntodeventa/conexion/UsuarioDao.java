package com.uacm.cm.puntodeventa.conexion;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.uacm.cm.puntodeventa.modelo.Usuario;


public class UsuarioDao {

    private SQLiteDatabase bd;
    private Usuario usuario;
    public UsuarioDao(Context context) throws Exception{
        try{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context, "punto_de_venta", null,1);
            this.bd = admin.getWritableDatabase();
        }catch (Exception e){
            throw new Exception("Error en la conexion con la base de datos. "+e);
        }
    }

    //Metodo para verificar el acceso a la aplicacion
    public Cursor verificarUsuario(String correo, String contraseña){
        return bd.rawQuery("SELECT id_usuario, nombre, apellidos, edad FROM usuarios WHERE correo = ? AND contrasenia = ?", new String[]{correo, contraseña});
    }

    //Metodo para obtener un usuario mediante su id
    public Usuario obtenerUsuario(int idUsuario) throws Exception {
        try {

            Cursor cursor = bd.rawQuery("SELECT id_usuario, nombre, apellidos, edad, correo, contrasenia FROM usuarios WHERE id_usuario = ?", new String[]{String.valueOf(idUsuario)});

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String apellidos = cursor.getString(2);
                int edad = cursor.getInt(3);
                String correo = cursor.getString(4);
                String contrasenia = cursor.getString(5);
                usuario = new Usuario(id, nombre, apellidos, edad, correo, contrasenia);
            }
            cursor.close();
        }catch(Exception e){

        }
        return usuario;
    }

    //Metodo para registrar un usuario en la bd
    public Long registrarUsuario(Usuario usuario){
        ContentValues registro = new ContentValues();
        registro.put("nombre", usuario.dameNombre());
        registro.put("apellidos", usuario.dameApellidos());
        registro.put("edad", usuario.dameEdad());
        registro.put("correo", usuario.dameCorreo());
        registro.put("contrasenia", usuario.dameContrasenia());
        Long resultado = bd.insert("usuarios", null,registro);
        bd.close();
        return resultado;
    }
}
