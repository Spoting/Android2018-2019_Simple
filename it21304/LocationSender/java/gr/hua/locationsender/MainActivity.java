package gr.hua.locationsender;


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

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION=1;
    public static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    /** Variable for 3rd Task, it will change when button is pressed
     *                              else it will stays "auto" */
    private String user_id = "auto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final TextView txtView = (TextView) findViewById(R.id.textView);
        final EditText txtUser = (EditText) findViewById(R.id.editText);
        final Button btn = (Button) findViewById(R.id.button);

        verifyPermissions();/** line 78 */

        /** Create BroadcastReceiver */
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
                if (isAirplaneModeOn) {   /** Start service if Airplane mode is ON*/
                    Toast.makeText(context, "AirplaneMode state changed ON",
                            Toast.LENGTH_SHORT).show();
                    Log.d("AirplaneMode", "Service state changed ON");
                                                      /** putExtra to pass the user's User_ID to Service*/
                    startService(new Intent(context, MyService.class).putExtra("userid",  user_id));

                } else { /** Stop service if Airplane mode is OFF*/
                    Toast.makeText(context, "AirplaneMode state changed OFF",
                            Toast.LENGTH_SHORT).show();
                    Log.d("AirplaneMode", "Service state changed OFF");

                    stopService(new Intent(context, MyService.class));
                }
            }
        };

        /** Setting user's User_ID*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 user_id = txtUser.getText().toString();
            }

        });
        /** Register Receiver*/
        this.registerReceiver(receiver, intentFilter);
    }
    /**Verify permissions for API > 23 */
    private void verifyPermissions(){
        int permissionsGranted = 101;
        if(permissionsGranted != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        }
    }
}
/**Try Insert*/
               /* ContentValues values = new ContentValues();
                values.put(KEY_USERID, txtUser.getText().toString());
                values.put(KEY_LONGIT, 12);
                values.put(KEY_LATIT, 13);
                values.put(KEY_DT, "2020");
                Uri resultUri = resolver.insert(uri, values);
                Log.d("kok", resultUri.toString()); */
/**Search All After Insert*/
                /*
                Cursor mCursor = resolver.query(uri, null, null, null, null);
                if (mCursor.getCount() == 0) {
                    mCursor.close();
                    Toast.makeText(getApplicationContext(), "No Count", Toast.LENGTH_LONG).show();
                } else {
                    mCursor.moveToFirst();
                    String x = "";
                    int i = 0;
                    do {
                        x += i + ": la " + i+"  " + mCursor.getString(1) + "+"+mCursor.getString(4)+"\n";
                        Toast.makeText(getApplicationContext(), "DataCame", Toast.LENGTH_LONG).show();
                        i++;
                    } while (mCursor.moveToNext());
                    txtView.setText(x);
                }*/