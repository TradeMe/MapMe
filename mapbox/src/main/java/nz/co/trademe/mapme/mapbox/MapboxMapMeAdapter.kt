package nz.co.trademe.mapme.mapbox

import android.content.Context
import com.mapbox.mapboxsdk.maps.MapboxMap
import nz.co.trademe.mapme.MapMeAdapter

abstract class MapboxMapMeAdapter(context: Context) : MapMeAdapter<MapboxMap>(context, MapboxAnnotationFactory())