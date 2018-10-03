package nz.co.trademe.mapme.sample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import nz.co.trademe.mapme.googlemaps.GoogleMapAnnotationFactory;
import nz.co.trademe.mapme.googlemaps.GoogleMapUtils;
import nz.co.trademe.mapme.sample.R;

public class GoogleMapsActivity extends MapActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, GoogleMapsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemaps_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    View getMapView() {
        return ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getView();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        onMapReady(new GoogleMapAnnotationFactory(), map);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(GoogleMapUtils.toGoogleMapsLatLng(aucklandLatLng), 14));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mapsAdapter.notifyAnnotatedMarkerClicked(marker);
        return true;
    }
}
