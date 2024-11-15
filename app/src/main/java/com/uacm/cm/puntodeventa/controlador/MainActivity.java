package com.uacm.cm.puntodeventa.controlador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.conexion.UsuarioDao;
import com.uacm.cm.puntodeventa.modelo.Usuario;

public class MainActivity extends AppCompatActivity {

    UsuarioDao usuarioDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        try {
            usuarioDao = new UsuarioDao(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciar(View view) throws Exception {

        // Verificamos el estado de la sesion
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);

        boolean sesionIniciada = preferences.getBoolean("sesionIniciada", false);
        Log.d("mine", "El valor de la sesion es: "+sesionIniciada);
        if (sesionIniciada) {
            int id = preferences.getInt("ID",-1);
            Log.d("mine", " \n"+id);
            Usuario usuario = usuarioDao.obtenerUsuario(id);
            Intent intentInicio = new Intent(this, Inicio.class);
            intentInicio.putExtra("usuario", usuario);
            startActivity(intentInicio);
            finish();
        }else{
            Intent intentLogin = new Intent(view.getContext(), Login.class);
            startActivity(intentLogin);
        }
    }
}