package nz.co.trademe.mapme.mapbox

import android.graphics.Bitmap
import com.mapbox.mapboxsdk.maps.MapboxMap
import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.AnnotationFactory
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class MapboxAnnotationFactory : AnnotationFactory<MapboxMap> {

    override fun createMarker(latLng: LatLng, icon: Bitmap?, title: String?): MarkerAnnotation {
        return MapboxMarkerAnnotation(latLng, title, icon)
    }

    override fun clear(map: MapboxMap) {
        map.clear()
    }

    override fun setOnMarkerClickListener(map: MapboxMap, onClick: (marker: Any) -> Boolean) {
        map.setOnMarkerClickListener { marker -> onClick(marker) }
    }

    override fun setOnInfoWindowClickListener(map: MapboxMap, onClick: (marker: Any) -> Boolean) {
        map.setOnInfoWindowClickListener { marker -> onClick(marker) }
    }
}