package com.example.trucksharingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor pressure;
    private TextView temp,pres;
    ImageButton changeSensor;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        temp = findViewById(R.id.temperature);
      //  pres = findViewById(R.id.pressure);
//        changeSensor=findViewById(R.id.changeSensor);
//        changeSensor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(SensorActivity.this, changeSensor);
//                popup.getMenuInflater()
//                        .inflate(R.menu.sensors, popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getTitle().equals("Temperature")){
//                            Toast.makeText(getApplicationContext(), "Temperature",Toast.LENGTH_SHORT).show();
//                            pressure = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//                        }
//                        else if (item.getTitle().equals("Pressure")){
//                            pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//                            Toast.makeText(getApplicationContext(), "Pressure",Toast.LENGTH_SHORT).show();
//                        }
//
//                        return true;
//                    }
//                });
//
//                popup.show();
//            }
//        });
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        } else {
            pres.setText("pressure sensor is not available");
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            pressure = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            pres.setText("temperature sensor is not available");
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float millibarsOfPressure = event.values[0];
        // Do something with this sensor data.
      //  pres.setText("Pressure is "+(int) millibarsOfPressure);
        temp.setText("Temperature is "+event.values[0]+"°C");
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}