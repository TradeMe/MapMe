package nz.co.trademe.mapme.annotations

interface OnMapAnnotationClickListener {

    fun <M> onMapAnnotationClick(mapAnnotationObject: MapAnnotation<M>): Boolean

}