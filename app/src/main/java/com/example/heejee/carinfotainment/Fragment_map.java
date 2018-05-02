package com.example.heejee.carinfotainment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import static com.google.android.gms.internal.a.R;


public class Fragment_map extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.getUiSettings().setMapToolbarEnabled(true);
/*
                GoogleMapOptions options = new GoogleMapOptions();
                options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                        .compassEnabled(false)
                        .rotateGesturesEnabled(false)
                        .tiltGesturesEnabled(false);
                       // .UiSettings.setMapToolbarEnabled(boolean);
*/

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);

                } else {
                    // Show rationale and request permission.
                }


                // For dropping a marker at a point on the Map

                LatLng seoul = new LatLng(37.495422, 126.959656);
                googleMap.addMarker(new MarkerOptions().position(seoul).title("Cyber Security Lab").snippet("Soongsil University"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(seoul).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}