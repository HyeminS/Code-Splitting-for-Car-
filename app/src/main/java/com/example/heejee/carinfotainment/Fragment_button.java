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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancState){
        view = inflater.inflate(R.layout.fragment_button, container,false);



        final ImageButton MusicButton = (ImageButton)view.findViewById(R.id.MusicButton);
        MusicButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                startActivity(intent);
            }
        }) ;
        final ImageButton MapButton = (ImageButton)view.findViewById(R.id.MapButton);
        MapButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse ("geo: 38.00, 35.03");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }) ;
        final ImageButton SettingButton = (ImageButton)view.findViewById(R.id.SettingButton);
        SettingButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                new SettingTask().execute();
            }
        }) ;
        final ImageButton WeatherButton = (ImageButton)view.findViewById(R.id.WeatherButton);
        WeatherButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://weather.naver.com/")));
            }
        }) ;
        final ImageButton GameButton = (ImageButton)view.findViewById(R.id.GameButton);
        GameButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://norara.dreamx.com/")));
            }
        }) ;
        final ImageButton YoutubeButton = (ImageButton)view.findViewById(R.id.YoutubeButton);
        YoutubeButton.setOnClickListener(new ImageButton.OnClickListener() {
            Context context;
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=pNLbxPXOYBI")));

            }
        }) ;
        final ImageButton ChattingButton = (ImageButton)view.findViewById(R.id.ChattingButton);
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
        }) ;
        final ImageButton CalenderButton = (ImageButton)view.findViewById(R.id.CalenderButton);
        CalenderButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("content://com.android.calendar/time/"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }) ;
        return view;
    }

}
