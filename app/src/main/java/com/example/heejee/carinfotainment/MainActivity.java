package com.example.heejee.carinfotainment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    private String ip = "192.168.0.4";
    private String port = "8989";
    public int connect_check;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connect_check = 0;
        Blank_Thread thread = new Blank_Thread();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        Log.i("heho","통신시작");
       NetworkTask myClientTask = new NetworkTask(ip, Integer.parseInt(port)
        );

        myClientTask.execute();
        Log.i("heho","올바른 연결");
      //  if(connect_check == 1) {
        //    Log.i("hehe","6번");
           thread.start();
        //}


    }
    private class Blank_Thread extends Thread {

            public void run() {
                SystemClock.sleep(3000);
                new Thread(new Runnable() {
                    @Override public void run(){
                        for(int i = 0; i<1; i++) {
                            runOnUiThread (new Runnable(){ public void run(){
                                Connect();
                            } });
                        } }
                }).start();

            }
        }


    public void Connect(){

        Log.i("heho","메인화면 시작");

        setContentView(R.layout.activity_main);
            View view = getWindow().getDecorView();

            loadFragment(new Fragment_button());
            loadFragment(new Fragment_map());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (view != null) {
                    // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));
                }
            } else if (Build.VERSION.SDK_INT >= 21) {
                // 21 버전 이상일 때
                getWindow().setStatusBarColor(Color.BLACK);
            }


    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.relativelayout, fragment);
        fragmentTransaction.commit();
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        JSONObject jsonObject = new JSONObject();
        String response;

        NetworkTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

       private void sendObject() {
            try {
                Log.i("Open_State", "Open_State = 1");
                jsonObject.put("Open_State", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Log.e("TCP", "server connecting~~!");
                Socket socket = new Socket(dstAddress, dstPort);
                sendObject();

                try {
                   // InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                   printStream.print("Car start");
                    response = inFromServer.readLine();
                    //Log.i("hehe",response);
                    if(response.equals("hihi")){
                        Log.i("hehe",response);
                    }
                   printStream.close();

                } catch (IOException e) {
                    Log.e("TCP", "don't send message!");
                    e.printStackTrace();
                }
                socket.close();





            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }
}