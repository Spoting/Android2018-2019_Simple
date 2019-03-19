package gr.hua.it21304.android_project2018_part1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static gr.hua.it21304.android_project2018_part1.DBHelper.KEY_USERID;

/** ContentProvider to give access of our DB to other Apps */
public class DBProvider extends ContentProvider {


    private static final String AUTHORITY = "gr.hua.it21304.android_project2018_part1.dbprovider";
    private static final String PATH = "location";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /**Path for table */
        sUriMatcher.addURI(AUTHORITY, PATH, 1);

        /**Path that matches a string of any valid characters of any length (/*) .
         * Used to query with String Args*/
        sUriMatcher.addURI(AUTHORITY, PATH + "/*", 2);
    }

    DBHelper mDBHelper;

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case 1: /**Use first url only since we dont need any args in our Apps inserts (For Service App)*/
                id = mDB.insert(DBHelper.TABLE_USERS_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Content URI pattern not recognizable: " + uri);
        }
        return Uri.parse(uri.toString() + "/" + id);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        Cursor mCursor;
        switch (sUriMatcher.match(uri)) {
            case 1: /** To query everything from Database */
                mCursor = mDB.query(DBHelper.TABLE_USERS_NAME, null, null, null, null, null, null);
                break;
            case 2:
                    /** To query with specific USERID from Database (For GoogleMap Application)*/
                selection = KEY_USERID+"=?";
                selectionArgs[0] = uri.getLastPathSegment();
                mCursor = mDB.query(DBHelper.TABLE_USERS_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Content URI pattern not recognizable: " + uri);
        }
        return mCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }
}

/*

    private Context context;
    private DBHelper db;
    static final String PROVIDER_NAME = "gr.hua.it21304.android_project2018_part1.DBProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/GPS_Users";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "GPS_Users", 1);
        uriMatcher.addURI(PROVIDER_NAME, "GPS_USERS/#", 2);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        DBHelper db = new DBHelper(context);
        db.getWritableDatabase();
        //db.getAllUsers();

        return (db == null) ? false : true;
                }

@Override
public Uri insert(Uri uri, ContentValues values) {
        UserBean user = new UserBean(1, "qwe", 123, 321, "lalala");
        long rowID = db.addUser(user);
        if (rowID > 0) {
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
        }

@Override
public Cursor query(Uri uri, String[] projection,
        String selection, String[] selectionArgs, String sortOrder) {
        //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //qb.setTables(this.db.TABLE_USERS_NAME);
        ArrayList<UserBean> usersArr = db.getAllUsers();
        Log.v("QUERYBACKEND", "Gia des ta values mia : id: " + usersArr.get(0).getId() +
        " userid: " + usersArr.get(0).getUserid() +
        " lon: " + usersArr.get(0).getLon() +
        " lat: " + usersArr.get(0).getLat() +
        " dt: " + usersArr.get(0).getDt());


        if (sortOrder == null || sortOrder == "") {

             // By default sort on student names

            sortOrder = NAME;

        }
        return null;
        }
@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        return count;
        }

@Override
public int update(Uri uri, ContentValues values,
        String selection, String[] selectionArgs) {
        int count = 0;
        return count;
        }

@Override
public String getType(Uri uri) {
        return null;
        }
 */