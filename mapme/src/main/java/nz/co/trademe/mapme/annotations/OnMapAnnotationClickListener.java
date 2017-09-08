package nz.co.trademe.mapme.annotations;

import android.support.annotation.NonNull;

public interface OnMapAnnotationClickListener {

    boolean onMapAnnotationClick(@NonNull MapAnnotation mapAnnotationObject);

}