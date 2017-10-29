package com.sep.isos_hc;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sep.isos_hc.viewdrag.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {
    private static final String TAG = "MapsActivity";

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private RadioGroup radioGroup;
    private RadioButton hosp, bbank;
    Button submit;
    Spinner radius;
    String radius_val = "";
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    protected LocationRequest locationRequest;
    double latitude, longitude;
    private LinkedList<HospitalModel> mListLocation;
    int REQUEST_CHECK_SETTINGS = 100;
    GPSTracker gpsTracker;
    Set<HospitalModel> get_hosp_loc_set = new LinkedHashSet<>();
    LinkedList<HospitalModel> get_hosp_loc_list = null;
    String[] sp;
    LinkedList<HospitalModel> infoValues = null;
    LinearLayout dragView, addr_info_layout, lout_id6, lout_id7, lout_id8, lout_id9, lout_id10,lout_id11;
    TextView lable1, lable2, lable3, lable4, lable5, lable6, lable7, lable8, lable9, lable10,lable11, txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10,txt11, addr_id, nrby_list_txt,dist_txt;
    String check_types = "";
    ListView lv;
    MyAdaptor myAdaptor;
    String dir_name="",dir_lat="",dir_lng="";
    Button direction;
    public final static int REQUEST_CODE = 10101;
    ProgressDialog loading;
    View SplitLine_hor1;
//    FrameLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

//        try {
//            String simTwoNumber, simOneNumber;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//                List<SubscriptionInfo> subInfoList = null;
//                subInfoList = subManager.getActiveSubscriptionInfoList();
//                if (subInfoList != null && subInfoList.size() > 0) {
//                    switch (subInfoList.size()) {
//                        case 2:
//                            simTwoNumber = subInfoList.get(1).getNumber();
//                        case 1:
//                            simOneNumber = subInfoList.get(0).getNumber();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        dragView = (LinearLayout) findViewById(R.id.dragView);
        addr_info_layout = (LinearLayout) findViewById(R.id.addr_info);
        lout_id6 = (LinearLayout) findViewById(R.id.lout_id6);
        lout_id7 = (LinearLayout) findViewById(R.id.lout_id7);
        lout_id8 = (LinearLayout) findViewById(R.id.lout_id8);
        lout_id9 = (LinearLayout) findViewById(R.id.lout_id9);
        lout_id10 = (LinearLayout) findViewById(R.id.lout_id10);
        lout_id11 = (LinearLayout) findViewById(R.id.lout_id11);

//        progressBar=(FrameLayout) findViewById(R.id.progressBarHolder);

        SplitLine_hor1 = (View) findViewById(R.id.SplitLine_hor1);

        direction = (Button) findViewById(R.id.dir);

        addr_id = (TextView) findViewById(R.id.addr_id);
//        nrby_list_txt = (TextView) findViewById(R.id.nerby_list_txt);

        dist_txt = (TextView) findViewById(R.id.dist);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);

        txt6 = (TextView) findViewById(R.id.txt6);
        txt7 = (TextView) findViewById(R.id.txt7);
        txt8 = (TextView) findViewById(R.id.txt8);
        txt9 = (TextView) findViewById(R.id.txt9);
        txt10 = (TextView) findViewById(R.id.txt10);
        txt11 = (TextView) findViewById(R.id.txt11);


        lable1 = (TextView) findViewById(R.id.lable1);
        lable2 = (TextView) findViewById(R.id.lable2);
        lable3 = (TextView) findViewById(R.id.lable3);
        lable4 = (TextView) findViewById(R.id.lable4);
        lable5 = (TextView) findViewById(R.id.lable5);

        lable6 = (TextView) findViewById(R.id.lable6);
        lable7 = (TextView) findViewById(R.id.lable7);
        lable8 = (TextView) findViewById(R.id.lable8);
        lable9 = (TextView) findViewById(R.id.lable9);
        lable10 = (TextView) findViewById(R.id.lable10);
        lable11 = (TextView) findViewById(R.id.lable11);


//        lv = (ListView) findViewById(R.id.list);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MapsActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
//            }
//        });

        addr_id.setText("Product By Swift ePublishing");
        addr_id.setGravity(Gravity.CENTER);

        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.hosp) {
                    if(check_types.equals("bbank")){
                        addr_info_layout.setVisibility(View.GONE);
                    }
                    check_types = "hosp";
//                    Toast.makeText(getApplicationContext(), "choice: hosp", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.bbank) {
                    if(check_types.equals("hosp")){
                        addr_info_layout.setVisibility(View.GONE);
                    }
                    check_types = "bbank";
//                    Toast.makeText(getApplicationContext(), "choice: blood", Toast.LENGTH_SHORT).show();
                }
            }

        });

        hosp = (RadioButton) findViewById(R.id.hosp);
        bbank = (RadioButton) findViewById(R.id.bbank);

        radius = (Spinner) findViewById(R.id.radius);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.radi_types, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(staticAdapter);

        radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                radius_val = radius.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), radius_val, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gpsTracker = new GPSTracker(getApplicationContext());

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean checkNet=checkConnection();
                gpsTracker = new GPSTracker(getApplicationContext());
                if(checkNet==true) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == hosp.getId()) {
                        dir_name = "";
                        dir_lat = "";
                        dir_lng = "";
                        get_nearby_places("hospital", String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), radius_val, check_types);
                    } else if (selectedId == bbank.getId()) {
                        dir_name = "";
                        dir_lat = "";
                        dir_lng = "";
                        get_nearby_places("bbank", String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), radius_val, check_types);
                    }else{
                        Toast.makeText(getApplicationContext(), "Please select anyone type!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
                    PassDataToDir(dir_name,dir_lat,dir_lng);
                } else {
                    if (!checkDrawOverlayPermission()) {

                    }else{
                        PassDataToDir(dir_name,dir_lat,dir_lng);
                    }
                }
            }
        });

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

//        TextView t = (TextView) findViewById(R.id.name);
//        t.setText(Html.fromHtml(getString(R.string.hello)));
//        Button f = (Button) findViewById(R.id.follow);
//        f.setText(Html.fromHtml(getString(R.string.follow)));
//        f.setMovementMethod(LinkMovementMethod.getInstance());
//        f.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
//                startActivity(i);
//            }
//        });
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_info, null);
                TextView tvTit = (TextView) v.findViewById(R.id.title);
                tvTit.setText(marker.getTitle());
                try {
                    String title = marker.getTitle();
                    addr_info_layout.setVisibility(View.VISIBLE);
                    if (check_types.equals("hosp")) {
                        lout_id6.setVisibility(View.GONE);
                        lout_id7.setVisibility(View.GONE);
                        lout_id8.setVisibility(View.GONE);
                        lout_id9.setVisibility(View.GONE);
                        lout_id10.setVisibility(View.GONE);
                        lout_id11.setVisibility(View.GONE);
                        lable1.setText("Hospital Name : ");
                        lable2.setText("Hospital NinNumber : ");
                        lable3.setText("Hospital Location Type : ");
                        lable4.setText("Hospital Sub District : ");
                        lable5.setText("Hospital Distict : ");
                    } else if (check_types.equals("bbank")) {
                        lout_id6.setVisibility(View.VISIBLE);
                        lout_id7.setVisibility(View.VISIBLE);
                        lout_id8.setVisibility(View.VISIBLE);
                        lout_id9.setVisibility(View.VISIBLE);
                        lout_id10.setVisibility(View.VISIBLE);
                        lout_id11.setVisibility(View.VISIBLE);
                        lable1.setText("Blood Bank Name : ");
                        lable2.setText("Blood Bank Address : ");
                        lable3.setText("Blood Bank District : ");
                        lable4.setText("Blood Bank Pincode : ");
                        lable5.setText("Blood Bank Cont.No : ");
                        lable6.setText("Blood Bank Mobile : ");
                        lable7.setText("Blood Bank HelpLine : ");
                        lable8.setText("Blood Bank Fax : ");
                        lable9.setText("Blood Bank Email : ");
                        lable10.setText("Blood Bank Website : ");
                        lable11.setText("Blood Bank Category : ");
                    }

                    LinkedList<HospitalModel> setInfo = compareVal(get_hosp_loc_list, title);

                    for (HospitalModel hmod : setInfo) {
                        if (check_types.equals("hosp")) {
                            txt1.setText(hmod.getName());
                            txt2.setText(hmod.getNinnum());
                            txt3.setText(hmod.getLoc_type());
                            txt4.setText(hmod.getSub_dist());
                            txt5.setText(hmod.getDist());
                            dir_name = hmod.getName();
                            dir_lat = String.valueOf(hmod.getLat());
                            dir_lng = String.valueOf(hmod.getLng());
                            double dst = Double.parseDouble(hmod.getDistance());
                            int mydst = (int) dst;
                            dist_txt.setText("Distance : "+String.valueOf(mydst) +" KMs");
                        } else if (check_types.equals("bbank")) {
                            txt1.setText(hmod.getName());
                            txt2.setText(hmod.getAddress());
                            txt3.setText(hmod.getDist());
                            txt4.setText(hmod.getPincode());
                            txt5.setText(hmod.getCont_num());
                            txt6.setText(hmod.getMob_num());
                            txt7.setText(hmod.getHelpline());
                            txt8.setText(hmod.getFax());
                            txt9.setText(hmod.getEmail());
                            txt10.setText(hmod.getWebsite());
                            txt11.setText(hmod.getCategory());
                            dir_name = hmod.getName();
                            dir_lat = String.valueOf(hmod.getLat());
                            dir_lng = String.valueOf(hmod.getLng());
                            double dst = Double.parseDouble(hmod.getDistance());
                            int mydst = (int) dst;
                            dist_txt.setText("Distance : "+String.valueOf(mydst) +" KMs");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return v;
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

    private void addMarker() {
        mMap.clear();
//        nrby_list_txt.setVisibility(View.VISIBLE);

        if (check_types.equals("hosp")) {
            addr_id.setText("Hospital Details");
//            nrby_list_txt.setText("NearBy Hospital List : ");
        } else if (check_types.equals("bbank")) {
            addr_id.setText("Blood Bank Details");
//            nrby_list_txt.setText("NearBy Blood Bank List : ");
        }

        Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).radius(Integer.parseInt(radius_val)*1000).strokeColor(android.R.color.transparent));
        circle.setVisible(true);
        getZoomLevel(circle);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(getZoomLevel(circle)), 2000, null);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(f_lat, f_long), 16));

//        lv.setVisibility(View.VISIBLE);
//        myAdaptor = new MyAdaptor(getApplicationContext(), R.layout.info_layout, get_hosp_loc_list, check_types);
//        lv.setAdapter(myAdaptor);
        for (HospitalModel hosp : get_hosp_loc_list) {
            if (check_types.equals("hosp")) {
                viewMarkerDetails(mMap, hosp.getName(), "", hosp.getLat(), hosp.getLng(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
            } else if (check_types.equals("bbank")) {
                viewMarkerDetails(mMap, hosp.getName(), "", hosp.getLat(), hosp.getLng(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
            }
//            }
        }

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
                finish();
            }

        }
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT < 23) {
                PassDataToDir(dir_name,dir_lat,dir_lng);
            } else {
                if (!Settings.canDrawOverlays(getApplication())) {
                    Toast.makeText(getApplicationContext(), "Overlay Permission is cancelled!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Overlay Permission is granted!", Toast.LENGTH_LONG).show();
                    PassDataToDir(dir_name,dir_lat,dir_lng);
                }
            }

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void viewMarkerDetails(GoogleMap gm, final String name, final String loc_type, double f_lat, double f_long, double z_lat, double z_long) {
        LatLng onstartLoc = new LatLng(f_lat, f_long);
        View viewmarker = ((LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView tv_marker_text = (TextView) viewmarker.findViewById(R.id.num_txt);
        tv_marker_text.setText(name + "\n" + loc_type);
        ImageView victim_marker = (ImageView) viewmarker.findViewById(R.id.vict_mark);
        victim_marker.setImageResource(R.drawable.redmarker);
        Marker marker = gm.addMarker(new MarkerOptions()
                .position(onstartLoc)
                .title(name)
//                .snippet(loc_type)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, viewmarker))));
//            gm.moveCamera(CameraUpdateFactory.newLatLng(mark_loc));


//        gm.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(f_lat, f_long), 16));


//            marker.showInfoWindow();

    }

    private Bitmap createDrawableFromView(Activity actvi, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        actvi.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null && (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public boolean checkConnection() {
        boolean isConnect = false;
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivitymanager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            isConnect = true;
        } else {
            isConnect = false;
        }
        return isConnect;
    }

    private void get_nearby_places(final String type, final String lati, final String longi, final String h_rad, final String c_typ) {
        loading = ProgressDialog.show(this, "", "Loading...", false, false);
        try {
            String path = "http://isos.swiftepublishing.com/isos_nearby/isos_nearby.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, path,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            response = response.trim();
                            if (c_typ.equals("hosp")) {
                                showJSON(response, c_typ);
                            } else if (c_typ.equals("bbank")) {
                                showJSON(response, c_typ);
                            }else{
                                if(loading!=null){
                                    loading.cancel();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                            if(loading!=null){
                                loading.cancel();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", type);
                    params.put("radius", h_rad);
                    params.put("cur_lat", lati);
                    params.put("cur_long", longi);
                    return params;
                }

            };
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private void showJSON(String response, String check) {
        get_hosp_loc_set.clear();
        JSONArray jsonMainNode = null;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (check.equals("hosp")) {
                jsonMainNode = jsonResponse.optJSONArray("nearby_log");
            } else if (check.equals("bbank")) {
                jsonMainNode = jsonResponse.optJSONArray("nrby_bbank_log");
            }
            if (jsonMainNode != null) {
                int j = jsonMainNode.length();
                if (j != 0) {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        if (check.equals("hosp")) {
                            String hosp_name = jsonChildNode.optString("isosnrby_hos_name");
                            String hosp_lat = jsonChildNode.optString("isosnrby_hos_lat");
                            String hosp_long = jsonChildNode.optString("isosnrby_hos_long");
                            String hosp_ninnum = jsonChildNode.optString("isosnrby_hos_ninnumber");
                            String hosp_loctype = jsonChildNode.optString("isosnrby_hos_loctype");
                            String hosp_sub_dis = jsonChildNode.optString("isosnrby_hos_sub_dis");
                            String hosp_dis = jsonChildNode.optString("isosnrby_hos_dis");
                            String hosp_dtance = jsonChildNode.optString("isosnrby_hos_distance");
                            get_hosp_loc_set.add(new HospitalModel(hosp_name, Double.parseDouble(hosp_lat), Double.parseDouble(hosp_long), hosp_ninnum, hosp_loctype, hosp_sub_dis, hosp_dis, hosp_dtance, "", "", "", "", "", "", "", "", ""));
                        } else if (check.equals("bbank")) {
                            String bbank_name = jsonChildNode.optString("isosnrby_bb_name");
                            String bb_dist = jsonChildNode.optString("isosnrby_bb_dis");
                            String bb_addr = jsonChildNode.optString("isosnrby_bb_address");
                            String bb_pcode = jsonChildNode.optString("isosnrby_bb_pincode");
                            String bb_cont = jsonChildNode.optString("isosnrby_bb_contno");
                            String bb_mobile = jsonChildNode.optString("isosnrby_bb_mobile");
                            String bb_help = jsonChildNode.optString("isosnrby_bb_helpline");
                            String bb_fax = jsonChildNode.optString("isosnrby_bb_fax");
                            String bb_email = jsonChildNode.optString("isosnrby_bb_email");
                            String bb_web = jsonChildNode.optString("isosnrby_bb_website");
                            String bb_cate = jsonChildNode.optString("isosnrby_bb_category");
                            String bb_lat = jsonChildNode.optString("isosnrby_bb_lat");
                            String bb_lng = jsonChildNode.optString("isosnrby_bb_long");
                            String bb_distance = jsonChildNode.optString("isosnrby_bb_distance");
                            get_hosp_loc_set.add(new HospitalModel(bbank_name, Double.parseDouble(bb_lat), Double.parseDouble(bb_lng), "", "", "", bb_dist, bb_distance, bb_addr, bb_cont, bb_mobile, bb_pcode, bb_help, bb_fax, bb_email, bb_web, bb_cate));
                        }
                    }
                    get_hosp_loc_list = new LinkedList<>(get_hosp_loc_set);
                    addMarker();
                    if(loading!=null){
                        loading.cancel();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No places available!", Toast.LENGTH_LONG).show();
                    mMap.clear();
                    addr_info_layout.setVisibility(View.GONE);
//                    nrby_list_txt.setVisibility(View.GONE);
                    if(get_hosp_loc_list!=null){
                        get_hosp_loc_list.clear();
                    }
//                    lv.setVisibility(View.GONE);
                    addr_id.setText("Product By Swift ePublishing");
                    if(loading!=null){
                        loading.cancel();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "No places available!", Toast.LENGTH_LONG).show();
                mMap.clear();
                addr_info_layout.setVisibility(View.GONE);
//                nrby_list_txt.setVisibility(View.GONE);
                get_hosp_loc_list.clear();
//                lv.setVisibility(View.GONE);
                addr_id.setText("Product By Swift ePublishing");
                if(loading!=null){
                    loading.cancel();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private LinkedList<HospitalModel> compareVal(LinkedList<HospitalModel> m_tit, final String val) {
        infoValues = new LinkedList<>();
        for (Iterator<HospitalModel> iterator = m_tit.iterator(); iterator.hasNext(); ) {
            HospitalModel hm = iterator.next();
            if (hm.getName().equals(val)) {
                infoValues.add(hm);
            }
        }
        return infoValues;
    }

    public class MyAdaptor extends ArrayAdapter<HospitalModel> {
        private SparseBooleanArray mSelectedItemsIds;
        LayoutInflater inflater;
        LinkedList<HospitalModel> nearDataList;
        String ld_dat_type_list = "";

        public MyAdaptor(Context context, int resourceId, LinkedList<HospitalModel> lists, String ld_lidt) {
            super(context, resourceId, lists);
            mSelectedItemsIds = new SparseBooleanArray();
            nearDataList = lists;
            ld_dat_type_list = ld_lidt;
            inflater = LayoutInflater.from(context);
        }

        private class ViewHolder {
            TextView list_lable1, list_lable2, list_lable3, list_lable4, list_lable5, list_lable6, list_lable7, list_lable8, list_lable9, list_lable10,list_lable11, list_txt1, list_txt2, list_txt3, list_txt4, list_txt5, list_txt6, list_txt7, list_txt8, list_txt9, list_txt10, list_txt11;
            LinearLayout list_lout_id6, list_lout_id7, list_lout_id8, list_lout_id9, list_lout_id10, list_lout_id11;
            TextView count_txt;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.info_layout, null);
                holder.count_txt = (TextView) view.findViewById(R.id.count_txt);
                holder.list_txt1 = (TextView) view.findViewById(R.id.list_txt1);
                holder.list_txt2 = (TextView) view.findViewById(R.id.list_txt2);
                holder.list_txt3 = (TextView) view.findViewById(R.id.list_txt3);
                holder.list_txt4 = (TextView) view.findViewById(R.id.list_txt4);
                holder.list_txt5 = (TextView) view.findViewById(R.id.list_txt5);
                holder.list_txt6 = (TextView) view.findViewById(R.id.list_txt6);
                holder.list_txt7 = (TextView) view.findViewById(R.id.list_txt7);
                holder.list_txt8 = (TextView) view.findViewById(R.id.list_txt8);
                holder.list_txt9 = (TextView) view.findViewById(R.id.list_txt9);
                holder.list_txt10 = (TextView) view.findViewById(R.id.list_txt10);
                holder.list_txt11 = (TextView) view.findViewById(R.id.list_txt11);

                holder.list_lable1 = (TextView) view.findViewById(R.id.list_lable1);
                holder.list_lable2 = (TextView) view.findViewById(R.id.list_lable2);
                holder.list_lable3 = (TextView) view.findViewById(R.id.list_lable3);
                holder.list_lable4 = (TextView) view.findViewById(R.id.list_lable4);
                holder.list_lable5 = (TextView) view.findViewById(R.id.list_lable5);
                holder.list_lable6 = (TextView) view.findViewById(R.id.list_lable6);
                holder.list_lable7 = (TextView) view.findViewById(R.id.list_lable7);
                holder.list_lable8 = (TextView) view.findViewById(R.id.list_lable8);
                holder.list_lable9 = (TextView) view.findViewById(R.id.list_lable9);
                holder.list_lable10 = (TextView) view.findViewById(R.id.list_lable10);
                holder.list_lable11 = (TextView) view.findViewById(R.id.list_lable11);
                holder.list_lout_id6 = (LinearLayout) view.findViewById(R.id.list_lout_id6);
                holder.list_lout_id7 = (LinearLayout) view.findViewById(R.id.list_lout_id7);
                holder.list_lout_id8 = (LinearLayout) view.findViewById(R.id.list_lout_id8);
                holder.list_lout_id9 = (LinearLayout) view.findViewById(R.id.list_lout_id9);
                holder.list_lout_id10 = (LinearLayout) view.findViewById(R.id.list_lout_id10);
                holder.list_lout_id11 = (LinearLayout) view.findViewById(R.id.list_lout_id11);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            HospitalModel hmodl = nearDataList.get(position);
            int count = position + 1;
            if (ld_dat_type_list.equals("hosp")) {
                holder.count_txt.setText(String.valueOf(count));
                holder.list_lable1.setText("Hospital Name : ");
                holder.list_lable2.setText("Hospital NinNumber : ");
                holder.list_lable3.setText("Hospital Location Type : ");
                holder.list_lable4.setText("Hospital Sub District : ");
                holder.list_lable5.setText("Hospital Distict : ");
                holder.list_lout_id6.setVisibility(View.GONE);
                holder.list_lout_id7.setVisibility(View.GONE);
                holder.list_lout_id8.setVisibility(View.GONE);
                holder.list_lout_id9.setVisibility(View.GONE);
                holder.list_lout_id10.setVisibility(View.GONE);
                holder.list_lout_id11.setVisibility(View.GONE);
                holder.list_txt1.setText(hmodl.getName());
                holder.list_txt2.setText(hmodl.getNinnum());
                holder.list_txt3.setText(hmodl.getLoc_type());
                holder.list_txt4.setText(hmodl.getSub_dist());
                holder.list_txt5.setText(hmodl.getDist());
            }else if(ld_dat_type_list.equals("bbank")){
                holder.list_lout_id6.setVisibility(View.VISIBLE);
                holder.list_lout_id7.setVisibility(View.VISIBLE);
                holder.list_lout_id8.setVisibility(View.VISIBLE);
                holder.list_lout_id9.setVisibility(View.VISIBLE);
                holder.list_lout_id10.setVisibility(View.VISIBLE);
                holder.list_lout_id11.setVisibility(View.VISIBLE);
                holder.count_txt.setText(String.valueOf(count));
                holder.list_lable1.setText("Blood Bank Name : ");
                holder.list_lable2.setText("Blood Bank Address : ");
                holder.list_lable3.setText("Blood Bank District : ");
                holder.list_lable4.setText("Blood Bank Pincode : ");
                holder.list_lable5.setText("Blood Bank Cont.No : ");
                holder.list_lable6.setText("Blood Bank Mobile : ");
                holder.list_lable7.setText("Blood Bank HelpLine : ");
                holder.list_lable8.setText("Blood Bank Fax : ");
                holder.list_lable9.setText("Blood Bank Email : ");
                holder.list_lable10.setText("Blood Bank Website : ");
                holder.list_lable11.setText("Blood Bank Category : ");
                holder.list_txt1.setText(hmodl.getName());
                holder.list_txt2.setText(hmodl.getAddress());
                holder.list_txt3.setText(hmodl.getDist());
                holder.list_txt4.setText(hmodl.getPincode());
                holder.list_txt5.setText(hmodl.getCont_num());
                holder.list_txt6.setText(hmodl.getMob_num());
                holder.list_txt7.setText(hmodl.getHelpline());
                holder.list_txt8.setText(hmodl.getFax());
                holder.list_txt9.setText(hmodl.getEmail());
                holder.list_txt10.setText(hmodl.getWebsite());
                holder.list_txt11.setText(hmodl.getCategory());
            }

            return view;
        }
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }
    public int getZoomLevel(Circle circle) {
        int zoomLevel=0;
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    private void PassDataToDir(String dirn_nam,String dirn_lat,String dirn_lng ){
        boolean checkNet=checkConnection();
        if(checkNet==true) {
            if (!dirn_nam.equals("") && !dirn_lat.equals("") && !dirn_lng.equals("")) {
                Intent intent = new Intent(MapsActivity.this, DirectionMapsActivity.class);
                intent.putExtra("bh_name", dirn_nam);
                intent.putExtra("bh_lat", dirn_lat);
                intent.putExtra("bh_lng", dirn_lng);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No Locations are available!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
        }
    }


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
//    public String getSubscriptionInfo(Context context) {
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
////            return "Not supported";
////        if (!hasPhoneStatePermission(context))
////            return "No permission";
//
//        StringBuilder sb = new StringBuilder();
//        SubscriptionManager sm = SubscriptionManager.from(context);
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//        sb.append("Slots ")
//                .append(sm.getActiveSubscriptionInfoCount())
//                .append('/')
//                .append(sm.getActiveSubscriptionInfoCountMax())
//                .append("\r\n");
//
//        List<SubscriptionInfo> subscriptions = sm.getActiveSubscriptionInfoList();
//        if (subscriptions != null)
//            for (SubscriptionInfo si : subscriptions)
//                sb.append("SIM ")
//                        .append(si.getSimSlotIndex() + 1)
//                        .append('/')
//                        .append(si.getSubscriptionId())
//                        .append(' ')
//                        .append(si.getCountryIso())
//                        .append('/')
//                        .append(si.getMcc()).append(si.getMnc())
//                        .append(' ')
//                        .append(si.getCarrierName())
//                        .append(si.getDataRoaming() == SubscriptionManager.DATA_ROAMING_ENABLE ? " R" : "")
//                        .append("\r\n");
//
//        if (sb.length() > 2)
//            sb.setLength(sb.length() - 2);
//
//        return sb.toString();
//    }
}
