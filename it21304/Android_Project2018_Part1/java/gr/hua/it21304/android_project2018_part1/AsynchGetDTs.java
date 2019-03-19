package gr.hua.it21304.android_project2018_part1;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class AsynchGetDTs extends AsyncTask<String, Void, ArrayList<String>> {
    private Context context;

    public AsynchGetDTs(Context context){
        super();
        this.context = context;
    }

    /**Updating Asynchronously the DropdownList*/
    @Override
    protected ArrayList<String> doInBackground(String... string) {
        DBHelper db = new DBHelper(this.context);
        ArrayList<String> asynchResult = db.getDTs();
        return asynchResult;
    }
}
