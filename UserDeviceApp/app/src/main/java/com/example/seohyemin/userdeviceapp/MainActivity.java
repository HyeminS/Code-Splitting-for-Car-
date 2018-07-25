package com.example.seohyemin.userdeviceapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
//import java.util.Base64;
import android.util.Base64;
import android.widget.Toast;

import static android.app.PendingIntent.getActivity;


@TargetApi(Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private String ip = "192.168.193.108";
    private String ip = "192.168.0.8";
    private String port = "7880";
    private ImageView imgCar;
    NetworkTask myClientTask;
    Button btn1;
    Button btn2;
    TextView tv1;
    int btn_index = 0;
    java.util.Base64.Encoder encoder;
    byte[] encodeByte;

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

        imgCar = (ImageView)findViewById(R.id.imageview1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        readFromAsset();

    }

    public void readFromAsset(){
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        StringBuffer sb = new StringBuffer();
        String data = "";
        String sdPath = null;
        sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        try {
            is = am.open("core_code.py");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while((data = br.readLine())!= null){
                sb.append(data);
                sb.append("\n");

                if(data == null)
                    break;
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(is != null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File dir = new File(sdPath + "/myDir");
        if(!dir.isDirectory())
            dir.mkdir();

        File file = new File(sdPath + "/mydir/custom.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void checkPermission(){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Log.i("Downloadg","외부 저장소 사용을 위해 읽기/쓰기 필요");
                }

                requestPermissions(new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            }else{
                Log.i("Downloadg","권한승인됨");
            }
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        JSONObject jsonObject = new JSONObject();
        String response;
        InputStream inputStream;
        OutputStream outputStream = null;
        PrintStream printStream = null;
        BufferedReader inFromServer = null;

        NetworkTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

        public String encode_LoginReq(String src) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            DataOutputStream convert = new DataOutputStream(buf);

            try {
                byte[] buffer = Arrays.copyOf(src.getBytes(), src.length());
                convert.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buf.toString();
        }


        private void sendMessage(int index) {
            AssetManager am = getResources().getAssets();
            InputStream is = null;
            StringBuffer sb = new StringBuffer();
            String encodeText = "";
            String data = "";


            try {
                is = am.open("core_code.py");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                while((data = br.readLine())!= null){
                    sb.append(data);
                    sb.append("\n");
                    if(data == null)
                        break;
                }

                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            encodeText = encode_LoginReq(sb.toString());

            switch (index) {
                case 0:
                    myClientTask.sendObject("User_Device", "EngineStart");
                    printStream.println(jsonObject); //open command 전송
                    break;
                case 1:
                     myClientTask.sendObject("User_Device", "RunApp");
                    printStream.println(jsonObject); //open command 전송
                    break;
                case 2:
                    myClientTask.sendObject("User_Device", encodeText);
                    printStream.println(jsonObject); //open command 전송
                    break;
                default:
                     myClientTask.sendObject("User_Device", "Close");
                    printStream.println(jsonObject); //open command 전송

            }

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

            try {
                Log.i("TCP", "addr - " + dstAddress + " port - " + dstPort);
                Socket socket = new Socket(dstAddress, dstPort);
                Log.i("TCP", "Server Connected");

                try {
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    printStream = new PrintStream(outputStream);
                    inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    Log.i("TCP", "S1");

                    sendMessage(btn_index);
                    Log.i("TCP", "S2");

                    //printStream.println("open"); //open command 전송

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
