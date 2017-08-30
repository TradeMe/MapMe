package nz.co.trademe.mapme.util

class TestAnnotationFactory : nz.co.trademe.mapme.annotations.AnnotationFactory<TestMap> {

    override fun createMarker(latLng: nz.co.trademe.mapme.LatLng, icon: android.graphics.Bitmap?, title: String?): nz.co.trademe.mapme.annotations.MarkerAnnotation<TestMap> {
        return TestAnnotation()
    }

    override fun clear(map: TestMap) {
    }

    override fun setOnMarkerClickListener(map: TestMap, onClick: (marker: Any) -> Boolean) {
    }

    override fun setOnInfoWindowClickListener(map: TestMap, onClick: (marker: Any) -> Boolean) {
    }
}

