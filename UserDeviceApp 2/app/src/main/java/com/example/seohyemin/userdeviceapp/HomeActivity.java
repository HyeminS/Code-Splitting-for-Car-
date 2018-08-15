package com.example.seohyemin.userdeviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
      //  intent.putExtra("value","setPowerOn");
      //  Log.i("TCP", intent.getStringExtra("value"));

     //   startActivityForResult(intent, 3000);

//        setResult(RESULT_OK,resultIntent);
        finish();
//


        startActivity(intent);
       // finish();
    }

}
