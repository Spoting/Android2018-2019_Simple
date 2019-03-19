package gr.hua.googlemappins;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_USERID = "userid";
    private static final String KEY_LONGIT = "longitude";
    private static final String KEY_LATIT = "latitude";
    private static final String KEY_DT = "dt";
    private int count = 0;
    private Uri uri ;
    //ContentResolver resolver = getContentResolver();
    ContentResolver resolver;

    private ArrayList<UserBean> arr = new ArrayList<UserBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final TextView txtView = (TextView) findViewById(R.id.textView);
        final EditText txtUser = (EditText) findViewById(R.id.editText);
        final Button btn = (Button) findViewById(R.id.button);

        /** When button is pressed */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = txtUser.getText().toString();/** Get user's entry*/
                Log.d ("txttext", ""+user_id);

                  /** We use the second uri in ContentProvider(FirstProject),
                   *        because we want to query based on args
                   */
                uri = Uri.parse("content://gr.hua.it21304.android_project2018_part1.dbprovider/location/"+user_id);
                resolver = getContentResolver();
                String columns[] = {KEY_USERID, KEY_LONGIT, KEY_LATIT, KEY_DT}; /** columns to return*/
                String tmpArgs[] = {user_id}; /** args for where clause*/
                /** Query from database*/
                Cursor mCursor = resolver.query(uri, columns, KEY_USERID+"=?", tmpArgs, null);
                count = mCursor.getCount();
                if (count == 0) { /** if there is no return values, dont do anything*/
                    mCursor.close();
                    Toast.makeText(getApplicationContext(), "No Count", Toast.LENGTH_LONG).show();
                } else {
                    /** Loop through results */
                    mCursor.moveToFirst();
                    do {
                        /** Set values to the Parcelable object UserBean,
                         *  then add the object to our ArrayList
                         */
                        UserBean user = new UserBean();
                        user.setUserid(mCursor.getString(0));
                        user.setLon(mCursor.getFloat(1));
                        user.setLat(mCursor.getFloat(2));
                        user.setDt(mCursor.getString(3));
                        arr.add(user);
                        Log.e("dataBeforeMap", ""+  user.getLon() + " " + user.getLat());
                    } while (mCursor.moveToNext());
                    /*
                    for (int i=0; i < arr.size(); i++){
                        Log.e("TrueDataBeforeMap", ""+ arr.get(i).getUserid()+" : "+arr.get(i).getLon() + " " +arr.get(i).getLat());
                    }*/
                    Toast.makeText(getApplicationContext(), "DataCame", Toast.LENGTH_LONG).show();
                    /** Intent to start GoogleMaps*/
                    Intent intent = new Intent(v.getContext() ,MapsActivity.class);
                    intent.putExtra("Position_List", arr); /** putExtra the Parcelable ArrayList */
                    startActivity(intent);

                }
            }
        });
    }
}

