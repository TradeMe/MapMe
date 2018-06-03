package nz.co.trademe.mapme.util

import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class TestAnnotation : MarkerAnnotation(LatLng(0.0, 0.0)) {

    override fun annotatesObject(nativeObject: Any): Boolean {
        return false
    }

    override fun addToMap(map: Any, context: android.content.Context) {
    }

    override fun removeFromMap(map: Any, context: android.content.Context) {
    }

    override fun onUpdateIcon(icon: android.graphics.Bitmap?) {
    }

    override fun onUpdateTitle(title: String?) {
    }

    override fun onUpdatePosition(position: nz.co.trademe.mapme.LatLng) {
    }

    override fun onUpdateZIndex(index: Float) {
    }

    override fun onUpdateAlpha(alpha: Float) {
    }

    override fun toString(): String {
        return "TestAnnotation( position = $position)"

    }


}
