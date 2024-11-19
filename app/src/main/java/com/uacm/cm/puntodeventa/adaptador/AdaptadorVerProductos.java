package com.uacm.cm.puntodeventa.adaptador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

//Adaptador que realiza las funciones del recycler view del fragemnt productos
public class AdaptadorVerProductos extends RecyclerView.Adapter<AdaptadorVerProductos.ViewHolder> implements Filterable {
    private ArrayList<Producto> productos;       // Lista original de productos
    private ArrayList<Producto> filtroProductos; // Lista filtrada de productos
    private OnItemClickListener listener;

    private int seleccionPosicion = RecyclerView.NO_POSITION;

    public AdaptadorVerProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        this.filtroProductos = new ArrayList<>(productos); // Inicializamos la lista de filtro con los productos originales
    }

    public void desSeleccionarElemento() {
        int previoSeleccionado = seleccionPosicion;
        seleccionPosicion = RecyclerView.NO_POSITION;
        notifyItemChanged(previoSeleccionado);
    }

    public interface OnItemClickListener{
        void onItemClick(int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ver_productos, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el producto de la posición actual
        Producto producto = productos.get(position);
        holder.asignarDatos(producto);

        if(seleccionPosicion == position){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            listener.onItemClick(position);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(view -> {
            notifyItemChanged(seleccionPosicion);
            seleccionPosicion = holder.getAdapterPosition();
            notifyItemChanged(seleccionPosicion);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size(); // Devolver el tamaño de la lista filtrada
    }

    // ViewHolder que enlaza los datos del producto a las vistas
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView codigo, nombre, precio;
        ImageView icono;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            codigo = itemView.findViewById(R.id.codigoProducto);
            nombre = itemView.findViewById(R.id.nombreProducto);
            precio = itemView.findViewById(R.id.precioProducto);
            icono = itemView.findViewById(R.id.iconoView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener !=null){
                        int posicion = getAdapterPosition();
                        if(posicion !=RecyclerView.NO_POSITION){
                            listener.onItemClick(posicion);
                        }
                    }
                }
            });
        }

        public void asignarDatos(Producto producto) {
            codigo.setText(producto.dameCodigo());   // Asignar el codigo del producto
            nombre.setText(producto.dameNombre());   // Asignar el nombre del producto
            precio.setText(String.valueOf(producto.damePrecio())); // Asignar el precio del producto

            if(producto.dameCantidad() <=3){
                icono.setImageResource(R.drawable.baseline_warning_24);
            } else if (producto.dameCantidad()>=5) {
                icono.setImageResource(R.drawable.baseline_done_outline_24);
            }else{
                icono.setImageResource(R.drawable.baseline_info_24);
            }
        }
    }

    // Implementacion del filtrado
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Producto> listaFiltrada = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                listaFiltrada.addAll(filtroProductos);  // Si no hay texto, mostramos todos los productos
            } else {
                String filtro = constraint.toString().toLowerCase().trim();

                // Filtramos productos cuyo nombre contenga el texto ingresado
                for (Producto producto : filtroProductos) {
                    if (producto.dameNombre().toLowerCase().contains(filtro)) {
                        listaFiltrada.add(producto);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productos.clear();
            productos.addAll((List) results.values);// Actualizamos la lista de productos filtrados
            desSeleccionarElemento();
            notifyDataSetChanged();// Notificar que los datos han cambiado para refrescar el RecyclerView
        }
    };
}
