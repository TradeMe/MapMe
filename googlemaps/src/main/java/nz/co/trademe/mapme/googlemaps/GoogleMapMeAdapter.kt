package nz.co.trademe.mapme.googlemaps

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import nz.co.trademe.mapme.MapMeAdapter
import nz.co.trademe.mapme.annotations.MapAnnotation
import nz.co.trademe.mapme.annotations.OnInfoWindowClickListener

abstract class GoogleMapMeAdapter(context: Context) : MapMeAdapter<GoogleMap>(context, GoogleMapAnnotationFactory()){
}

