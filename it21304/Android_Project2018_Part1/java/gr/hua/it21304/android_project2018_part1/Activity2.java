package gr.hua.it21304.android_project2018_part1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        final Button selectB = (Button) findViewById(R.id.button_search);
        final EditText selected_useridT = (EditText) findViewById(R.id.text_selectUserID); /**Constraint on max_character in XML = 10*/
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_delete); /**Will be used for our DropdownList*/

        final DBHelper db = new DBHelper(this);

        /**Adding all Database's Timestamps to an Array. Asynchronous*/
        AsynchGetDTs asynchGetDTs = new AsynchGetDTs(this);
        try {
            //ArrayList<String> arrDT = db.getDTs(); //This will be used if we didnt want Asynch

            ArrayList<String> arrDT = asynchGetDTs.execute().get();

            /**Fill an adapter with the array's values*/
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrDT);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter); /**Set to our Spinner the Adapter with the dts*/

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        /** Debug
         for (int i = 0; i < arrDT.size(); i++) {
         Log.v("MEH4", "DT " + arrDT.get(i));
         } */


        /**Button to run Query then Start a new Act3 with passed data from Act2*/
        selectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), Activity3.class);
                String passData = "";

                String suid = selected_useridT.getText().toString(); /**Argument 1 for Query*/
                String dt = spinner.getSelectedItem().toString(); /**Argument 2 for Query*/
                ArrayList<UserBean> results = db.searchResults(suid, dt); /**Run Query and Store results to Array*/

                /**Check if there are no results*/
                if (results.size() > 0) {
                    for (int i = 0; i < results.size(); i++) {
                        /**Append each ObjectResult to a String*/
                        passData += results.get(i).toString();

                    }
                    //intent2.putExtra("givenUID", suid);
                    //intent2.putExtra("givenDT", dt);
                    /**Pass the created String to Act3*/
                    intent2.putExtra("Data", passData);
                    /**Start Act3*/
                    startActivity(intent2);
                } else {
                    Toast.makeText(v.getContext(), "Query Failed Check Your Inputs", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}


/**
 @Override
 public void onResume(){
 super.onResume();
 final Spinner spinner = (Spinner) findViewById(R.id.spinner_showResults);
 AsynchGetDTs asynchGetDTs = new AsynchGetDTs(this);
 try {
 //ArrayList<String> arrDT = db.getDTs(); //This will be used if we didnt want Asynch

 ArrayList<String> arrDT = asynchGetDTs.execute().get();


 ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrDT);
 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 adapter.notifyDataSetChanged();
 spinner.setAdapter(adapter);

 } catch (InterruptedException e) {
 e.printStackTrace();
 } catch (ExecutionException e) {
 e.printStackTrace();
 }

 }
 **/


/**
 final Button btn = (Button) findViewById(R.id.button);

 btn.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
String suid = selected_useridT.getText().toString();
String dt = spinner.getSelectedItem().toString();

UserBean user = new UserBean();

}
});
 }
 **/