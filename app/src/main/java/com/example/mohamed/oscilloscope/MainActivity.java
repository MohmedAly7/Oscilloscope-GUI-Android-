package com.example.mohamed.oscilloscope;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final Random RANDOM = new Random();
    private  GraphView graph ;
    private  float time ;
    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;
    private LineGraphSeries<DataPoint> series3;
    private Spinner tDiv , vDiv ;
   // private LineGraphSeries<DataPoint> series4;
    private int invertch2 ;
    private float lastX = -10;
    private int x=1;
    private final int  numberOfData =200 ;
  /* public void addItemsOnSpinner2() {

        tDiv = (Spinner) findViewById(R.id.timeDiv);
        List<String> list = new ArrayList<String>();
        list.add("1S\Div");
        list.add("");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tDiv.setAdapter(dataAdapter);
    }*/
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked

                if (checked)
                    invertch2 = -1;
                else
                    invertch2 = 1;

        }


       protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graph = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0),
                new DataPoint(3, 0),
                new DataPoint(4, 0)
        });
           time=20;
        graph.addSeries(series);
           invertch2 =1;
           series2 = new LineGraphSeries<DataPoint>();
           series1 = new LineGraphSeries<DataPoint>();
           series3 = new LineGraphSeries<DataPoint>();
        // we get graph view instance
        // GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        // series = new LineGraphSeries<DataPoint>()
        // customize a little bit viewport

           final Viewport viewport = graph.getViewport();
           graph.getGridLabelRenderer().setNumVerticalLabels(10);
           graph.getGridLabelRenderer().setNumHorizontalLabels(10);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-1);
           viewport.setMaxY(1);
        viewport.setScrollable(true);

        /*
        * Radio group reference and listner
        * */

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Viewport viewport1 = graph.getViewport();
                viewport1.setYAxisBoundsManual(true);
                viewport1.setXAxisBoundsManual(true);
                viewport.setScrollable(false);
                viewport1.setMinX(-5);
                viewport1.setMaxX(5);

                switch (checkedId) {
                    case R.id.ch1:
                        x = 1;
                        graph.removeAllSeries();
                        graph.refreshDrawableState();
                        viewport1.setMinY(-1);
                        viewport1.setMaxY(1);
                        graph.addSeries(series1);
                        break;
                    case R.id.ch2:
                        viewport1.setMinY(-10);
                        viewport1.setMaxY(10);
                        x = 2;
                        graph.removeAllSeries();
                        graph.refreshDrawableState();
                        graph.addSeries(series2);
                        break;
                    case R.id.both:
                        viewport1.setMinY(-10);
                        viewport1.setMaxY(10);
                        x = 3;
                        graph.removeAllSeries();
                        graph.refreshDrawableState();
                        graph.addSeries(series1);
                        graph.addSeries(series2);
                        break;
                    case R.id.add:
                        viewport1.setMinY(-11);
                        viewport1.setMaxY(11);
                        x = 4;
                        graph.removeAllSeries();
                        graph.refreshDrawableState();
                        graph.addSeries(series3);
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                while(true){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry(x);
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }
    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }
    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    // add random data to graph
    private void addEntry(int intch) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        lastX = lastX +.3f;
        switch(intch){
            case 1:
                if (lastX>=time)
                {
                   graph.removeAllSeries();
                    lastX =-10;
                    series1   = new LineGraphSeries<DataPoint>();
                    series1.appendData(new DataPoint(lastX,Math.sin(lastX)), false, numberOfData);
                   graph.addSeries(series1);
                    return;

                }
                series1.appendData(new DataPoint(lastX,Math.sin(lastX)), false, numberOfData);
                break;
            case 2:
                if (lastX>=time)
                {
                    graph.removeAllSeries();
                    lastX =-10;
                    series2   = new LineGraphSeries<DataPoint>();
                    series2.appendData(new DataPoint(lastX,Math.sin(lastX)*10*invertch2), false, numberOfData);
                    graph.addSeries(series2);
                    return;

                }
                series2.appendData(new DataPoint(lastX,(Math.sin(lastX)*10*invertch2)), false, numberOfData);
                break;
            case 3 :
                if (lastX>=time)
                {
                    graph.removeAllSeries();
                    lastX =-10;
                    series1   = new LineGraphSeries<DataPoint>();
                    series2   = new LineGraphSeries<DataPoint>();
                    series1.appendData(new DataPoint(lastX,Math.sin(lastX)), false, numberOfData);
                    series2.appendData(new DataPoint(lastX,Math.sin(lastX)*10*invertch2), false, numberOfData);
                    graph.addSeries(series1);
                    graph.addSeries(series2);
                    return;

                }
                series1.appendData(new DataPoint(lastX,Math.sin(lastX)), false, numberOfData);
                series2.appendData(new DataPoint(lastX,Math.sin(lastX)*10*invertch2), false, numberOfData);
                break;
            case 4:
                if (lastX>=time)
                {
                    graph.removeAllSeries();
                    lastX =-10;
                    series3   = new LineGraphSeries<DataPoint>();
                    series3.appendData(new DataPoint(lastX,Math.sin(lastX)+Math.sin(lastX)*10*invertch2), false, numberOfData);
                    graph.addSeries(series3);
                    return;

                }
                series3.appendData(new DataPoint(lastX,Math.sin(lastX)+Math.sin(lastX)*10*invertch2),false,numberOfData);
                break;




        }

    }

}




