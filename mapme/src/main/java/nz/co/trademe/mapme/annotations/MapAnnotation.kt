package nz.co.trademe.mapme.annotations

import android.content.Context
import nz.co.trademe.mapme.NO_POSITION
import java.io.Serializable

abstract class MapAnnotation<in M> : Serializable {

    var position = NO_POSITION
    var placeholder = false
    internal var isDirty = false

    abstract fun annotatesObject(nativeObject: Any): Boolean

    abstract fun addToMap(map: M, context: Context)

    abstract fun removeFromMap(map: M, context: Context)

}

