package com.uacm.cm.puntodeventa.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.modelo.Producto;
import java.util.ArrayList;

public class AdaptadorListaPopup extends RecyclerView.Adapter<AdaptadorListaPopup.ViewHolder> {

    private ArrayList<Producto> productos;

    public AdaptadorListaPopup(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_popup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.dameNombre());
        holder.tvPrecio.setText(String.valueOf(producto.damePrecio()));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
        }
    }
}
