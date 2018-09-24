package nz.co.trademe.mapme.annotations;

import androidx.annotation.NonNull;

public interface OnMapAnnotationClickListener {

    boolean onMapAnnotationClick(@NonNull MapAnnotation mapAnnotationObject);

}