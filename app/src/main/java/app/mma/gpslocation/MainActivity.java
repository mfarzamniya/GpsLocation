package app.mma.gpslocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location();

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        map.getMapAsync(this);
    }

    private void location() {
        GpsTracker gpstracker = new GpsTracker(this);
        if(gpstracker.isCanGetLocation()){
            double lat = gpstracker.getLatitude();
            double lon = gpstracker.getLongitude();
            Toast.makeText(this,
                    "Location is : \n lat : " + lat + "\n lon : " + lon,
                    Toast.LENGTH_LONG).show();

            Locale locale = new Locale("fa");
            Geocoder geocoder = new Geocoder(getApplicationContext(), locale);
            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                Log.e("", "");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gpstracker.showGpsAlertDialog();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(35.72749369051, 51.32034518091);

        MarkerOptions markerOptions = new MarkerOptions()
             .position(latLng).title("Android Learn Class")
             .snippet("Vali asr aq")
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        googleMap.addMarker(markerOptions);

        LatLng latLng2 = new LatLng(35.82749369051, 51.52034518091);

        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(latLng).title("Android Learn Class 2")
                .snippet("Vali asr aq 2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_18_2959847));

        googleMap.addMarker(markerOptions2);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setMapType(googleMap.MAP_TYPE_SATELLITE);

    }
}
