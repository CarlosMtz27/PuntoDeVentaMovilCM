package com.uacm.cm.puntodeventa.adaptador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.modelo.Venta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdaptadorVentas extends RecyclerView.Adapter<AdaptadorVentas.ViewHolderDatos> {

    ArrayList<Venta> ventas;
    private OnItemClickListener listener;
    private int seleccionPosicion = RecyclerView.NO_POSITION;

    public AdaptadorVentas(ArrayList<Venta> ventas) {
        this.ventas = ventas;
    }

    public interface OnItemClickListener{
        void onItemClick(int posicion);
    }
    @NonNull
    @Override
    //enlaza el adaptador con el RecyclerView
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ventas, parent, false);
        return new ViewHolderDatos(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        Venta venta = ventas.get(position);
        holder.asignarDatos(ventas.get(position));
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
        return ventas.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView tvIdVenta,tvTotalVenta,fechaVenta;
        public ViewHolderDatos(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvIdVenta = (TextView) itemView.findViewById(R.id.tvIdVenta);
            tvTotalVenta = (TextView) itemView.findViewById(R.id.tvTotalVenta);
            fechaVenta = (TextView) itemView.findViewById(R.id.fechaVenta);

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

        public void asignarDatos(Venta venta) {
            tvIdVenta.setText(String.valueOf(venta.dameId()));
            tvTotalVenta.setText(String.valueOf(venta.dameMontoTotal()));
            fechaVenta.setText(formatoFecha(venta.dameFechaVenta()));
        }

        public String formatoFecha(Date fechaVenta){
            fechaVenta = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaVenta);
            return fechaFormateada;
        }
    }


}


