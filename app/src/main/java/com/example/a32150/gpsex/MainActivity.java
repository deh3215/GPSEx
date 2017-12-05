package com.example.a32150.gpsex;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView tv, tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        //tv1 = findViewById(R.id.textView1);

        tv.setText("定位中....");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null)
        {
// 取到手機地點後的程式碼
            Log.d("LOC", mLastLocation.getLongitude() + "," + mLastLocation.getLatitude());
            tv.setText(mLastLocation.getLongitude() + "," + mLastLocation.getLatitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onClick(View v)   {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(
                "https://maps.googleapis.com/maps/api/elevation/json?locations=24.9303237,121.1715947&key=AIzaSyC3qUJpugVjMS2jMwnQfZ6mvKEmJwgAB6o",
        new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
//Method-1
                    //JSONObject obj = new JSONObject(response);
                    //JSONArray array = obj.getJSONArray("results");
                    //JSONObject obj1 = array.getJSONObject(0);
                    //double result = ev.results[0].elevation;
                    //tv.setText(result);
//Method-2
                    Gson gson = new Gson();
                    elevation ev = gson.fromJson(response, elevation.class);

                    String elevation = String.valueOf(ev.results[0].elevation);
                    String lng = String.valueOf(ev.results[0].location.lng);
                    String lat = String.valueOf(ev.results[0].location.lat);
                    String resolution = String.valueOf(ev.results[0].resolution);

                    tv.setText("高度 : "+elevation+"\n緯度 : "+lng+"\n經度 : "+lat+"\n解析度 : "+resolution);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
    }

    public void onClick1(View v)   {
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> list = geocoder.getFromLocation(24.9318495, 121.1717214, 10);
            Address addr = list.get(0);
            Address addr1 = list.get(1);
            String str = addr.getAddressLine(0);
            tv.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
