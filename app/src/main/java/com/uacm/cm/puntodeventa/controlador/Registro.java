package com.uacm.cm.puntodeventa.controlador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

public class Registro extends AppCompatActivity {

    private EditText etNombreRegistro, etApellidosRegistro, etEdadRegistro, etCorreoRegistro, etPasswordRegistro;
    private Button btnRegistro;
    private boolean passwordVisible=false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        etApellidosRegistro = findViewById(R.id.etApellidosRegistro);
        etEdadRegistro = findViewById(R.id.etEdadRegistro);
        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etPasswordRegistro = findViewById(R.id.etPasswordRegistro);
        btnRegistro = findViewById(R.id.btnRegistro);

        etPasswordRegistro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= (etPasswordRegistro.getRight() - etPasswordRegistro.getCompoundDrawables()[2].getBounds().width())){
                        passwordVisible = !passwordVisible;
                        if(passwordVisible){
                            etPasswordRegistro.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPasswordRegistro.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.remove_view,0);
                        }else{
                            etPasswordRegistro.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPasswordRegistro.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.remove,0);
                        }
                        etPasswordRegistro.setSelection(etPasswordRegistro.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        etCorreoRegistro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()){
                    etCorreoRegistro.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.baseline_done_24,0);
                }else {
                    etCorreoRegistro.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UsuarioDao usuarioDao = new UsuarioDao(Registro.this);
                    String nombre = etNombreRegistro.getText().toString().trim();
                    String apellidos = etApellidosRegistro.getText().toString().trim();
                    int edad = Integer.parseInt(etEdadRegistro.getText().toString().trim());
                    String correo = etCorreoRegistro.getText().toString().trim();
                    String contraseña = etPasswordRegistro.getText().toString().trim();
                    Usuario usuario = new Usuario(nombre, apellidos, edad, correo, contraseña);
                    if (usuarioDao.registrarUsuario(usuario) != -1) {
                        MotionToast.Companion.createColorToast(Registro.this,"Exito","Usuario Registrado"+usuario.dameNombre(),
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(Registro.this, R.font.roboto));
                        Intent intentLogin = new Intent(view.getContext(), Login.class);
                        startActivity(intentLogin);
                        finish();
                    } else {
                        MotionToast.Companion.createColorToast(Registro.this,"Error","No se pudo registrar el usuario",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(Registro.this, R.font.roboto));
                    }
                } catch (Exception e) {
                    MotionToast.Companion.createColorToast(Registro.this,"AVISO","Completa los campos",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(Registro.this, R.font.roboto));
                    e.printStackTrace();
                }
            }
        });
    }
}