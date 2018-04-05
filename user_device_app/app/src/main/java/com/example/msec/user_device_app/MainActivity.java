package com.example.msec.user_device_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton car_open = (ToggleButton) findViewById(R.id.car_open);

        car_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tag", "button clicked!");
            }
        });
    }
}
