@file:JvmName("MapBoxUtils")
package nz.co.trademe.mapme.mapbox

import android.content.Context
import android.graphics.Bitmap
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import nz.co.trademe.mapme.LatLng

fun LatLng.toMapBoxLatLng(): com.mapbox.mapboxsdk.geometry.LatLng {
    return com.mapbox.mapboxsdk.geometry.LatLng(this.latitude, this.longitude)
}

fun Bitmap.toMapboxIcon(context: Context): Icon {
    return IconFactory.getInstance(context).fromBitmap(this)
}
