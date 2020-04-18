package ru.bdim.weather.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.bdim.weather.R;
import ru.bdim.weather.addiyional.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra(LAT, 37);
        double lon = intent.getDoubleExtra(LON, -122);
        mMap = googleMap;

        LatLng loc = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(loc));//.title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                setMarker(latLng);
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra(LAT, latLng.latitude);
                intent.putExtra(LON, latLng.longitude);
                setResult(MAP_RESULT_CODE, intent);
                finish();
            }
        });
    }
    private void setMarker(LatLng location){
        mMap.addMarker(new MarkerOptions().position(location));
    }
}