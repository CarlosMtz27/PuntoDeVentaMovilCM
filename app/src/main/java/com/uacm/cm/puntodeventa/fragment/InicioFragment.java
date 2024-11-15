package com.uacm.cm.puntodeventa.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.adaptador.AdaptadorProductos;
import com.uacm.cm.puntodeventa.conexion.ProductoDao;
import com.uacm.cm.puntodeventa.controlador.Pago;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.Usuario;

import java.util.ArrayList;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class InicioFragment extends Fragment {

    private TextView tvUsuario, tvTotalPago, tituloProducto, tvCodigoPop, tvCantidadPop, tvDescripcionPop, tvPrecioPop;
    private Button btnEliminarLista, btnPagar, btnVerDetalles, btnEscanear;;
    private SearchView svBuscarProducto;
    private ProductoDao productoDao;
    private ArrayList<Producto> productos;
    public AdaptadorProductos adapter;
    private int posicionLista;
    private float totalPago = 0;
    private RecyclerView rvProductosVenta;
    private Usuario usuario;
    private Dialog dialogoPop;
    private Producto productoSeleccionado;
    public InicioFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para el fragmento
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        usuario = (Usuario) getArguments().getSerializable("usuario");

        tvUsuario = view.findViewById(R.id.tvUsuario);
        tvTotalPago = view.findViewById(R.id.tvTotalPago);
        btnEliminarLista = view.findViewById(R.id.btnEliminarLista);
        svBuscarProducto = view.findViewById(R.id.svBuscarPoducto);
        rvProductosVenta = view.findViewById(R.id.rvProductosVenta);
        btnPagar = view.findViewById(R.id.btnPagar);
        btnEscanear = view.findViewById(R.id.btnEscanear);
        btnVerDetalles = view.findViewById(R.id.btnDetallesProducto);
        dialogoPop = new Dialog(view.getContext());
        
        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(InicioFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Escanear Código");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        btnVerDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productoSeleccionado != null) {
                    // Inflar el popup
                    dialogoPop.setContentView(R.layout.boton_layout);
                    dialogoPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    LottieAnimationView animationView = dialogoPop.findViewById(R.id.lottie_view);
                    animationView.setAnimation(R.raw.animationinfo); // Usa R.raw y el nombre del archivo sin extensión
                    animationView.playAnimation();

                    Button botonCerrarDialogo = dialogoPop.findViewById(R.id.botonCerrarDialogo);
                    tituloProducto = dialogoPop.findViewById(R.id.tituloBotonDialogo);
                    tvDescripcionPop = dialogoPop.findViewById(R.id.descripcionBotonDialogo);
                    tvPrecioPop = dialogoPop.findViewById(R.id.precioBotonDialogo);
                    tvCantidadPop = dialogoPop.findViewById(R.id.cantidadBotonDialogo);
                    tvCodigoPop = dialogoPop.findViewById(R.id.codigoBotonDialogo);

                    tituloProducto.setText(productoSeleccionado.dameNombre());
                    tvDescripcionPop.setText(productoSeleccionado.dameDescripcion());
                    tvPrecioPop.setText(String.valueOf(productoSeleccionado.damePrecio()));
                    tvCantidadPop.setText(String.valueOf(productoSeleccionado.dameCantidad()));
                    tvCodigoPop.setText(productoSeleccionado.dameCodigo());
                    // Mostramos el popup
                    dialogoPop.show();

                    botonCerrarDialogo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogoPop.cancel();
                        }
                    });
                } else {
                    MotionToast.Companion.createColorToast(getActivity(),"AVISO","Seleccione un producto primero",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                }
            }
        });
        rvProductosVenta.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        productos = new ArrayList<>();
        adapter = new AdaptadorProductos(productos);
        rvProductosVenta.setAdapter(adapter);

        String vendedor = usuario.dameNombre() + " " + usuario.dameApellidos();
        tvUsuario.setText(vendedor);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productos.size()>0){
                    Intent intentIrPago = new Intent(view.getContext(), Pago.class);
                    intentIrPago.putExtra("usuario", usuario);
                    intentIrPago.putExtra("subTotal", tvTotalPago.getText().toString());
                    intentIrPago.putExtra("productos", productos);
                    startActivity(intentIrPago);
                }else {
                    MotionToast.Companion.createColorToast(getActivity(),
                            "Error",
                            "Agregue productos a la lista",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContext(),R.font.roboto));
                }
            }
        });
        svBuscarProducto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    buscarProducto(s);
                    tvTotalPago.setText((String.valueOf(totalPago)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Manejar clics en la lista de productos
        adapter.setOnItemClickListener(new AdaptadorProductos.OnItemClickListener() {
            @Override
            public void onItemClick(int posicion) {
                productoSeleccionado = productos.get(posicion);
                Toast.makeText(getContext(), "Producto seleccionado: " + productoSeleccionado.dameNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        // Eliminar producto de la lista
        btnEliminarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productoSeleccionado != null) {
                    int posicion = productos.indexOf(productoSeleccionado);
                    if (posicion != -1) {
                        totalPago -= productos.get(posicion).damePrecio();
                        productos.remove(posicion);
                        adapter.notifyDataSetChanged();
                        tvTotalPago.setText(String.valueOf(totalPago));
                        // Reiniciar producto seleccionado
                        productoSeleccionado = null;
                        Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                        //Toast.makeText(getContext(), "Error al eliminar\nel producto", Toast.LENGTH_SHORT).show();
                        MotionToast.Companion.createColorToast(getActivity(),
                                "Error",
                                "Error al eliminar el producto!",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_CENTER,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getContext(),R.font.roboto));
                    }
                } else {
                    MotionToast.Companion.createColorToast(getActivity(),"INFO","Seleccione un producto primero",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                    //Toast.makeText(getContext(), "Selecciona un producto para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }



    private void buscarProducto(String codigo) throws Exception {
        try {
            productoDao = new ProductoDao(getContext());
            Producto producto = productoDao.buscarProductoCodigo(codigo);
            if(producto!=null){
                productos.add(producto);
                totalPago += producto.damePrecio();
            }else {
                MotionToast.Companion.createColorToast(getActivity(),"ERROR","Codigo incorrecto",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getContext(), R.font.roboto));
                //Toast.makeText(getContext(), "Código Incorrecto", Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String codigoEscaneado = result.getContents();
                try {
                    // Buscar el producto con el código escaneado
                    buscarProducto(codigoEscaneado);
                    tvTotalPago.setText(String.valueOf(totalPago));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al buscar producto", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
