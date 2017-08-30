package nz.co.trademe.mapme.googlemaps

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import nz.co.trademe.mapme.MapMeAdapter

abstract class GoogleMapMeAdapter(context: Context) : MapMeAdapter<GoogleMap>(context, GoogleMapAnnotationFactory())