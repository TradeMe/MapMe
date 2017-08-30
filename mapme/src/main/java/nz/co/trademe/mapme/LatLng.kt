package nz.co.trademe.mapme

class LatLng(var1: Double, var3: Double) {
    val latitude: Double
    val longitude: Double

    init {
        if (-180.0 <= var3 && var3 < 180.0) {
            this.longitude = var3
        } else {
            this.longitude = ((var3 - 180.0) % 360.0 + 360.0) % 360.0 - 180.0
        }
        this.latitude = Math.max(-90.0, Math.min(90.0, var1))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as LatLng

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

    override fun toString(): String {
        return "LatLng(latitude=$latitude, longitude=$longitude)"
    }

}