package jerry.build_test;

import android.graphics.Color;
import android.util.Log;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
// MPA

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private Sensor Ac, Gy;
    private SensorManager SM;
    private LineChart xA,yA,zA,xG,yG,zG;
    private SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        Ac = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Gyro
        Gy = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        listener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    draw(event,xA,yA,zA);
                    Log.v(TAG,"Trigger Ac");
                }
                else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    draw(event,xG,yG,zG);
                    Log.v(TAG,"Trigger Gy");
                }
                else {
                    Log.v(TAG,"Trigger" + event.sensor.getName());
                }
            }
        };


//        SM.registerListener(this,Gy,SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(listener,Ac,SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(listener,Gy,SensorManager.SENSOR_DELAY_NORMAL);

        // Assign view
        xA = (LineChart) findViewById(R.id.xAchart);
        yA = (LineChart) findViewById(R.id.yAchart);
        zA = (LineChart) findViewById(R.id.zAchart);
        xG = (LineChart) findViewById(R.id.xGychart);
        yG = (LineChart) findViewById(R.id.yGychart);
        zG = (LineChart) findViewById(R.id.zGychart);
        xA.setBackgroundColor(Color.WHITE);
        yA.setBackgroundColor(Color.WHITE);
        zA.setBackgroundColor(Color.WHITE);
        xG.setBackgroundColor(Color.WHITE);
        yG.setBackgroundColor(Color.WHITE);
        zG.setBackgroundColor(Color.WHITE);
        LineData dataxA = new LineData();
        LineData datayA = new LineData();
        LineData datazA = new LineData();
        LineData dataxG = new LineData();
        LineData datayG = new LineData();
        LineData datazG = new LineData();
        dataxA.setValueTextColor(Color.WHITE);
        datayA.setValueTextColor(Color.WHITE);
        datazA.setValueTextColor(Color.WHITE);
        dataxG.setValueTextColor(Color.WHITE);
        datayG.setValueTextColor(Color.WHITE);
        datazG.setValueTextColor(Color.WHITE);

        xA.setData(dataxA);
        yA.setData(datayA);
        zA.setData(datazA);
        xG.setData(dataxG);
        yG.setData(datayG);
        zG.setData(datazG);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy) {
    // Not in use
    }

    private void draw(SensorEvent event,LineChart X,LineChart Y,LineChart Z) {

        LineData dataX = X.getLineData();
        LineData dataY = Y.getLineData();
        LineData dataZ = Z.getLineData();

        ILineDataSet setX = dataX.getDataSetByIndex(0);
        ILineDataSet setY = dataY.getDataSetByIndex(0);
        ILineDataSet setZ = dataZ.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (setX == null) {
            setX = createSet();
            dataX.addDataSet(setX);
        }
        if (setY == null) {
            setY = createSet();
            dataY.addDataSet(setY);
        }
        if (setZ == null) {
            setZ = createSet();
            dataZ.addDataSet(setZ);
        }
////data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
        dataX.addEntry(new Entry(setX.getEntryCount(), event.values[0] + 5), 0);
        dataX.notifyDataChanged();

        // let the chart know it's data has changed
        X.notifyDataSetChanged();

        // limit the number of visible entries
        X.setVisibleXRangeMaximum(150);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry
        X.moveViewToX(dataX.getEntryCount());

        // Doing the above code for Y
        dataY.addEntry(new Entry(setY.getEntryCount(), event.values[1] + 5), 0);
        dataY.notifyDataChanged();
        Y.notifyDataSetChanged();
        Y.setVisibleXRangeMaximum(150);
        Y.moveViewToX(dataY.getEntryCount());

        // Doing the above code for Z
        dataZ.addEntry(new Entry(setZ.getEntryCount(), event.values[2] + 5), 0);
        dataZ.notifyDataChanged();
        Z.notifyDataSetChanged();
        Z.setVisibleXRangeMaximum(150);
        Z.moveViewToX(dataZ.getEntryCount());

    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(listener,Ac,SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(listener,Gy,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(listener);

    }


}
