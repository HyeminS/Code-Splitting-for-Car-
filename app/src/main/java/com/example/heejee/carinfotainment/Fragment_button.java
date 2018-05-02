package com.example.heejee.carinfotainment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by heejee on 2018-04-03.
 */

public class Fragment_button extends Fragment {
    class SettingTask extends AsyncTask<Void, String, Void> {
        private ProgressDialog mDlg = new ProgressDialog(Fragment_button.this.getContext());
        private int mId = 0;


        @Override
        protected void onPreExecute() {
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.setMessage("first message");
            mDlg.setCancelable(false);
            mDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //do nothing
                }
            });
            mDlg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDlg.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mDlg.setMessage(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress("second message");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //do nothing
            }

            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

            return null;
        }


    }

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancState) {
        view = inflater.inflate(R.layout.fragment_button, container, false);

        final ImageButton MusicButton = (ImageButton) view.findViewById(R.id.MusicButton);
        MusicButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                startActivity(intent);
            }
        });
        final ImageButton MapButton = (ImageButton) view.findViewById(R.id.MapButton);
        MapButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo: 38.00, 35.03");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        final ImageButton SettingButton = (ImageButton) view.findViewById(R.id.SettingButton);
        SettingButton.setOnClickListener(new ImageButton.OnClickListener() {
            private String ip = "192.168.0.4";
            private String port = "8989";
            @Override
            public void onClick(View view) {
                Fragment_button.NetworkTask CodeSplitting = new Fragment_button.NetworkTask(ip, Integer.parseInt(port));
                CodeSplitting.execute();

//                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                new SettingTask().execute();
            }
        });
        final ImageButton WeatherButton = (ImageButton) view.findViewById(R.id.WeatherButton);
        WeatherButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://weather.naver.com/")));
            }
        });
        final ImageButton GameButton = (ImageButton) view.findViewById(R.id.GameButton);
        GameButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://norara.dreamx.com/")));
            }
        });
        final ImageButton YoutubeButton = (ImageButton) view.findViewById(R.id.YoutubeButton);
        YoutubeButton.setOnClickListener(new ImageButton.OnClickListener() {
            Context context;

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=pNLbxPXOYBI")));

            }
        });
        final ImageButton ChattingButton = (ImageButton) view.findViewById(R.id.ChattingButton);
        ChattingButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Emailing link");
                intent.putExtra(Intent.EXTRA_TEXT, "Link is \n" +
                        "This is the body of hte message");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        final ImageButton CalenderButton = (ImageButton) view.findViewById(R.id.CalenderButton);
        CalenderButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("content://com.android.calendar/time/"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
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
                    if (response.equals("hihi")) {
                        Log.i("hehe", response);
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
