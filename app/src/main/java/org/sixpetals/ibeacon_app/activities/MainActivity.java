package org.sixpetals.ibeacon_app.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.opengl.Visibility;
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
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.sixpetals.ibeacon_app.R;
import org.sixpetals.ibeacon_app.model.BeaconResponse;
import org.sixpetals.ibeacon_app.model.BeaconResponseSet;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity implements BeaconConsumer {
    private BeaconManager beaconManager;
    private BeaconParser beaconParser;
    private MainActivityFragment fragment;


    private int OUTRANGE = 0;
    private int INRANGE = 1;

    public static BeaconResponseSet BeaconSet;

    // iBeaconのデータを認識するためのParserフォーマット
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new MainActivityFragment();

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
                BeaconRangeInfo info = new BeaconRangeInfo();
                info.region = region;
                info.rangeStatus = INRANGE;
                new BeaconAsyncTask().execute(info);
            }

            @Override
            public void didExitRegion(Region region) {
                // 領域からの退場を検知
                Log.d("Beacon", "EXIT Region. ");
                BeaconRangeInfo info = new BeaconRangeInfo();
                info.region = region;
                info.rangeStatus = OUTRANGE;
                new BeaconAsyncTask().execute(info);
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                // 領域への入退場のステータス変化を検知
                Log.d("MyActivity", "DetermineState: " + i);
            }
        });

        try {
            //ビーコンマスタを読込
            if(BeaconSet == null) {
                AssetManager as = getResources().getAssets();
                InputStream is = as.open("beacon-response.json");
                BeaconResponseSet set = new BeaconResponseSet();
                set.init(is);
                BeaconSet = set;
            }

            // ビーコン情報の監視を開始
            for( BeaconResponse res : BeaconSet.findAll()){
                beaconManager.startMonitoringBeaconsInRegion(new Region(res.name, res.getBeaconId(), null, null));
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    public void showInfomation(BeaconResponse res) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("Info", res);
        startActivity(intent);
    }



    public class BeaconAsyncTask extends AsyncTask<BeaconRangeInfo, Object, BeaconRangeInfo> {

        @Override
        protected BeaconRangeInfo doInBackground(BeaconRangeInfo... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(BeaconRangeInfo result) {
            TextView tv =  (TextView)findViewById(R.id.mainMessageTextView);
            Button btn = (Button)findViewById(R.id.viewInfomaitonButton);
            if(result.rangeStatus == INRANGE) {
                BeaconResponse res  = BeaconSet.findByBeaconId(result.region.getId1());
                if (res == null) return;
                tv.setText("ビーコン「" + result.region.getUniqueId()+"」を検出しました。");

                btn.setTag(res);
                btn.setVisibility(View.VISIBLE);
            }else{
                tv.setText("ビーコン「"+ result.region.getUniqueId()+"」はレンジ外になりました。");
                btn.setTag(null);
                btn.setVisibility(View.INVISIBLE);
            }
        }



    }


    public class BeaconRangeInfo {
        Region region;
        int rangeStatus;

    }
}
