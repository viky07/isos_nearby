package com.sep.isos_hc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < 23) {
                    Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (checkPermission()) {
                        Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        requestPermission();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                {
                        READ_CONTACTS,
                        READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

//                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean ReceivePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean AclPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AflPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean SawPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
//                    boolean GaPermission = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (ReadContactsPermission && ReadPhoneStatePermission && AclPermission && AflPermission && SawPermission) {
                        Toast.makeText(SplashActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

//        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
//        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int SeventhPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        int EighthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SeventhPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

}
