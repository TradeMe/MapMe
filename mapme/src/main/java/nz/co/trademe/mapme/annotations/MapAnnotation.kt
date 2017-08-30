package nz.co.trademe.mapme.annotations

import android.content.Context
import nz.co.trademe.mapme.NO_POSITION
import java.io.Serializable

abstract class MapAnnotation : Serializable {

    var position = NO_POSITION
    var placeholder = false
    internal var isDirty = false

    abstract fun annotatesObject(nativeObject: Any): Boolean

    abstract fun addToMap(map: Any, context: Context)

    abstract fun removeFromMap(map: Any, context: Context)

}

