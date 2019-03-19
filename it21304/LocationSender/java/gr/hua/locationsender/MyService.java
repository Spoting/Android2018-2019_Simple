package gr.hua.locationsender;


import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Timer timerObj;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 20;

    private String last_user_id ;
    private static final String KEY_USERID = "userid";
    private static final String KEY_LONGIT = "longitude";
    private static final String KEY_LATIT = "latitude";
    private static final String KEY_DT = "dt";

    private static Uri uri =  /** URI to access Provider of first Project*/
            Uri.parse("content://gr.hua.it21304.android_project2018_part1.dbprovider/location");
    ContentResolver resolver;

    /** Location Listener to get our position*/
    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d("location", "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("location", "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("location", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("location", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("location", "onStatusChanged: " + provider);
        }
    }
    /** List that includes alternative Providers that our Listener could use
     * Could also include Google Provider.. */
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("la", "onStartCommand");
        if(intent != null){ /**If not null get user's User_ID*/
            last_user_id = intent.getStringExtra("userid");
            Log.d ("User ID", " : "+last_user_id);
        }

        super.onStartCommand(intent, flags, startId);
        /** With START_STICKY the system will try to re-create the Service after it is killed */
        return START_STICKY;
    }

    @Override /** When the Service is created*/
    public void onCreate() {
        Log.d("create", "onCreate");

        /** InitializeLocation*/
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Log.d("create", "initializeLocationManager");
        }
        /** If we wanted to use Network  */
         /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i("internet", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("internet", "network provider does not exist, " + ex.getMessage());
        } */
        /** To use GPS */
        try {
            mLocationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER,
                            LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.d("gps", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("gps", "gps provider does not exist " + ex.getMessage());
        }

        /** Insert every 3 seconds */
        timerObj = new Timer();/** Automatically creates a new Thread when you Schedule a TimerTask */
        Log.e("timer","timerInitialized");

        TimerTask timerTaskObj = new TimerTask() { /**This will be executed by the new Thread */
            public void run() {
                try {
                    /** Initialize Location to get our lastLocation*/
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.e("loc", "ARG: "+ location.getLongitude() + " " + location.getLatitude());
                    if (location != null) {
                        Log.d("RUN:LOCATION", location.toString());
                        resolver = getContentResolver(); /** Get access to provider via resolver*/
                        /** Gathering data into ContentValues for insert*/
                        ContentValues values = new ContentValues();
                        values.put(KEY_USERID, last_user_id); // User's ID from MainActivity or "auto"
                        values.put(KEY_LONGIT, location.getLongitude()); // Longitude from Location
                        values.put(KEY_LATIT, location.getLatitude()); // Latitude from Location
                        values.put(KEY_DT, getCurrentTimeStamp()); // Timestamp line 181
                        /** Use resolver to insert our location */
                        resolver.insert(MyService.uri, values);
                    }
                } catch (java.lang.SecurityException ex) {
                    Log.e("TIMERTASK", "failed to get Last Known Location", ex);
                }
            }
        };
        timerObj.schedule(timerTaskObj, 0, 3000); /** Schedules the TimerTask,
                                                                    to start immediately (delay: 0),
                                                                    every 3 seconds (period: 3000) */

        //super.onCreate();
    }

    @Override /** when Destroyed remove Listeners*/
    public void onDestroy() {
        Log.e("destroy", "onDestroy");
       super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("destroy", "fail to remove LocationListeners, ignore ", ex);
                }
            }
        }
        if(timerObj != null){
            timerObj.cancel();
        }
    }
    /**Function to get System's Date so we can use it as User's Timestamp*/
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            return null;
        }
    }

}
