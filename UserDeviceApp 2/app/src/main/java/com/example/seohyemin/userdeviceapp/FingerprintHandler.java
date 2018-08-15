package com.example.seohyemin.userdeviceapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by whit3hawks on 11/16/16.
 */
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private getMsg gm;

    public interface getMsg {
        void onAuthSucess();
        void onAuthFail();
    }

    private Context context;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;

        if(context instanceof getMsg){
            this.gm = (getMsg) context;
        }else{
            //this.gm = (getMsg) context;
            //throw new ClassCastException("FingerprintHandler : context must implement LoginListener!");
        }
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        if(gm !=null){
            gm.onAuthSucess();
        }
        Intent intent = new Intent(context, HomeActivity.class);
       // intent.putExtra("value","setPowerOn");
       // Log.i("FingerHandler", intent.getStringExtra("value"));

      ((Activity) context).finish();
      context.startActivity(intent);

    }

    private void update(String e){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
    }

}
