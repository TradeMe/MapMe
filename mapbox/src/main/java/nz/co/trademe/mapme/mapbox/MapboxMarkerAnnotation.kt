package nz.co.trademe.mapme.mapbox

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.maps.MapboxMap
import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class MapboxMarkerAnnotation(latLng: LatLng,
                             title: String?,
                             icon: Bitmap? = null) : MarkerAnnotation(latLng, title, icon) {

    override fun onUpdateIcon(icon: Bitmap?) {
        nativeMarker?.let {
            if (icon != null) {
                nativeMarker?.icon = (IconFactory.recreate(nativeMarker!!.icon.id, icon))
            } else {
                nativeMarker?.icon = null
            }
        }
    }

    override fun onUpdateTitle(title: String?) {
        nativeMarker?.title = title
    }

    override fun onUpdatePosition(position: LatLng) {
        nativeMarker?.position = position.toMapBoxLatLng()
    }

    override fun onUpdateZIndex(index: Float) {
        Log.w(MapboxMarkerAnnotation::class.simpleName, "zIndex not supported on MapboxMarkerAnnotations")
    }

    override fun onUpdateAlpha(alpha: Float) {
        Log.w(MapboxMarkerAnnotation::class.simpleName, "alpha not supported on MapboxMarkerAnnotations")
    }

    override fun onUpdateAnchor(anchorUV: Pair<Float, Float>) {
        Log.w(MapboxMarkerAnnotation::class.simpleName, "anchor not supported on MapboxMarkerAnnotations")
    }

    private var nativeMarker: Marker? = null

    override fun annotatesObject(objec: Any): Boolean {
        return nativeMarker?.equals(objec) ?: false
    }

    override fun removeFromMap(map: Any, context: Context) {
        nativeMarker?.remove()
        nativeMarker = null
    }

    override fun addToMap(map: Any, context: Context) {
        val mapboxMap = map as MapboxMap
        val markerOptions = MarkerOptions()
                .icon(icon?.toMapboxIcon(context))
                .title(title)
                .position(latLng.toMapBoxLatLng())
        nativeMarker = mapboxMap.addMarker(markerOptions)
    }

}
