package nz.co.trademe.mapme.annotations

import nz.co.trademe.mapme.LatLng

interface AnnotationFactory<in Map> {

    fun createMarker(latLng: LatLng): MarkerAnnotation

    fun clear(map: Map)

    fun setOnMarkerClickListener(map: Map, onClick: (marker: Any) -> Boolean)

    fun setOnInfoWindowClickListener(map: Map, onClick: (marker: Any) -> Boolean)

}