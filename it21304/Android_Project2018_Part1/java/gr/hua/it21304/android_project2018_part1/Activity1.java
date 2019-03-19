package gr.hua.it21304.android_project2018_part1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        /**EditTexts*/  /** Constraint on max_characters in XML = 10*/
        final EditText useridT =  (EditText) findViewById(R.id.text_userID);
        final EditText longT =    (EditText) findViewById(R.id.text_longitude);
        final EditText latiT =    (EditText) findViewById(R.id.text_latitude);
        /**Buttons*/
        final Button insertB =    (Button) findViewById(R.id.button_insert);
        final Button upgradeB =     (Button) findViewById(R.id.newDB);
        final Button moveA2  =    (Button) findViewById(R.id.moveToA2);
        /**Database Handler*/
        final DBHelper db = new DBHelper(this);

        /**Button to Insert Values from EditTexts to DB*/
        insertB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longtitude,latitude,uid;
                longtitude = longT.getText().toString();
                latitude =  latiT.getText().toString();
                uid = useridT.getText().toString();

                if (uid.equals("") || longtitude.equals("") || latitude.equals("")){
                    Toast.makeText(v.getContext(), "FAIL. Check your entries", Toast.LENGTH_SHORT).show();
                }else {

                    UserBean user = new UserBean();
                    /**Setting values in userObject from user's entries*/
                    user.setUserid(uid);
                    user.setLon(Float.parseFloat(longtitude));
                    user.setLat(Float.parseFloat(latitude));
                    user.setDt(getCurrentTimeStamp());/**Is Generated Automatically from a Function, see at the end of the file*/

                    //Debug
                    Log.v("MEH1", "INSERTING TO DB: values : "
                            + " userid: " + user.getUserid()
                            + " lon: " + user.getLon()
                            + " lat: " + user.getLat()
                            + " dt: " + user.getDt());

                    /**Run Query, check IF it Fails or Succeeds and output a corresponding Message*/
                    if (db.addUser(user) == -1) {
                        Toast.makeText(v.getContext(), "FAIL.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "SUCCESS.", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
        /**Button to start Act2*/
        moveA2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), Activity2.class);
                /**Start Act2*/
                startActivity(intent1);
            }
        });

        /**Button to Empty Database, Mostly for Debugging Purposes*/
        upgradeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = db.getDatabaseVersion();
                db.onUpgrade(db.getWritableDatabase(), x , x + 1);
                Toast.makeText(v.getContext(), "DB Updated/Cleared" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**Function to get System's Date so we can use it as User's Timestamp*/
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); /**Pattern can be easily changed*/
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}



/*
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERID, user.getUid()); // User Id
        values.put(KEY_LON, user.getLon()); // User Longitude
        values.put(KEY_LAT, user.getLat()); // User Latitude
        values.put(KEY_DT, user.getDt()); // User timestamp
        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }
    */