package com.ramotion.circlemenu.example.simple;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ramotion.circlemenu.CircleMenuView;

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
    private class NetworkManagerThread extends Thread {
        public void run() {
            try {
                Log.i("TCP", "NetworkManagerThread");

                ServerSocket socServer = new ServerSocket(8888);
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
    private String ip = "192.168.137.62";
    private String port = "7880";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NetworkManagerThread().start();

        final CircleMenuView menu = findViewById(R.id.circle_menu);
        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationStart");

            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationStart");

            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);

                NetworkTask myClientTask = new NetworkTask(
                        ip, Integer.parseInt(port)
                );
                myClientTask.execute();

                JSONObject jsonObject = new JSONObject();
                switch (index) {
                    case 0:
                        myClientTask.sendObject("User_Device", "PowerOn");
                        break;
                    case 1:
                        myClientTask.sendObject("User_Device", "Open");
                        break;
                    case 2:
                        myClientTask.sendObject("User_Device", "Close");
                        break;
                    case 3:
                        myClientTask.sendObject("User_Device", "EngineStart");
                        break;
                    default:
                        myClientTask.sendObject("User_Device", "EngineStop");

                }
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);

            }

        });


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

        private void sendObject(String name, String value) {
            try {
                Log.i(name, value);
                jsonObject.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int code = 0;
            InputStream inputStream;
            OutputStream outputStream = null;
            PrintStream printStream = null;
            BufferedReader inFromServer = null;

            try {
                Socket socket = new Socket(dstAddress, dstPort);
                Log.i("TCP", "Server Connected");

                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    printStream = new PrintStream(outputStream);
                    inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

                   /* response = inFromServer.readLine(); //final response received
                    Log.i("TCP", "Authentication Message From Server : " + response);
*/
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
                        //
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
