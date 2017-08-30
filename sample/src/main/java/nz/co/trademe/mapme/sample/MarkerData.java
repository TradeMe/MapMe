package nz.co.trademe.mapme.sample;

import nz.co.trademe.mapme.LatLng;

public class MarkerData {

    private final LatLng latLng;
    private final String title;
    private MarkerColour markerColour = MarkerColour.RED;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MarkerData(LatLng latLng, String title) {
        this(latLng, title, MarkerColour.RED);
    }

    public MarkerData(LatLng latLng, String title, MarkerColour colour) {
        this.latLng = latLng;
        this.title = title;
        this.markerColour = colour;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTitle() {
        return title;
    }

    public MarkerColour getMarkerColour() {
        return markerColour;
    }

    public void setMarkerColour(MarkerColour markerColour) {
        this.markerColour = markerColour;
    }

    public enum MarkerColour {
        RED,
        GREEN,
        BLUE
    }

    @Override
    public String toString() {
        return "MarkerData{" +
                "latLng=" + latLng +
                ", title='" + title + '\'' +
                ", markerColour=" + markerColour +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkerData that = (MarkerData) o;

        if (selected != that.selected) return false;
        if (latLng != null ? !latLng.equals(that.latLng) : that.latLng != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return markerColour == that.markerColour;

    }

    @Override
    public int hashCode() {
        int result = latLng != null ? latLng.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (markerColour != null ? markerColour.hashCode() : 0);
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }

    public MarkerData copy() {
        return new MarkerData(this.latLng, this.title, this.markerColour);
    }
}
