package com.uacm.cm.puntodeventa.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.developer.kalert.KAlertDialog;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.conexion.DetalleVentaDao;
import com.uacm.cm.puntodeventa.conexion.ProductoDao;
import com.uacm.cm.puntodeventa.conexion.VentaDao;
import com.uacm.cm.puntodeventa.modelo.DetalleVenta;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.Usuario;
import com.uacm.cm.puntodeventa.modelo.Venta;

import java.util.ArrayList;
import java.util.Date;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Pago extends AppCompatActivity {

    private EditText etDescuento, etPagoRecibido;
    private TextView tvVendedor, tvSubtotal, tvCambio, tvTotalPago, tvPagaCon, tvDescuento;
    private Usuario usuario;
    private ArrayList<Producto> productos;
    private Venta venta;
    private  float subtotal=0, descuento=0, totalPago=0;
    private ProductoDao productoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pago);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        productos = (ArrayList<Producto>) getIntent().getSerializableExtra("productos");
        etDescuento = (EditText) findViewById(R.id.etDescuento);
        etPagoRecibido =(EditText) findViewById(R.id.etPagoRecibido);
        tvVendedor = findViewById(R.id.tvVendedorPago);
        tvSubtotal = findViewById(R.id.tvSubTotal);
        tvCambio = findViewById(R.id.tvCambio);
        tvTotalPago= findViewById(R.id.tvTotalP);
        tvPagaCon = findViewById(R.id.tvPagaCon);
        tvDescuento = findViewById(R.id.tvDescuento);
        String vendedor = usuario.dameNombre() + " "+ usuario.dameApellidos();
        tvVendedor.setText(vendedor);
        tvSubtotal.setText(getIntent().getStringExtra("subTotal"));
        tvTotalPago.setText(getIntent().getStringExtra("subTotal"));
        findViewById(R.id.imgBillete20).setOnClickListener(this::seleccionarBillete);
        findViewById(R.id.imgBillete50).setOnClickListener(this::seleccionarBillete);
        findViewById(R.id.imgBillete100).setOnClickListener(this::seleccionarBillete);
        findViewById(R.id.imgBillete200).setOnClickListener(this::seleccionarBillete);
    }

    private void seleccionarBillete(View view) {
        int billete = 0;

        YoYo.with(Techniques.Tada).duration(900).playOn(view);
        if(R.id.imgBillete20 == view.getId()){
            billete = 20;
            Toast.makeText(this,"20 pesos", Toast.LENGTH_LONG).show();
        }else if(R.id.imgBillete50 == view.getId()){
            billete = 50;
            Toast.makeText(this,"50 pesos", Toast.LENGTH_LONG).show();
        }else if(R.id.imgBillete100 == view.getId()){
            billete = 100;
            Toast.makeText(this,"100 pesos", Toast.LENGTH_LONG).show();
        }else if(R.id.imgBillete200 == view.getId()){
            billete = 200;
            Toast.makeText(this,"200 pesos", Toast.LENGTH_LONG).show();
        }
        etPagoRecibido.setText(String.valueOf(billete));
    }

    public void aplicarDescuento(View view){
        subtotal = Float.parseFloat( tvSubtotal.getText().toString());
        descuento = Float.parseFloat(etDescuento.getText().toString());
        totalPago =subtotal - (subtotal * (descuento/100));
        tvTotalPago.setText(String.valueOf(totalPago));
        Toast.makeText(this,"Descuento aplicado", Toast.LENGTH_LONG).show();
    }

    public void calcularPago(View view){
        float cambio = Float.parseFloat(etPagoRecibido.getText().toString()) - Float.parseFloat(tvTotalPago.getText().toString());
        tvPagaCon.setText(etPagoRecibido.getText().toString());
        tvCambio.setText(String.valueOf(cambio));
        tvDescuento.setText(String.valueOf(subtotal * (descuento/100)));
    }

    public void realizarPago(View view) throws Exception {
        String pagoRecibido = etPagoRecibido.getText().toString();
        String totalPago = tvTotalPago.getText().toString();
        String cambio = tvCambio.getText().toString();
        if (TextUtils.isEmpty(pagoRecibido) || TextUtils.isEmpty(totalPago) || TextUtils.isEmpty(cambio) || Float.parseFloat(cambio) < 0) {
            MotionToast.Companion.createColorToast(this,
                    "Error",
                    "Ingresa los datos del pago",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.roboto));

            return;
        }
        DetalleVenta detalleVenta;
        Date fecha = new Date();
        VentaDao ventaDao;
        DetalleVentaDao detalleVentaDao;
        productoDao = new ProductoDao(view.getContext());
        venta = new Venta(fecha, Float.parseFloat(tvTotalPago.getText().toString()), usuario);
        try {
            ventaDao = new VentaDao(view.getContext());
            detalleVentaDao = new DetalleVentaDao(view.getContext());
            long idVenta = ventaDao.registrarVenta(venta);
            for (Producto producto : productos) {
                detalleVenta = new DetalleVenta((int) idVenta, producto.dameCodigo(), 1, producto.damePrecio());
                detalleVentaDao.registrarDetalleVenta(detalleVenta, this);
            }

            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE,false);
            pDialog.setTitleText("¡Éxito!");
            pDialog.setContentText("Pago realizado con éxito.");
            pDialog.setConfirmText("OK");
            pDialog.setConfirmClickListener(kAlertDialog -> {
                pDialog.dismiss();
                // Iniciar la nueva actividad
                Intent inicio = new Intent(view.getContext(), Inicio.class);
                inicio.putExtra("usuario", usuario);
                inicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inicio);
                finish();
            });
            pDialog.show();
        } catch (Exception e) {
            throw new Exception("Error: " + e);
        }
    }

}