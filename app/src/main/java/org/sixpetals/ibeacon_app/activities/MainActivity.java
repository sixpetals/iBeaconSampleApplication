package org.sixpetals.ibeacon_app.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.sixpetals.ibeacon_app.R;


public class MainActivity extends ActionBarActivity implements BeaconConsumer {
    private BeaconManager beaconManager;
    private BeaconParser beaconParser;
    private PlaceholderFragment fragment;

    // iBeaconのデータを認識するためのParserフォーマット
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new  PlaceholderFragment();

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);


        // BeaconParseを設定
        beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout(IBEACON_FORMAT);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(beaconParser);

    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                // 領域への入場を検知
                Log.d("Beacon", "ENTER Region.");

               new BeaconAsyncTask().execute("ビーコン発見" + region.getUniqueId());
            }

            @Override
            public void didExitRegion(Region region) {
                // 領域からの退場を検知
                Log.d("Beacon", "EXIT Region. ");
                new BeaconAsyncTask().execute("ビーコン喪失");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                // 領域への入退場のステータス変化を検知
                Log.d("MyActivity", "DetermineState: " + i);
            }
        });

        try {
            // ビーコン情報の監視を開始
            beaconManager.startMonitoringBeaconsInRegion(new Region("unique-id-001", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public class BeaconAsyncTask extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv =  (TextView)findViewById(R.id.mainMessageTextView);
            tv.setText(result);
        }
    }
}
