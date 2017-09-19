package nz.co.trademe.mapme.util

import nz.co.trademe.mapme.LatLng
import nz.co.trademe.mapme.annotations.AnnotationFactory
import nz.co.trademe.mapme.annotations.MarkerAnnotation

class TestAnnotationFactory : AnnotationFactory<TestMap> {

    override fun createMarker(latLng: LatLng): MarkerAnnotation {
        return TestAnnotation()
    }

    override fun clear(map: TestMap) {
    }

    override fun setOnMarkerClickListener(map: TestMap, onClick: (marker: Any) -> Boolean) {
    }

    override fun setOnInfoWindowClickListener(map: TestMap, onClick: (marker: Any) -> Boolean) {
    }
}

