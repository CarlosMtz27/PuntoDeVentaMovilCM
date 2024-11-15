package com.uacm.cm.puntodeventa.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.developer.kalert.KAlertDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.conexion.ProductoDao;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.Usuario;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class AgregarProducto extends AppCompatActivity {
    private EditText etNombreProducto, etCodigoProducto, etDescripcionProducto,etPrecioProducto,etCantidadProducto;
    private ProductoDao productoDao;
    private Usuario usuario;
    Button btnEscanearCodigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_producto);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.etNombreProducto = findViewById(R.id.etNombreProducto);
        this.etCodigoProducto = findViewById(R.id.etCodigoProdcto);
        this.etDescripcionProducto = findViewById(R.id.etDescripcionProducto);
        this.etPrecioProducto = findViewById(R.id.etPrecioProducto2);
        this.etCantidadProducto = findViewById(R.id.etCantidadProducto);
        btnEscanearCodigo = (Button) findViewById(R.id.btnEscanearCodigo);

        btnEscanearCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(AgregarProducto.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Escanear Código");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            etCodigoProducto.setText(intent.getStringExtra("codigo"));
            if (etCodigoProducto != null && !etCodigoProducto.getText().toString().isEmpty()) {
                etCodigoProducto.setFocusable(false);
                etCodigoProducto.setFocusableInTouchMode(false);
                etCodigoProducto.setClickable(false);
                btnEscanearCodigo.setEnabled(false);
            }
            etNombreProducto.setText(intent.getStringExtra("nombre"));
            etDescripcionProducto.setText(intent.getStringExtra("descripcion"));
            etPrecioProducto.setText(String.valueOf(intent.getFloatExtra("precio", 0)));
            etCantidadProducto.setText(String.valueOf(intent.getIntExtra("cantidad", 0)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() != null){
                String codigoEscaneado = result.getContents();
                etCodigoProducto.setText(codigoEscaneado);
                etCodigoProducto.setFocusable(true);
                etCodigoProducto.setFocusableInTouchMode(true);
                etCodigoProducto.setClickable(true);
            }else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void guardarProducto(View view) throws Exception {
        try {
            String nombre = etNombreProducto.getText().toString();
            String codigo = etCodigoProducto.getText().toString();
            String descripcion = etDescripcionProducto.getText().toString();
            float precio = Float.parseFloat(etPrecioProducto.getText().toString());
            int cantidad = Integer.parseInt(etCantidadProducto.getText().toString());
            Producto producto = new Producto(codigo, nombre, descripcion, precio, cantidad);
            try {
                productoDao = new ProductoDao(this);
                if (productoDao.existeProducto(codigo)) {
                    int result = productoDao.actualizarProducto(producto);
                    if (result != -1) {
                        KAlertDialog sDialog = new KAlertDialog(view.getContext());
                        sDialog.setTitleText("¡Éxito!")
                                .setContentText("El producto ha sido editado")
                                .setConfirmClickListener("OK", new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        Intent inicio = new Intent(view.getContext(), Inicio.class);
                                        inicio.putExtra("mostrarFragmento", "productos");
                                        inicio.putExtra("usuario", usuario);
                                        // Cierra la actividad anterior
                                        inicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(inicio);
                                        finish();  // Finaliza la actividad actual
                                    }
                                })
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        sDialog.show();

                    } else {
                        MotionToast.Companion.createColorToast(this,"Error","Producto no eliminado",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                    }
                } else {
                    long resultado = productoDao.agregarProducto(producto);
                    if (resultado != -1) {
                        KAlertDialog sDialog = new KAlertDialog(view.getContext());
                        sDialog.setTitleText("¡Éxito!")
                                .setContentText("El producto ha sido agregado")
                                .setConfirmClickListener("OK", new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        Intent inicio = new Intent(view.getContext(), Inicio.class);
                                        inicio.putExtra("mostrarFragmento", "productos");
                                        inicio.putExtra("usuario", usuario);
                                        // Cierra la actividad anterior
                                        inicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(inicio);
                                        finish();  // Finaliza la actividad actual
                                    }
                                })
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        sDialog.show();

                    } else {
                        MotionToast.Companion.createColorToast(this,"Error","Producto no registrado",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                    }
                }
            } catch (Exception e) {
                throw new Exception("Error al intentar guardar el producto: "+e);
            }
        }catch (Exception e){
            throw new Exception("Error en la creacion del producto: "+e);
        }
    }
}