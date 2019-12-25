package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView welcomeText,lat,lon,acc,alt,addr,latV,lonV,accV,altV,addrV;
    LocationManager manager;
    LocationListener listener;
    Geocoder gcoder;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welomeText);
        lat = findViewById(R.id.latText);
        lon = findViewById(R.id.lonText);
        acc = findViewById(R.id.accText);
        alt = findViewById(R.id.altText);
        addr = findViewById(R.id.adrText);
        latV = findViewById(R.id.latVal);
        lonV = findViewById(R.id.lonVal);
        accV = findViewById(R.id.accVal);
        altV = findViewById(R.id.altVal);
        addrV = findViewById(R.id.adrVal);

        lon.setText("Longitude:");
        lat.setText("Latitude:");
        acc.setText("Accuracy:");
        alt.setText("Altitude:");
        addr.setText("Address:");
        welcomeText.setText("Hiker's Watch");

        //temp
//        lonV.setText("0");
//        latV.setText("0");
//        accV.setText("0");
//        altV.setText("0");
//        addrV.setText("0");

        manager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location){
                Log.i("Location:",location.toString());
                Log.i("=========","========================");
                String address = "";
                if(location!=null){
                    double latitude =  location.getLatitude();
                    double longitude = location.getLongitude();
                    float accuracy = location.getAccuracy();
                    double altitude = location.getAltitude() ;
                    latV.setText(String.format("%.5f",latitude));
                    lonV.setText(String.format("%.5f",longitude));
                    accV.setText(String.format("%.2f",accuracy));
                    altV.setText(String.format("%.2f",altitude));

                    gcoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try{
                        List<Address> addressList = gcoder.getFromLocation(latitude,longitude,1);
                        if(addressList!=null && addressList.size()>0){
                            address += addressList.get(0).getLocality() +" ";
                            address += addressList.get(0).getSubAdminArea()+" ";
                            address += addressList.get(0).getAdminArea() + " ";
                            address += addressList.get(0).getCountryCode();

                            addrV.setText(address);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(Build.VERSION.SDK_INT < 23){
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);
            // last known location
            Location lastloc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            gcoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                if(lastloc!=null) {
                    try {
                        List<Address> addressList = gcoder.getFromLocation(lastloc.getLatitude(), lastloc.getLongitude(), 1);
                        if (addressList != null && addressList.size() > 0) {
                            String address = "";
                            address += addressList.get(0).getLocality() + " ";
                            address += addressList.get(0).getSubAdminArea() + " ";
                            address += addressList.get(0).getAdminArea() + " ";
                            address += addressList.get(0).getCountryCode();

                            addrV.setText(address);
                            double latitude = lastloc.getLatitude();
                            double longitude = lastloc.getLongitude();
                            float accuracy = lastloc.getAccuracy();
                            double altitude = lastloc.getAltitude();
                            latV.setText(String.format("%.5f", latitude));
                            lonV.setText(String.format("%.5f", longitude));
                            accV.setText(String.format("%.2f", accuracy));
                            altV.setText(String.format("%.2f", altitude));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }


}

