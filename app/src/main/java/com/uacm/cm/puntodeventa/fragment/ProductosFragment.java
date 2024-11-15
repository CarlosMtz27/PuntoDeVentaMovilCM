package com.uacm.cm.puntodeventa.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.developer.kalert.KAlertDialog;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.adaptador.AdaptadorVerProductos;
import com.uacm.cm.puntodeventa.conexion.ProductoDao;
import com.uacm.cm.puntodeventa.controlador.AgregarProducto;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.Usuario;
import java.util.ArrayList;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ProductosFragment extends Fragment {
    private TextView tituloProducto, tvCodigoPop, tvCantidadPop, tvDescripcionPop, tvPrecioPop;
    private Button btnEliminar, btnAgregar, btnEditar,btnVerDetalles;
    private RecyclerView rvProductos;
    private int posicionLista = -1;
    private ArrayList<Producto> productos;
    private ProductoDao productoDao;
    private SearchView svFiltrarProductos;
    private AdaptadorVerProductos adapter;
    private Usuario usuario;
    private Producto productoSeleccionado;
    private Dialog dialogoPop;
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        btnEliminar = view.findViewById(R.id.btnEliminar);
        btnAgregar = view.findViewById(R.id.btnAgregar);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnVerDetalles = view.findViewById(R.id.btnVerDetalles);
        rvProductos = view.findViewById(R.id.rvProductos);
        svFiltrarProductos = view.findViewById(R.id.svFiltrarProductos);
        dialogoPop = new Dialog(view.getContext());

        btnVerDetalles.setVisibility(View.GONE);
        btnEditar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);

        view.setOnTouchListener((v,event)->{
            if(posicionLista != -1){
                adapter.desSeleccionarElemento();
                posicionLista = -1;
                productoSeleccionado = null;
                btnVerDetalles.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
                btnEditar.setVisibility(View.GONE);
            }
            return false;
        });

        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable("usuario");
        }

        try {
            productoDao = new ProductoDao(getActivity());
            productos = productoDao.obtenerProductos();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        adapter = new AdaptadorVerProductos(productos);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductos.setAdapter(adapter);

        svFiltrarProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.desSeleccionarElemento();
                btnVerDetalles.setVisibility(View.GONE);
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        adapter.setOnItemClickListener(new AdaptadorVerProductos.OnItemClickListener() {
            @Override
            public void onItemClick(int posicion) {
                posicionLista = posicion;
                productoSeleccionado = productos.get(posicion);
                mostrarBotonVerDetalles();
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
                    animationView.setAnimation(R.raw.animationinfo);
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

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAgregarProducto = new Intent(getActivity(), AgregarProducto.class);
                intentAgregarProducto.putExtra("usuario", usuario);
                startActivity(intentAgregarProducto);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productoSeleccionado!=null){
                    Producto producto = productos.get(posicionLista);
                    Intent intent = new Intent(getActivity(), AgregarProducto.class);
                    intent.putExtra("codigo", producto.dameCodigo());
                    intent.putExtra("nombre", producto.dameNombre());
                    intent.putExtra("descripcion", producto.dameDescripcion());
                    intent.putExtra("precio", producto.damePrecio());
                    intent.putExtra("cantidad", producto.dameCantidad());
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                }else{
                    MotionToast.Companion.createColorToast(getActivity(),"AVISO","Seleccione un producto primero",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productoSeleccionado != null && posicionLista >= 0 && posicionLista < productos.size()) {
                    new KAlertDialog(view.getContext())
                            .setTitleText("¿Está seguro?")
                            .setContentText("Va a eliminar " + productoSeleccionado.dameNombre())
                            .setConfirmText("Sí, eliminar!")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    int resultado = productoDao.eliminarProducto(productoSeleccionado);
                                    if (resultado > 0) {
                                        productos.remove(posicionLista);
                                        adapter.notifyItemRemoved(posicionLista);
                                        adapter.notifyDataSetChanged();

                                        posicionLista = -1;
                                        productoSeleccionado = null;

                                        sDialog.setTitleText("¡Éxito!")
                                                .setContentText("El producto ha sido eliminado")
                                                .setConfirmClickListener("OK", null)
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    } else {
                                        MotionToast.Companion.createColorToast(getActivity(), "Error", "Producto no eliminado",
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                                    }
                                }
                            })
                            .show();
                } else {
                    MotionToast.Companion.createColorToast(getActivity(), "Error", "Seleccione un producto primero",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                }
            }
        });

        return view;
    }

    private void mostrarBotonVerDetalles(){
        btnVerDetalles.setVisibility(View.VISIBLE);
        Animation slideIn = AnimationUtils.loadAnimation(getContext(),R.anim.animacion_boton_ver_detalles);
        btnVerDetalles.startAnimation(slideIn);
        btnEditar.setVisibility(View.VISIBLE);
        btnEliminar.setVisibility(View.VISIBLE);
        btnEditar.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.boton_deslizar_derecha));
        btnEliminar.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.boton_deslizar_izquierda));

    }
}
