package com.fede.nextu.controldegastos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    protected BarChart barChart;
    private EditText editText;
    private InterstitialAd interstitialAd;
    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
    BarEntry valor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        interstitialAd =new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        barChart = (BarChart) findViewById(R.id.chart1);
        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        //barChart.setDescription("tabla de gastos");
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new Formater(barChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.SERIF);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setTextSize(8f);
        xAxis.setValueFormatter(xAxisFormatter);
        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(8f);
        l.setXEntrySpace(4f);
        editText = (EditText) findViewById(R.id.edit_gastos);

        yVals1.add(new BarEntry(0,0));
        insertarBar(yVals1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarValores(7,100);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            verPublicidad();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
            .build();
        interstitialAd.loadAd(adRequest);
    }

    private void verPublicidad() {
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }else {
            Toast.makeText(this, "No se cargo la Publicidad", Toast.LENGTH_SHORT).show();
            iniciarPublicidad();
        }
    }

    private void iniciarPublicidad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

    private void insertarValores(int count, float range) {

        float start = 0f;
        barChart.getXAxis().setAxisMinValue(start);
        barChart.getXAxis().setAxisMaxValue(start + count + 1);

        String entrada = editText.getText().toString();
        String[] arreglo = entrada.split(",");

        valor1= new BarEntry(obtenerDia(), Float.parseFloat(arreglo[1]));
        yVals1.add(valor1);
        insertarBar(yVals1);

    }

    private void insertarBar(ArrayList<BarEntry> vals) {

        float start = 0f;
        barChart.getXAxis().setAxisMinValue(start);
        barChart.getXAxis().setAxisMaxValue(start + 7 + 1);

        BarDataSet set1;
        set1 = new BarDataSet(vals, "Gastos");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(8f);
        data.setValueTypeface(Typeface.SERIF);
        data.setBarWidth(0.6f);
        barChart.setData(data);
        barChart.getData().notifyDataChanged();
        barChart.invalidate();

    }


    private int obtenerDia() {
        String entrada = editText.getText().toString();

        if (entrada.startsWith("lunes")) {
            return 1;
        } else if (entrada.startsWith("martes")) {
            return 2;
        } else if (entrada.startsWith("miércoles")) {
            return 3;
        } else if (entrada.startsWith("jueves")) {
            return 4;
        } else if (entrada.startsWith("viernes")) {
            return 5;
        } else if (entrada.startsWith("sábado")) {
            return 6;
        } else if (entrada.startsWith("domingo")) {
            return 7;
        }
        return 0;
    }

}
