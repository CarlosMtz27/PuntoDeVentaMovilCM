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
import com.uacm.cm.puntodeventa.conexion.VentaDao;
import com.uacm.cm.puntodeventa.modelo.ProductoDato;
import java.util.ArrayList;

public class EstadisticasProductos extends Fragment {

    private BarChart barChart;
    private BarChart barChartDias;

    public EstadisticasProductos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas_productos, container, false);

        barChart = view.findViewById(R.id.barChart);
        barChartDias = view.findViewById(R.id.barChartDias);

        try {
            DetalleVentaDao detalleVentaDao = new DetalleVentaDao(getContext());
            VentaDao ventaDao = new VentaDao(getContext());

            ArrayList<ProductoDato> productosMasVendidos = detalleVentaDao.obtenerProductosMasVendidos();
            setupBarChart(barChart, productosMasVendidos, "Ventas por Producto", getResources().getColor(R.color.principal));

            ArrayList<ProductoDato> diasMasVendidos = ventaDao.obtenerVentasPorDia();
            setupBarChart(barChartDias, diasMasVendidos, "Ganancias por Día", getResources().getColor(R.color.secundario));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void setupBarChart(BarChart chart, ArrayList<ProductoDato> data, String label, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[data.size()];

        for (int i = 0; i < data.size(); i++) {
            ProductoDato item = data.get(i);
            entries.add(new BarEntry(i, item.getCantidadVenta()));
            labels[i] = formatLabel(item.getNombre());
        }

        // Configuramos el conjunto de datos
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(color);//aplicamos el color
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        // Asignar datos al grafico
        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        // Formateador para nombres en el eje X
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);


        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.invalidate();
    }

    private String formatLabel(String label) {
        if (label.length() > 10) {
            StringBuilder formattedLabel = new StringBuilder();
            String[] words = label.split(" ");
            int currentLineLength = 0;

            for (String word : words) {
                if (currentLineLength + word.length() > 10) {
                    formattedLabel.append("\n");
                    currentLineLength = 0;
                }
                formattedLabel.append(word).append(" ");
                currentLineLength += word.length() + 1;
            }

            return formattedLabel.toString().trim();
        }
        return label;
    }
}
