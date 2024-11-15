package com.uacm.cm.puntodeventa.modelo;

import java.io.Serializable;

public class DetalleVenta implements Serializable {
    private int id;
    private int numeroVenta;
    private String producto;
    private int cantidad;
    private float precioVenta;

    public DetalleVenta(int numeroVenta, String producto, Integer cantidad, float precioVenta) {
        this.numeroVenta= numeroVenta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        asignarVentas(numeroVenta);
        asignarProducto(producto);
        asignarCantidad(cantidad);
        asignarPrecioVenta(precioVenta);
    }

    public DetalleVenta(int id, int ventas, String producto, Integer cantidad, float precioVenta) {
        this.id = id;
        this.numeroVenta = ventas;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
    }

    public int dameId() {
        return id;
    }


    public int dameVentas() {
        return numeroVenta;
    }

    public void asignarVentas(int ventas) {
        this.numeroVenta = ventas;
    }

    public String dameProducto() {
        return producto;
    }

    public void asignarProducto(String producto) {
        this.producto = producto;
    }

    public Integer dameCantidad() {
        return cantidad;
    }

    public void asignarCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public float damePrecioVenta() {
        return precioVenta;
    }

    public void asignarPrecioVenta(float precioVenta){
        this.precioVenta = precioVenta;
    }

}
