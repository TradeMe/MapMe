package nz.co.trademe.mapme.annotations

import android.graphics.Bitmap
import nz.co.trademe.mapme.LatLng

abstract class MarkerAnnotation(latLng: LatLng,
                                title: String? = null,
                                icon: Bitmap? = null,
                                zIndex: Float = 0f,
                                alpha: Float = 1f,
                                anchorUV: Pair<Float, Float> = Pair(0.5f, 1.0f)) : MapAnnotation() {

    var latLng: LatLng = latLng
        set(value) {
            field = value
            onUpdatePosition(value)
        }

    var title: String? = title
        set(value) {
            field = value
            onUpdateTitle(value)
        }

    var icon: Bitmap? = icon
        set(value) {
            field = value
            onUpdateIcon(value)
        }

    var zIndex: Float = zIndex
        set(value) {
            field = value
            onUpdateZIndex(value)
        }

    var alpha: Float = alpha
        set(value) {
            field = value
            onUpdateAlpha(value)
        }

    var anchor: Pair<Float, Float> = anchorUV
        set(value) {
            field = value
            onUpdateAnchor(value)
        }

    /**
     * Called when an icon has been set on the annotation.
     *
     * Update the native marker with the [icon]
     */
    abstract protected fun onUpdateIcon(icon: Bitmap?)

    /**
     * Called when an title has been set on the annotation.
     *
     * Update the native marker with the [title]
     */
    abstract protected fun onUpdateTitle(title: String?)

    /**
     * Called when a position has been set on the annotation.
     *
     * Update the native marker with the [position]
     */
    abstract protected fun onUpdatePosition(position: LatLng)

    /**
     * Called when a zindex has been set on the annotation.
     *
     * Update the native marker with the [zindex]
     */
    abstract protected fun onUpdateZIndex(index: Float)

    /**
     * Called when an alpha has been set on the annotation.
     *
     * Update the native marker with the [alpha]
     */
    abstract protected fun onUpdateAlpha(alpha: Float)

    /**
     * Called when an anchor has been set on the annotation.
     *
     * Update the native marker with the [anchor]
     */
    abstract protected fun onUpdateAnchor(anchorUV: Pair<Float, Float>)


    override fun toString(): String {
        return "MarkerAnnotation(latLng=$latLng, title=$title, icon=$icon, zIndex=$zIndex, alpha=$alpha, anchor(U,V)=${anchor.first},${anchor.second})"
    }

}