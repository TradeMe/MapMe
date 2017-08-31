package nz.co.trademe.mapme.annotations

import android.graphics.Bitmap
import nz.co.trademe.mapme.LatLng

interface AnnotationFactory<in Map> {

    fun createMarker(latLng: LatLng, icon: Bitmap?, title: String?): MarkerAnnotation<Map>

    fun clear(map: Map)

    fun setOnMarkerClickListener(map: Map, onClick: (marker: Any) -> Boolean)

    fun setOnInfoWindowClickListener(map: Map, onClick: (marker: Any) -> Boolean)

}