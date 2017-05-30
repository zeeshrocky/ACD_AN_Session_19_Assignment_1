package com.example.zeeshan.googlemap;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng rpa = new LatLng(9.993784, 76.357920);
        mMap.addMarker(new MarkerOptions().position(rpa).title("Marker in RPA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rpa,16));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                marker =mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("Current Location")
                        .snippet(getCompleteAddressString(point.latitude,point.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter("Current Location", getCompleteAddressString(point.latitude,point.longitude)));

                marker.showInfoWindow();
                Toast.makeText(getApplicationContext(),
                        point.latitude + "," +
                                point.longitude,
                        Toast.LENGTH_SHORT).show();
                Log.e("My loction address : ", "" + getCompleteAddressString(point.latitude,point.longitude));
            }
        });
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My loction address : ", "" + strReturnedAddress.toString());
            } else {
                Log.e("My loction address : ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My loction address : ", "Cannot get Address!");
        }
        return strAdd;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private String name, vicinity;

        public MyInfoWindowAdapter(String name, String vicinity) {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.custom_info_contents, null);
            this.name = name;
            this.vicinity = vicinity;
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            tvTitle.setText(name);
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(vicinity);

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}