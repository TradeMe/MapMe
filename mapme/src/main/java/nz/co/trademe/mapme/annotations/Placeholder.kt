package nz.co.trademe.mapme.annotations

import android.content.Context

/**
 * This class exists as a placeholder for a map annotation. It is needed to hold a position value,
 * which will be manipulated as DiffUtil updates are applied. Once all updates are applied, this placeholder
 * will be converted into an annotation via createAnnotation.
 *
 * Without this placeholder, createAnnotation is called to early, before all DiffUtil updates
 * have been applied. This causes issues where createAnnotation is called with a position that is
 * out of bounds.
 */
internal class Placeholder : MapAnnotation() {

    init {
        this.placeholder = true
    }

    override fun annotatesObject(nativeObject: Any): Boolean {
        return false
    }

    override fun addToMap(map: Any, context: Context) {
    }

    override fun removeFromMap(map: Any, context: Context) {
    }
}