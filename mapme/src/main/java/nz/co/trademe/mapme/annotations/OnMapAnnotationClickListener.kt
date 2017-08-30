package nz.co.trademe.mapme.annotations

interface OnMapAnnotationClickListener {

    fun onMapAnnotationClick(mapAnnotationObject: MapAnnotation): Boolean

}