package nz.co.trademe.mapme.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import nz.co.trademe.mapme.MapMeAdapter;
import nz.co.trademe.mapme.annotations.AnnotationFactory;
import nz.co.trademe.mapme.annotations.MapAnnotation;
import nz.co.trademe.mapme.annotations.MarkerAnnotation;

public class SampleMapMeAdapter<M> extends MapMeAdapter<M> {

    private final List<MarkerData> markers;

    public SampleMapMeAdapter(@NonNull Context context, @NonNull List<MarkerData> markers, @NonNull AnnotationFactory<M> annotationFactory) {
        super(context, annotationFactory);
        this.markers = markers;
    }

    @NotNull
    @Override
    public MapAnnotation onCreateAnnotation(@NotNull AnnotationFactory<M> mapFactory, int position, int viewType) {
        MarkerData item = this.markers.get(position);
        return mapFactory.createMarker(item.getLatLng(), getIconBitmap(item), item.getTitle());
    }

    @Override
    public void onBindAnnotation(@NotNull MapAnnotation annotation, int position, Object payload) {
        if (annotation instanceof MarkerAnnotation) {
            MarkerData item = this.markers.get(position);
            ((MarkerAnnotation) annotation).setIcon(getIconBitmap(item));
        }
    }

    private Bitmap getIconBitmap(MarkerData item) {
        Drawable icon;

        MarkerData.MarkerColour colour = item.getMarkerColour();

        if (item.isSelected()) {
            colour = MarkerData.MarkerColour.GREEN;
        }

        switch (colour) {
            case BLUE:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.marker_blue);
                break;
            case GREEN:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.marker_green);
                break;
            case RED:
            default:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.marker_red);
                break;
        }

        return ((BitmapDrawable) icon).getBitmap();
    }


    @Override
    public int getItemCount() {
        return this.markers.size();
    }

}
