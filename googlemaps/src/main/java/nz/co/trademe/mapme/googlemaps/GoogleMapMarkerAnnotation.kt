package nz.co.trademe.mapme.googlemaps

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class GoogleMapMarkerAnnotation(latLng: LatLng,
                                title: String?,
                                icon: Bitmap? = null) : MarkerAnnotation<GoogleMap>(latLng, title, icon) {

    override fun onUpdateIcon(icon: Bitmap?) {
        nativeMarker?.setIcon(icon?.toBitmapDescriptor())
    }

    override fun onUpdateTitle(title: String?) {
        nativeMarker?.title = title
    }

    override fun onUpdatePosition(position: LatLng) {
        nativeMarker?.position = position.toGoogleMapsLatLng()
    }

    override fun onUpdateZIndex(index: Float) {
        nativeMarker?.zIndex = index
    }

    override fun onUpdateAlpha(alpha: Float) {
        nativeMarker?.alpha = alpha
    }

    private var nativeMarker: Marker? = null

    override fun annotatesObject(objec: Any): Boolean {
        return nativeMarker?.equals(objec) ?: false
    }

    override fun removeFromMap(map: GoogleMap, context: Context) {
        nativeMarker?.remove()
        nativeMarker = null
    }

    override fun addToMap(map: GoogleMap, context: Context) {
        val options = MarkerOptions()
                .position(latLng.toGoogleMapsLatLng())
                .icon(icon?.toBitmapDescriptor())
                .title(title)
                .alpha(alpha)
                .zIndex(zIndex)

        nativeMarker = map.addMarker(options)
    }

}
