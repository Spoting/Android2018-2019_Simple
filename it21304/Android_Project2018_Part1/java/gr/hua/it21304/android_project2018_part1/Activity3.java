package gr.hua.it21304.android_project2018_part1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TableLayout;
import android.widget.TextView;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);


        TextView results = (TextView) findViewById(R.id.textView3); /**This textview will be used to show our Results*/
        results.setMovementMethod( new ScrollingMovementMethod()); /**Add a scrollbar to the textview, see also activity_3.xml*/

        Intent i = this.getIntent();
        /**Get passed Data from Act2*/
        String data = i.getExtras().getString("Data");
        //String matchedUserID = i.getExtras().getString("givenUID");
        //String matchedDTs = i.getExtras().getString("givenDT");
        /**Set the Data to the textview*/
        results.setText(data);
    }

}
