package nz.co.trademe.mapme.sample.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import nz.co.trademe.mapme.mapbox.MapBoxUtils;
import nz.co.trademe.mapme.mapbox.MapboxAnnotationFactory;
import nz.co.trademe.mapme.sample.R;

public class MapBoxActivity extends MapActivity<MapboxMap>
        implements OnMapReadyCallback, MapboxMap.OnMarkerClickListener {

    private MapView mapView;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, MapBoxActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapbox_activity);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(Style.LIGHT);

        mapView.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(MapboxMap map) {
        onMapReady(new MapboxAnnotationFactory(), map);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapBoxUtils.toMapBoxLatLng(aucklandLatLng), 13));
    }

    @Override
    View getMapView() {
        return mapView;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mapsAdapter.notifyAnnotatedMarkerClicked(marker);
        return true;
    }
}
