package gr.hua.it21304.android_project2018_part1;
/** Try to Implement Serializable so we can pass objectUser from Act2 to Act3,
 * making us not passing just a String but an Object.
 *
 * import java.io.Serializable;
 */

/** Simple Bean representing the user's entry*/

public class UserBean {

    private int id;
    private float lon, lat;
    private String userid, dt;

    /**Constructor*/
    public UserBean (){

    }
    /**Constructor2*/
    public UserBean(int id, String userid, float lon, float lat, String dt) {
        this.id = id;
        this.userid = userid;
        this.lon = lon;
        this.lat = lat;
        this.dt = dt;

    }
    /** Stringtify our Object */
    @Override
    public String toString() {
        return //"UserBean{" +
                //"id=" + id +
                "-----------------------\n" +
                        "{ userid=" + userid +
                        ", lon=" + lon +
                        ", lat=" + lat +
                        ", dt=" + dt +
                        "}" + "\n";
    }


    /**Getters_Setters*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
