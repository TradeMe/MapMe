package nz.co.trademe.mapme.annotations

interface OnInfoWindowClickListener {

    fun <M> onInfoWindowClick(mapAnnotationObject: MapAnnotation<M>): Boolean

}