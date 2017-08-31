package nz.co.trademe.mapme.googlemaps

import android.graphics.Bitmap
import com.google.android.gms.maps.GoogleMap
import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.AnnotationFactory
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class GoogleMapAnnotationFactory : AnnotationFactory<GoogleMap> {

    override fun createMarker(latLng: LatLng, icon: Bitmap?, title: String?): MarkerAnnotation<GoogleMap> {
        return GoogleMapMarkerAnnotation(latLng, title, icon)
    }

    override fun clear(map: GoogleMap) {
        map.clear()
    }

    override fun setOnMarkerClickListener(map: GoogleMap, onClick: (marker: Any) -> Boolean) {
        map.setOnMarkerClickListener { marker -> onClick(marker) }
    }

    override fun setOnInfoWindowClickListener(map: GoogleMap, onClick: (marker: Any) -> Boolean) {
        map.setOnInfoWindowClickListener { marker -> onClick(marker) }
    }
}