package com.uacm.cm.puntodeventa.modelo;

public class ProductoDato {
    private String nombre;
    private int cantidadVendida;

    public ProductoDato(String nombre, int cantidadVendida){
        this.nombre = nombre;
        this.cantidadVendida = cantidadVendida;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadVenta() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
}
