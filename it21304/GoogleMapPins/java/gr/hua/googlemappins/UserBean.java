package gr.hua.googlemappins;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean representing the user's location and metadata
 * Implements Parcelable so we can pass our ArrayList to Maps via Intent
 */

public class UserBean implements Parcelable {


    private float lon, lat;
    private String userid, dt;

    /**
     * Constructor
     */
    public UserBean() {

    }
/** For Parcel ->
 *              https://developer.android.com/reference/android/os/Parcelable
 */

    private UserBean(Parcel in) {
        userid = in.readString();
        lon = in.readFloat();
        lat = in.readFloat();
        dt = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(userid);
        dest.writeFloat(lon);
        dest.writeFloat(lat);
        dest.writeString(dt);

    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        public UserBean[] newArray(int size) {
            return new UserBean[size];

        }
    };

    /**
     * Getters_Setters
     */


    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }


}

