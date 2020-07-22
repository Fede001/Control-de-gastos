package com.fede.nextu.controldegastos;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by JHOAN on 17/03/2018.
 */

public class Formater implements IAxisValueFormatter {

    private BarLineChartBase<?> chart;

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        if (value == 1) {
            return "Lunes";
        } else if (value == 2) {
            return "Martes";
        } else if (value == 3) {
            return "Miercoles";
        } else if (value == 4) {
            return "Jueves";
        } else if (value == 5) {
            return "Viernes";
        } else if (value == 6) {
            return "Sabado";
        } else if (value == 7) {
            return "Domingo";
        } else {
            return "";
        }
    }

    public Formater(BarLineChartBase<?> chart) {
        this.chart = chart;
    }


}
