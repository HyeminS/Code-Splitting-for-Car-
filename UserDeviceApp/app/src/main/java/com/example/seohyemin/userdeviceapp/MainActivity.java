package com.example.seohyemin.userdeviceapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {
    private String ip = "192.168.193.108";
    private String port = "7880";
    private ImageView imgCar;
    NetworkTask myClientTask;
    int btn_index = 0;

    private class NetworkManagerThread extends Thread {
        public void run() {
            try {
                Log.i("TCP", "NetworkManagerThread");
                ServerSocket socServer = new ServerSocket(8800);
                Socket socClient = null;
                BufferedReader br = null;
                PrintStream ps = null;
                String s = null;

                while (true) {
                    socClient = socServer.accept();
                    Log.i("TCP", "" + socClient.getInetAddress().getHostAddress());

                    br = new BufferedReader(new InputStreamReader(socClient.getInputStream()));
                    ps = new PrintStream(socClient.getOutputStream());

                    s = br.readLine();
                    Log.i("TCP", s);
                    ps.println("OKAY");

                    br.close();
                    ps.close();
                    socClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

       @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    imgCar.setImageResource(R.drawable.img_car_all_locked);
                    myClientTask = new NetworkTask(
                            ip, Integer.parseInt(port)
                    );
                    myClientTask.execute();

                    btn_index = 0;
                    return true;
                case R.id.navigation_runapp:
                    imgCar.setImageResource(R.drawable.img_car_all_locked);
                    myClientTask = new NetworkTask(
                            ip, Integer.parseInt(port)
                    );
                    myClientTask.execute();

                    btn_index = 1;
                    return true;
                case R.id.navigation_dashboard:
                    imgCar.setImageResource(R.drawable.img_car_top_left_unlock);
                    myClientTask = new NetworkTask(
                            ip, Integer.parseInt(port)
                    );
                    myClientTask.execute();

                    btn_index = 2;
                    return true;
                case R.id.navigation_notifications:
                    imgCar.setImageResource(R.drawable.img_car_all_locked);
                    myClientTask = new NetworkTask(
                            ip, Integer.parseInt(port)
                    );
                    myClientTask.execute();

                    btn_index = 3;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NetworkManagerThread().start();

/*        myClientTask = new NetworkTask(
                ip, Integer.parseInt(port)
        );
        myClientTask.execute();*/

        imgCar = (ImageView)findViewById(R.id.imageview1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
       // JSONObject jsonObject = new JSONObject();
        String response;
        InputStream inputStream;
        OutputStream outputStream = null;
        PrintStream printStream = null;
        BufferedReader inFromServer = null;

        NetworkTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

       /* public void sendMessage(String msg){
            Log.i("a", "sendMessage: " + msg);
            try {
                printStream.println(msg);
            } catch (NullPointerException e) {
                Log.i("a", "null");
            } catch (Exception e) {
                Log.i("a", "e");
            }
        }*/
        private void sendMessage(int index){
            switch (index) {
                case 0:
                    //myClientTask.sendObject("User_Device", "PowerOn");
                    printStream.println("EngineStart"); //open command 전송
                    break;
                case 1:
                    // myClientTask.sendObject("User_Device", "Open");
                    printStream.println("RunApp"); //open command 전송
                    break;
                case 2:
                    //myClientTask.sendObject("User_Device", "Close");
                    printStream.println("Open"); //open command 전송
                    break;
                default:
                    // myClientTask.sendObject("User_Device", "EngineStart");
                    printStream.println("Close"); //open command 전송

            }

        }

       /* private void sendObject(String name, String value) {
            try {
                Log.i(name, value);
                jsonObject.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        @Override
        protected Void doInBackground(Void... arg0) {
            int code = 0;

            try {
                Log.i("TCP", "addr - " + dstAddress + " port - " + dstPort);
                Socket socket = new Socket(dstAddress, dstPort);
                Log.i("TCP", "Server Connected");

                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    printStream = new PrintStream(outputStream);
                    inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    //sendMessage(btn_index);
                    printStream.println("open"); //open command 전송

                    response = inFromServer.readLine(); //user authentication
                    Log.i("TCP", "Message From Server : " + response);

                    try {
                        code = Integer.parseInt(response);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    code += 1;
                    printStream.println(String.valueOf(code));
                    Log.i("TCP", "Send Authentication Message to Server : " + code);

                    response = inFromServer.readLine(); //final response received
                    Log.i("TCP", "Core Code Message From Server : " + response);

                    if(response.equals("Code Requesting")){
                        printStream.println("Core Code");
                    }
                    Log.i("TCP", "Code Send Completed");

                    printStream.close();
                } catch (IOException e) {
                    Log.e("TCP", "Connecting Failed");
                    e.printStackTrace();
                }finally {
                    try{
                        if(printStream != null){
                            printStream.close();
                        }
                        if(inFromServer != null){
                            inFromServer.close();
                        }
                    }catch (IOException e){
                    }
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
