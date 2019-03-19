package gr.hua.it21304.android_project2018_part1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GPS";
    public static final String TABLE_USERS_NAME = "GPS_Users";
    public static final String KEY_ID = "id";
    public static final String KEY_USERID= "userid";
    public static final String KEY_LONGIT= "longitude";
    public static final String KEY_LATIT= "latitude";
    public static final String KEY_DT= "dt";

  //  private static final String CREATE_TABLE_USERS_NAME;

    /**Create Table Query, It also includes Constraints*/
    private static final String
        CREATE_TABLE_USERS_NAME =
                "CREATE TABLE " + TABLE_USERS_NAME + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL CHECK (length("+KEY_ID+") <= 10),"
                        + KEY_USERID + " TEXT NOT NULL CHECK (length("+KEY_USERID+") < 10),"
                        //FLOAT, DOUBLE == REAL in SQLite
                        + KEY_LONGIT + " REAL NOT NULL,"
                        + KEY_LATIT + " REAL NOT NULL,"
                        + KEY_DT + " TEXT NOT NULL CHECK (length("+KEY_DT+") < 20)"
                        + ")";

    /**Constructor*/
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**onCreate will be called when the app will be installed on the device*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL (CREATE_TABLE_USERS_NAME);
    }

    /**For upgrading/altering our Database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_NAME);
            onCreate(db);
        }
    }

    /**Getter for DatabaseVersion*/
    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }



    /**Adding User's Inputs*/
    public long addUser(UserBean user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, user.getId());
        /**Passing Values to Query*/
        values.put(KEY_USERID, user.getUserid());
        values.put(KEY_LONGIT, user.getLon());
        values.put(KEY_LATIT, user.getLat());
        values.put(KEY_DT, user.getDt());
        /**Executing Query*/
        long tmp = db.insert(TABLE_USERS_NAME, null, values);
        db.close();
        /**Returns -1 if fails*/
        return tmp;
    }


    /**Select from DB, based on args*/
    public ArrayList<UserBean> searchResults (String userid, String dt) {
        String[] tmpArgs = {userid, dt}; /** arguments for select query */

        ArrayList<UserBean> tmpArr = new ArrayList<UserBean>();
        SQLiteDatabase db = this.getReadableDatabase();

        /**Using Cursor to get and manage the ResultSet*/
        Cursor curs = db.query(TABLE_USERS_NAME, null,KEY_USERID+"=? OR "+KEY_DT+ "=?",
               tmpArgs, null, null, null);

        int len = curs.getCount();
        Log.v("MEH2","Value tou curs : " + len); //Debug

        if (curs.moveToFirst()) {
            do {
                UserBean user = new UserBean();
                /**Setting to new userObject the row's results*/
                user.setId(curs.getInt(0));
                user.setUserid(curs.getString(1));
                user.setLon(curs.getFloat(2));
                user.setLat(curs.getFloat(3));
                user.setDt(curs.getString(4));
                //Debug
                Log.v("MEH2", "Gia des ta values mia : id: " +user.getId()+
                        " userid: " +user.getUserid()+
                        " lon: " +user.getLon()+
                        " lat: " +user.getLat()+
                        " dt: " +user.getDt());

                /**Finally add userObj to a temp array */
                tmpArr.add(user);

            } while (curs.moveToNext()); /** loop until there is no NextResult to Move to */
        }
        db.close();
        /**Finally Return the temp Array*/
        return tmpArr;
    }



    /**Function to be called on start of Act2 so we can populate the required
     * DropdownList from an Array*/
    public ArrayList<String> getDTs() {

        ArrayList<String> dtList = new ArrayList<>();

        /**Add an empty value on the first cell for better User Experience*/
        dtList.add(0,"");

        SQLiteDatabase db = this.getReadableDatabase();

        /**Using Cursor to get and manage the ResultSet*/
        Cursor curs = db.query(TABLE_USERS_NAME, new String[]{"dt"},null,
                null, null, null, null);

        if (curs.moveToFirst()) {
            do {
                Log.v("MEH3","Value tou dt : "+curs.getString(0)); //Debug
                /**Add DT to Array*/
                dtList.add(curs.getString(0));

            } while (curs.moveToNext()); /** loop until there is no NextResult to Move to */
            //return dtList;
        }
        db.close();
        /**Return the Array filled with all DTs*/
        return dtList;
    }



//FOURTH ACTIVITY
    public ArrayList<UserBean> getAllUsers () {
        ArrayList<UserBean> tmpArr = new ArrayList<UserBean>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS_NAME,null,null, null,null,null,null); //Select * type of query

        if (cursor.moveToFirst()) {
            do {
                UserBean user = new UserBean();

                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID))));
                user.setUserid(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERID)));
                user.setLon(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LONGIT))));
                user.setLat(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LATIT))));
                user.setDt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DT)));

                tmpArr.add(user);
            } while (cursor.moveToNext());
        }
        db.close();
        return tmpArr;
    }
}

/**
 *
 *
 * public int updateUser(UserBean user) {
 *         SQLiteDatabase db = this.getWritableDatabase();
 *         ContentValues values = new ContentValues();
 *         values.put(KEY_USERID, user.getUserid()); // User Id
 *         values.put(KEY_LONGIT, user.getLon()); // User Longitude
 *         values.put(KEY_LATIT, user.getLat()); // User Latitude
 *         values.put(KEY_DT, user.getDt()); // User timestamp
 *         // updating row
 *         return db.update(TABLE_USERS_NAME, values, KEY_ID + " = ?",
 *                 new String[]{String.valueOf(user.getId())});
 *     }
 *
 *
 *
 public int updateUser(UserBean user) {
 SQLiteDatabase db = this.getWritableDatabase();
 ContentValues values = new ContentValues();
 values.put(KEY_USERID, user.getUserid()); // User Id
 values.put(KEY_LONGIT, user.getLon()); // User Longitude
 values.put(KEY_LATIT, user.getLat()); // User Latitude
 values.put(KEY_DT, user.getDt()); // User timestamp
 // updating row
 return db.update(TABLE_USERS_NAME, values, KEY_USERID + " = ?",
 new String[]{String.valueOf(user.getUserid())});
 }


 public List<User> getAllUsers() {
 List<User> usersList = new ArrayList<User>(); //List that will contain the results
 SQLiteDatabase db = this.getReadableDatabase(); //Get readable db instance
 Cursor cursor = db.query(TABLE_USERS,null,null,new String[]{},null,null,null); //Select * type of query
 // Looping through all rows and adding to list
 if (cursor.moveToFirst()) { //If cursor isn't empty
 do {
 User user = new User(); //Create new user object
 //Set user parameters
 user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID))));
 user.setUid(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERID)));
 user.setLon(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LON))));
 user.setLat(Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAT))));
 user.setDt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DT)));

 //Adding user to list
 usersList.add(user);
 } while (cursor.moveToNext());
 }
 //Return Users List
 return usersList;
 }

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

 **/