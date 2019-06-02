package nz.co.trademe.mapme.googlemaps

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.GoogleMap
import nz.co.trademe.mapme.MapMeListAdapter

abstract class GoogleMapMeListAdapter<T>(context: Context, config: AsyncDifferConfig<T>)
    : MapMeListAdapter<T, GoogleMap>(context, GoogleMapAnnotationFactory(), config) {

    constructor(context: Context, diffCallback: DiffUtil.ItemCallback<T>)
            : this(context, AsyncDifferConfig.Builder(diffCallback).build())
}