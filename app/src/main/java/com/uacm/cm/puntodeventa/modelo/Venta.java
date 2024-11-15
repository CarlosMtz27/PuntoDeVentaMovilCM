package com.uacm.cm.puntodeventa.modelo;

import java.io.Serializable;
import java.util.Date;

public class Venta implements Serializable {
    private int id;
    private Date fechaVenta;
    private float montoTotal;
    private Usuario vendedor;

    public Venta(Date fechaVenta, float montoTotal, Usuario vendedor) {
       asignarFechaVenta(fechaVenta);
       asignarMontoTotal(montoTotal);
       asignarVendedor(vendedor);
    }

    private void asignarVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
    public Usuario dameVendedor(){
        return vendedor;
    }

    public Venta(int id, Date fechaVenta, float montoTotal, Usuario vendedor) {
        this.id = id;

        asignarFechaVenta(fechaVenta);
        asignarMontoTotal(montoTotal);
        asignarVendedor(vendedor);
    }

    public int dameId() {
        return id;
    }

    public Date dameFechaVenta() {
        return fechaVenta;
    }

    public void asignarFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public float dameMontoTotal() {
        return montoTotal;
    }

    public void asignarMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }
}
