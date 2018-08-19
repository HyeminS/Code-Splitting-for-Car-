package com.example.seohyemin.userdeviceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by seohyemin on 2018. 7. 29..
 */

public class SplashActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MainActivity.class 자리에 다음에 넘어갈 액티비티를 넣어주기
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("state", "launch");
        startActivity(intent);
        finish();
    }
}
