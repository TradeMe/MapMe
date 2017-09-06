package nz.co.trademe.mapme.annotations

interface OnInfoWindowClickListener {

    fun onInfoWindowClick(mapAnnotationObject: MapAnnotation): Boolean

}