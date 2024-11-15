package com.uacm.cm.puntodeventa.controlador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.conexion.UsuarioDao;
import com.uacm.cm.puntodeventa.modelo.Usuario;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Login extends AppCompatActivity {
    private Button btnIniciarSesion, btnRegistrarme;
    private EditText etCorreo, etPassword;
    private boolean passwordVisible = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarme = findViewById(R.id.btnRegistro);

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width())){
                        passwordVisible = !passwordVisible;
                        if(passwordVisible){
                            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass,0,R.drawable.remove_view,0);
                        }else{
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass,0,R.drawable.remove,0);
                        }
                        etPassword.setSelection(etPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()){
                    etCorreo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.usuarioicon,0, R.drawable.baseline_done_24,0);
                }else {
                    etCorreo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.usuarioicon,0,0,0);
                }
            }
        });

        //evento del boton para inciar sesion
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String correo = etCorreo.getText().toString();
                    String password = etPassword.getText().toString();
                    if (correo.isEmpty() || password.isEmpty()) {
                        MotionToast.Companion.createColorToast(Login.this,"AVISO","Por favor ingrese su correo y contraseña",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(Login.this, R.font.roboto));
                        return;
                    }

                    UsuarioDao usuarioDao = new UsuarioDao(Login.this);
                    Cursor fila = usuarioDao.verificarUsuario(correo,password);
                    if (fila.moveToFirst()) {
                        int id = fila.getInt(0);
                        String nombre = fila.getString(1);
                        String apellidos = fila.getString(2);
                        int edad = fila.getInt(3);

                        Usuario usuario = new Usuario(id, nombre, apellidos, edad, correo, password);

                        MotionToast.Companion.createColorToast(Login.this,"Exito","Bienvenido "+usuario.dameNombre(),
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(Login.this, R.font.roboto));
                        SharedPreferences sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("sesionAbierta", true);
                        editor.putInt("ID", usuario.dameId());
                        editor.apply();
                        //Log.d("Sesion", "Sesión guardada como activa: " + sharedPreferences.getBoolean("sesionActiva", false));

                        Intent intentInicioSesion = new Intent(view.getContext(), Inicio.class);
                        intentInicioSesion.putExtra("usuario", usuario);
                        startActivity(intentInicioSesion);
                        finish();
                    } else {
                        MotionToast.Companion.createColorToast(Login.this,"Error","Correo o contraseña incorrectos",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    MotionToast.Companion.createColorToast(Login.this,"Error","Correo o contraseña incorrectos",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_TOP,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(Login.this, R.font.roboto));
                }
            }
        });

        //Evento del boton para registrarse
        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(view.getContext(), Registro.class);
                startActivity(intentRegistro);
            }
        });
    }
}