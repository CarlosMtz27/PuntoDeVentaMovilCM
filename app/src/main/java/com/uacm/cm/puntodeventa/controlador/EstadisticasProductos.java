package com.uacm.cm.puntodeventa.controlador;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.conexion.DetalleVentaDao;
import com.uacm.cm.puntodeventa.modelo.ProductoDato;
import java.util.ArrayList;

public class EstadisticasProductos extends Fragment {

    private BarChart barChart;

    public EstadisticasProductos() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas_productos, container, false);
        barChart = view.findViewById(R.id.barChart);

        try {
            DetalleVentaDao detalleVentaDao = new DetalleVentaDao(getContext());
            ArrayList<ProductoDato> productosMasVendidos = detalleVentaDao.obtenerProductosMasVendidos();
            ArrayList<BarEntry> entries = new ArrayList<>();
            String[] productNames = new String[productosMasVendidos.size()];
            for (int i = 0; i < productosMasVendidos.size(); i++) {
                ProductoDato producto = productosMasVendidos.get(i);
                entries.add(new BarEntry(i, producto.getCantidadVenta()));
                productNames[i] = producto.getNombre();
            }
            // Configuramos el conjunto de datos
            BarDataSet dataSet = new BarDataSet(entries, "Ventas por Producto");
            dataSet.setColor(getResources().getColor(R.color.principal));
            dataSet.setValueTextSize(12f);
            dataSet.setValueTextColor(Color.WHITE);

            // Asignamos el conjunto de datos a la grafica
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            // Formateador para nombres de productos
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(productNames));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);

            // Configuracion final de la grafica
            barChart.getDescription().setEnabled(false);
            barChart.setDrawValueAboveBar(false);
            barChart.invalidate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return view;
    }
}