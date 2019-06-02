package nz.co.trademe.mapme.mapbox

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.mapbox.mapboxsdk.maps.MapboxMap
import nz.co.trademe.mapme.MapMeListAdapter

abstract class MapboxMapMeListAdapter<T>(context: Context, config: AsyncDifferConfig<T>)
    : MapMeListAdapter<T, MapboxMap>(context, MapboxAnnotationFactory(), config) {

    constructor(context: Context, diffCallback: DiffUtil.ItemCallback<T>)
            : this(context, AsyncDifferConfig.Builder(diffCallback).build())
}