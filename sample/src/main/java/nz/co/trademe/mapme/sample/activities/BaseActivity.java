package nz.co.trademe.mapme.sample.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nz.co.trademe.mapme.LatLng;
import nz.co.trademe.mapme.sample.Constants;
import nz.co.trademe.mapme.sample.MarkerData;
import nz.co.trademe.mapme.sample.R;
import nz.co.trademe.mapme.sample.Util;


public abstract class BaseActivity extends AppCompatActivity {

    protected static final LatLng aucklandLatLng = new LatLng(Constants.AUCKLAND_LAT, Constants.AUCKLAND_LON);

    protected final List<MarkerData> markers = new ArrayList<>();

    protected boolean detectMoves;

    protected static int MARKER_COUNT = 3;

    public List<MarkerData> addSampleMarkers(List<MarkerData> markersList, int number) {
        return addSampleMarkers(markersList, number, MarkerData.MarkerColour.RED);
    }

    public List<MarkerData> addSampleMarkers(List<MarkerData> markersList, int number, MarkerData.MarkerColour colour) {
        List<MarkerData> list = new ArrayList<>(markersList);
        for (int i = 0; i < number; i++) {
            list.add(new MarkerData(Util.getLocationInLatLngRad(1000, aucklandLatLng), "Marker " + (i + 1), colour));
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.detect_moves_item).setChecked(this.detectMoves);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.detect_moves_item:
                this.detectMoves = !this.detectMoves;
                item.setChecked(this.detectMoves);
                break;
            case R.id.add_10_item:
                List<MarkerData> newMarkers = addSampleMarkers(this.markers, 10);
                onMarkersChanged(newMarkers);
                return true;
            case R.id.remove_5_item:
                ArrayList<MarkerData> removedCopy = new ArrayList<>(this.markers);
                List<MarkerData> random5Removed = getRandomMarkerData(5);

                for (MarkerData marker : random5Removed) {
                    removedCopy.remove(marker);
                }

                onMarkersChanged(removedCopy);
                return true;
            case R.id.add_5_remove_5_item:

                ArrayList<MarkerData> markerCopy = new ArrayList<>(this.markers);
                List<MarkerData> random5 = getRandomMarkerData(5);

                for (MarkerData marker : random5) {
                    markerCopy.remove(marker);
                }

                List<MarkerData> finalMarkerList = addSampleMarkers(markerCopy, 5, MarkerData.MarkerColour.BLUE);
                onMarkersChanged(finalMarkerList);
                return true;

            case R.id.change_5_item:
                ArrayList<MarkerData> markers = new ArrayList<>(this.markers);

                List<MarkerData> randomMarkers = getRandomMarkerData(5);

                MarkerData[] markersArray = new ArrayList<>(this.markers).toArray(new MarkerData[]{});
                for (MarkerData marker : randomMarkers) {
                    MarkerData copy = marker.copy();
                    int index = markers.indexOf(marker);

                    switch (marker.getMarkerColour()) {
                        case RED:
                            copy.setMarkerColour(MarkerData.MarkerColour.BLUE);
                            break;
                        case BLUE:
                            copy.setMarkerColour(MarkerData.MarkerColour.RED);
                            break;
                    }

                    markersArray[index] = copy;
                }

                onMarkersChanged(Arrays.asList(markersArray));
                return true;
            case R.id.move_change:
                ArrayList<MarkerData> changedMarkers = new ArrayList<>(this.markers);

                //move a marker from the start of the list to the end
                MarkerData marker = changedMarkers.remove(0);
                MarkerData copy = marker.copy();
                copy.setMarkerColour(MarkerData.MarkerColour.BLUE);
                changedMarkers.add(copy);
                onMarkersChanged(changedMarkers);

                return true;
        }

        return false;
    }

    protected void onMarkersChanged(List<MarkerData> newMarkers) {
        MarkerDiffCallback callback = new MarkerDiffCallback(this.markers, newMarkers);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, this.detectMoves);

        this.markers.clear();
        this.markers.addAll(newMarkers);
        dispatchDiffUtilResult(diffResult);
    }

    abstract void dispatchDiffUtilResult(DiffUtil.DiffResult result);

    private List<MarkerData> getRandomMarkerData(int count) {
        List<MarkerData> randomMarkers = new ArrayList<>();
        List<MarkerData> markers = new ArrayList<>(this.markers);

        while (randomMarkers.size() < count) {
            Collections.shuffle(markers);
            if (!randomMarkers.contains(markers.get(0))) {
                randomMarkers.add(markers.get(0));
            }
        }

        return randomMarkers;
    }


    public static class MarkerDiffCallback extends DiffUtil.Callback {
        private List<MarkerData> mOldList;
        private List<MarkerData> mNewList;

        public MarkerDiffCallback(List<MarkerData> oldList, List<MarkerData> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList != null ? mOldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewList != null ? mNewList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mNewList.get(newItemPosition).getLatLng().equals(mOldList.get(oldItemPosition).getLatLng());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
        }

    }

}
