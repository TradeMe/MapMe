package nz.co.trademe.mapme.sample.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import nz.co.trademe.mapme.annotations.AnnotationFactory;
import nz.co.trademe.mapme.annotations.MapAnnotation;
import nz.co.trademe.mapme.annotations.OnMapAnnotationClickListener;
import nz.co.trademe.mapme.sample.MarkerBottomSheet;
import nz.co.trademe.mapme.sample.MarkerData;
import nz.co.trademe.mapme.sample.SampleMapMeAdapter;
import timber.log.Timber;

public abstract class MapActivity<M> extends BaseActivity implements OnMapAnnotationClickListener {

    protected SampleMapMeAdapter<M> mapsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onMapReady(AnnotationFactory<M> annotationFactory, M map) {
        mapsAdapter = new SampleMapMeAdapter<>(this, this.markers, annotationFactory);
        mapsAdapter.attach(getMapView(), map);
        mapsAdapter.setOnAnnotationClickListener(this);

        List<MarkerData> newMarkers = addSampleMarkers(this.markers, MARKER_COUNT);
        onMarkersChanged(newMarkers);
    }

    @Override
    void dispatchDiffUtilResult(DiffUtil.DiffResult result) {
        result.dispatchUpdatesTo(mapsAdapter);
    }

    abstract View getMapView();

    @Override
    public boolean onMapAnnotationClick(@NotNull MapAnnotation mapAnnotation) {
        Timber.d("annotation click: " + mapAnnotation);

        MarkerBottomSheet bottomSheetDialog = MarkerBottomSheet.newInstance(mapAnnotation);
        bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");

        MarkerData data = markers.get(mapAnnotation.getPosition());
        data.setSelected(true);
        mapsAdapter.notifyItemChanged(mapAnnotation.getPosition());
        return true;
    }

    public void removeAnnotation(MapAnnotation mapAnnotation) {
        markers.remove(mapAnnotation.getPosition());
        mapsAdapter.notifyItemRemoved(mapAnnotation.getPosition());
    }

    public void unselectMarker(MapAnnotation mapAnnotation) {
        markers.get(mapAnnotation.getPosition()).setSelected(false);
        mapsAdapter.notifyItemChanged(mapAnnotation.getPosition());
    }

}
