package nz.co.trademe.mapme.sample.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.squareup.picasso.Picasso;

import nz.co.trademe.mapme.sample.R;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_activity);

        View googleMapsButton = findViewById(R.id.google_maps_container);
        googleMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMapsActivity.start(ChoiceActivity.this);
            }
        });

        View mapBoxButton = findViewById(R.id.mapbox_container);
        mapBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapBoxActivity.start(ChoiceActivity.this);
            }
        });

        ImageView mapboxImageView = (ImageView) findViewById(R.id.mapbox_imageview);

        try {
            MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
                    .setAccessToken(Mapbox.getAccessToken())
                    .setUsername(Constants.MAPBOX_USER)
                    .setStyleId("light-v9")
                    .setLat(nz.co.trademe.mapme.sample.Constants.AUCKLAND_LAT)
                    .setLon(nz.co.trademe.mapme.sample.Constants.AUCKLAND_LON)
                    .setZoom(13)
                    .setWidth(1280)
                    .setHeight(600)
                    .setRetina(true) // Retina 2x image will be returned
                    .build();

            Picasso.with(this)
                    .load(staticImage.getUrl().url().toString()).into(mapboxImageView);
        } catch (ServicesException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().findFragmentById(R.id.google_lite_fragment).getView().setClickable(false);
    }
}
