package com.uacm.cm.puntodeventa.adaptador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.modelo.Producto;
import java.util.ArrayList;

//Este adaptador realiza las funcionalidades del recycler view de los productos, que se ocupa en el fragment principal
public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolderProductos> {

    private ArrayList<Producto> productos;
    private OnItemClickListener listener;
    private int seleccionPosicion = RecyclerView.NO_POSITION;

    public AdaptadorProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    public interface OnItemClickListener{
        void onItemClick(int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    //Enlaza adaptador con el itemlist
    public ViewHolderProductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new ViewHolderProductos(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProductos holder, int position) {
        Producto producto = productos.get(position);
        holder.asignarDatos(producto);

        if(seleccionPosicion == position){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(view -> {

            notifyItemChanged(seleccionPosicion);
            seleccionPosicion = holder.getAdapterPosition();
            notifyItemChanged(seleccionPosicion);

            if (listener != null) {
                listener.onItemClick(seleccionPosicion);
            }
        });
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolderProductos extends RecyclerView.ViewHolder {

        TextView nombre, precio;

        public ViewHolderProductos(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            precio = (TextView) itemView.findViewById(R.id.precio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int posicion = getAdapterPosition();
                        if (posicion != RecyclerView.NO_POSITION) {
                            listener.onItemClick(posicion);
                        }
                    }
                }
            });
        }

        public void asignarDatos(Producto producto) {
            nombre.setText(producto.dameNombre());
            precio.setText(String.valueOf(producto.damePrecio()));
        }
    }
}
