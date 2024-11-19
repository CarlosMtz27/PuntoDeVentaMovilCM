package com.uacm.cm.puntodeventa.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.adaptador.AdaptadorListaPopup;
import com.uacm.cm.puntodeventa.adaptador.AdaptadorVentas;
import com.uacm.cm.puntodeventa.conexion.DetalleVentaDao;
import com.uacm.cm.puntodeventa.conexion.VentaDao;
import com.uacm.cm.puntodeventa.modelo.Producto;
import com.uacm.cm.puntodeventa.modelo.Venta;
import java.util.ArrayList;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class VentasFragment extends Fragment {

    private RecyclerView rvVentas;
    private TextView tvTotalVentas,tvCantidadProductosPop, tvTotalVentaPop, tvVendedorPop;
    private ArrayList<Venta> ventas;
    private Venta ventaSeleccionada;
    private VentaDao ventaDao;
    private DetalleVentaDao detalleVentaDao;
    private AdaptadorVentas adapter;
    private int posicionLista = -1;
    private Button btnVerDetallesVenta;
    private Dialog dialogoPop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        ventas = new ArrayList<>();
        tvTotalVentas = view.findViewById(R.id.tvTotalVentas);
        btnVerDetallesVenta =view.findViewById(R.id.btnVerDetallesVenta);
        dialogoPop = new Dialog(getContext());
        try {
            ventaDao = new VentaDao(getActivity());
            ventas = ventaDao.obtenerVentas();
            adapter = new AdaptadorVentas(ventas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        rvVentas = view.findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvVentas.setAdapter(adapter);
        tvTotalVentas.setText(String.valueOf(ventas.size()));

        adapter.setOnItemClickListener(new AdaptadorVentas.OnItemClickListener() {
            @Override
            public void onItemClick(int posicion) {
                posicionLista = posicion;
                ventaSeleccionada = ventas.get(posicion);
            }
        });

        btnVerDetallesVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ventaSeleccionada != null) {
                        // Inicializar el DAO de detalle de ventas
                        detalleVentaDao = new DetalleVentaDao(view.getContext());
                        ArrayList<Producto> productosVenta = detalleVentaDao.obtenerProductosVenta(ventaSeleccionada.dameId(), view.getContext());

                        // Configurar el popup
                        dialogoPop.setContentView(R.layout.popup_detalle_ventas);
                        dialogoPop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        RecyclerView rvListaProductosPop = dialogoPop.findViewById(R.id.rvListaProductosPopup);
                        tvTotalVentaPop = dialogoPop.findViewById(R.id.tvTotalVentaPop);
                        tvCantidadProductosPop = dialogoPop.findViewById(R.id.tvCantidadProductosPop);
                        tvVendedorPop = dialogoPop.findViewById(R.id.tvVendedorPop);

                        // Mostrar detalles de la venta
                        tvTotalVentaPop.setText(String.valueOf(ventaSeleccionada.dameMontoTotal()));
                        tvCantidadProductosPop.setText(String.valueOf(productosVenta.size()));

                        // Verificar si el vendedor no es null antes de acceder a sus datos
                        if (ventaSeleccionada.dameVendedor() != null) {
                            String vendedor = ventaSeleccionada.dameVendedor().dameNombre() + " " + ventaSeleccionada.dameVendedor().dameApellidos();
                            tvVendedorPop.setText(vendedor);
                        } else {
                            tvVendedorPop.setText("Vendedor no disponible");
                            // Log para depuración
                            Log.e("VentasFragment", "El vendedor es null para la venta con ID: " + ventaSeleccionada.dameId());
                        }

                        // Configurar adaptador para los productos de la venta
                        AdaptadorListaPopup adaptador = new AdaptadorListaPopup(productosVenta);
                        rvListaProductosPop.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvListaProductosPop.setAdapter(adaptador);

                        // Mostrar el popup
                        dialogoPop.show();
                    } else {
                        MotionToast.Companion.createColorToast(getActivity(),"INFO","Seleccione un elemento primero",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(view.getContext(), R.font.roboto));
                    }
                } catch (Exception e) {
                    Log.e("VentasFragment", "Error al mostrar los detalles de la venta.", e);
                }
            }
        });
        return view;
    }
}
