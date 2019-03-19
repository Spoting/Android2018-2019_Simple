package gr.hua.googlemappins;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Camera;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<UserBean> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /** Get Arraylist from extras of Intent*/
        arr = getIntent().getParcelableArrayListExtra("Position_List");
        Log.d("arrSize", " " + arr.size());

        /* debug
        for (int i=0; i < arr.size(); i++){
            Log.e("TrueData", ""+ arr.get(i).getUserid()+" : "+arr.get(i).getLon() + " " +arr.get(i).getLat());
        }*/

        /** Obtain the SupportMapFragment and get notified when the map is ready to be used.*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng tmpPosition = new LatLng(0,0);
        /** Loop through our Array and add corresponsive Markers*/
        for (int i = 0; i < arr.size(); i++) {
            Log.e("data", ""+ arr.get(i).getUserid()+" : "+arr.get(i).getLon() + " " +arr.get(i).getLat());
            MarkerOptions options = new MarkerOptions();
            tmpPosition = new LatLng(arr.get(i).getLat(),arr.get(i).getLon());
            options.position(tmpPosition);
            options.title(arr.get(i).getDt());

            mMap.addMarker(options);

        }
        /** Finally move Camera to the last Location*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmpPosition, 0));
    }
}
