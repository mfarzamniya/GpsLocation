package app.mma.gpslocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class GpsTracker extends Service implements LocationListener {

    private Context context;
    private LocationManager locationManager;

    private Location location;
    private double latitude;
    private double longitude;

    private boolean isGpsEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    public static final int TIME_BW_UPDATES = 1000 * 10 ; // 10 second
    public static final int MIN_DISTANCE_FOR_UPDATE = 10; //10 meter

    public GpsTracker(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation() throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGpsEnabled && !isNetworkEnabled){
            canGetLocation = false;
            // no provider enable
        } else {
            canGetLocation = true;
            // first, get location using network provider
            if(isNetworkEnabled){
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        TIME_BW_UPDATES,
                        MIN_DISTANCE_FOR_UPDATE,
                        this);
                if(locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

            }

            if(isGpsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATE, this);
                if(locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
        return location;
    }

    public double getLatitude(){
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    public void showGpsAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GPS")
               .setMessage("GPS is not enabled. Do you want to go to Setting menu?")
               .setPositiveButton("Seetings", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent intent = new Intent();
                       intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       context.startActivity(intent);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                   }
               });
        builder.show();
    }

    public void stopUsingGps(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            Log.i(GpsTracker.class.getSimpleName(),
                    "lat : " + location.getLatitude() + ", lon = " + location.getLongitude());
        } else {
            Log.i(GpsTracker.class.getSimpleName(), "location = null");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(GpsTracker.class.getSimpleName(), "provider enabled : " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(GpsTracker.class.getSimpleName(), "provider disabled : " + provider);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
