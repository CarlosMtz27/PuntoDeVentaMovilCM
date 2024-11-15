package com.uacm.cm.puntodeventa.modelo;

import java.io.Serializable;

public class Producto implements Serializable {
    private String codigo, nombre,descripcion;
    private float precio;
    private int cantidad;

    public Producto(String codigo, String nombre, String descripcion, float precio, int cantidad) throws Exception {
        try {
            asignarCodigo(codigo);
            asignarNombre(nombre);
            asignarDescripcion(descripcion);
            asignarPrecio(precio);
            asignarCantidad(cantidad);
        }catch (Exception e){
            throw new Exception("Error: "+e);
        }
    }

    public String dameCodigo(){
        return codigo;
    }
    private void asignarCodigo(String codigo){
        this.codigo = codigo;
    }
    public String dameNombre() {
        return nombre;
    }

    private void asignarNombre(String nombre) {
        this.nombre = nombre;
    }

    public String dameDescripcion() {
        return descripcion;
    }

    private void asignarDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float damePrecio() {
        return precio;
    }

    private void asignarPrecio(float precio) throws Exception {
        if(precio >0){
            this.precio = precio;
        }else if(precio==0){
            this.precio = 0;
        }else {
            throw new Exception("El precio debe ser mayor a 0");
        }
    }

    public int dameCantidad() {
        return cantidad;
    }

    public void asignarCantidad(int cantidad) throws Exception {
        if(cantidad >0){
            this.cantidad = cantidad;
        }else if(cantidad==0){
            this.cantidad = 0;
        }else {
            throw new Exception("La cantidad debe ser mayor a 0");
        }

    }

    @Override
    public String toString() {
        return nombre;
    }
}
