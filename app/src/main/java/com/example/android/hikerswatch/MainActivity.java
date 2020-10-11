package com.example.android.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();
        }

    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo",location.toString());

        TextView  latTextView = findViewById(R.id.latitude_id);

        TextView  lonTextView = findViewById(R.id.longitude_id);

        TextView  Accuracy = findViewById(R.id.Accuracy_id);

        TextView  AltitudeTextView = findViewById(R.id.Altitude_id);


        latTextView.setText(String.format("Latitude : %.2f", location.getLatitude()));
        lonTextView.setText(String.format("Longitude : %.2f", location.getLongitude()));
        Accuracy.setText(String.format("Accuracy : %s", location.getAccuracy()));
        AltitudeTextView.setText(String.format("Altitude :%s", location.getAltitude()));
      //  AddressTextView.setText(String.format("Address : %s",location.getAd));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            String address = "Could not find address";

            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(addressList != null && addressList.size() >0) {
                address = "";

                if(addressList.get(0).getSubThoroughfare() != null)
                address += addressList.get(0).getSubThoroughfare() + " ";

                if(addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + "\n ";

                if(addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + "\n";

                if(addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + "\n ";

                if(addressList.get(0).getCountryName() != null)
                    address += addressList.get(0).getCountryName() + " ";

                TextView  AddressTextView = findViewById(R.id.Address_id);

                AddressTextView.setText(address);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastKnownLocation != null)

            updateLocationInfo(lastKnownLocation);
        }
    }
}