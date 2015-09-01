package org.sixpetals.ibeacon_app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.sixpetals.ibeacon_app.R;
import org.sixpetals.ibeacon_app.model.BeaconResponse;

import java.io.IOException;
import java.io.InputStream;

public class QuestionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);





    }

    @Override
    public void onStart(){
        super.onStart();

        Intent intent = getIntent();
        if(intent != null) {
            BeaconResponse res = (BeaconResponse) intent.getSerializableExtra("Info");
            TextView tv = (TextView) findViewById(R.id.questionTitleView);
            ImageView iv = (ImageView) findViewById(R.id.questionImageView);

            tv.setText(res.text);

            try {
                InputStream ist = getResources().getAssets().open(res.imageFileName);
                Bitmap bitmap = BitmapFactory.decodeStream(ist);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.d("Assets", "Error");
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
