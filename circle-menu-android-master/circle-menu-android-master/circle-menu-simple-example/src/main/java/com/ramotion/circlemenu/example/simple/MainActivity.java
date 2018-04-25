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
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {
    private String ip = "10.19.6.1";
    private String port = "8480";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                switch (index){
                    case 0:
                        myClientTask.sendObject("User_Device","PowerOn");
                        break;
                    case 1:
                        myClientTask.sendObject("User_Device","Open");
                        break;
                    case 2:
                        myClientTask.sendObject("User_Device","Close");
                        break;
                    case 3:
                        myClientTask.sendObject("User_Device","EngineStart");
                        break;
                    default :
                        myClientTask.sendObject("User_Device","EngineStop");

                }
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);

            }

            /*@Override
            public boolean onButtonLongClick(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonLongClick| index: " + index);
              Log.i("tag", "Open button clicked!");
                return true;
            }

            @Override
            public void onButtonLongClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonLongClickAnimationStart| index: " + index);
               // Toast toast = Toast.makeText(getApplicationContext(), "출력할 문자열_클로즈", Toast.LENGTH_SHORT); toast.show();
            }

            @Override
            public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonLongClickAnimationEnd| index: " + index);
            }*/
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

        private void sendObject(String name, String value){
            try{
                Log.i(name,value);
                jsonObject.put(name,value);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Log.e("TCP","server connecting~~!");
                Socket socket = new Socket(dstAddress, dstPort);
                //sendObject();
               /* InputStream inputStream = socket.getInputStream();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                        1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                response = byteArrayOutputStream.toString("UTF-8");
*/

                try{
                   /* OutputStream outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonObject);
                    printStream.close();*/
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    BufferedReader inFromServer =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    printStream.print(jsonObject);
                    response = inFromServer.readLine();
                    Log.i("kk", response);
                    printStream.close();
                }
                catch (IOException e)
                {
                    Log.e("TCP","don't send message!");
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
