package com.example.kyungrikim.net2;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private TextView txtResponse;
    private EditText edtTextAddress, edtTextPort;
    private Button btnConnect, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTextAddress = (EditText) findViewById(R.id.address);
        edtTextPort = (EditText) findViewById(R.id.port);
        btnConnect = (Button) findViewById(R.id.connect);
        btnClear = (Button) findViewById(R.id.clear);
        txtResponse = (TextView) findViewById(R.id.response);

        btnConnect.setOnClickListener(buttonConnectOnClickListener);
        btnClear.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                txtResponse.setText("");
            }
        });
    }

    // Ŭ���̺�Ʈ ������
    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        public void onClick(View arg0) {
            NetworkTask myClientTask = new NetworkTask(
                    edtTextAddress.getText().toString(),
                    Integer.parseInt(edtTextPort.getText().toString())
            );
            myClientTask.execute();
        }
    };

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response;
        JSONObject jsonObject = new JSONObject();
        NetworkTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }


        private void sendObject(){

            try{
                jsonObject.put("name", "wdkang");
                jsonObject.put("company", "acanet");
                jsonObject.put("age", "26");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Log.e("TCP","server connecting~~!");
                Socket socket = new Socket(dstAddress, dstPort);
                sendObject();
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
                    OutputStream outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(jsonObject);
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

        @Override
        protected void onPostExecute(Void result) {
            txtResponse.setText(response);

            //txtResponse.setText("a");
            super.onPostExecute(result);
        }

    }

}
