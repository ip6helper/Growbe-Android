package com.growbe.growbe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FoundActivity extends AppCompatActivity {

    String ETIQUETTE = "GrowBe Found";

    WifiManager wifiMgmt;
    List<ScanResult> wifiList;

    WifiReceiver receiverWifi;
    TextView text;
    ListView list;
    String wifis[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);
        Log.i(ETIQUETTE, "onCreate Debug");

        text = (TextView)findViewById(R.id.textViewProv);
        list = (ListView)findViewById(R.id.list);

        wifiMgmt = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

        // Verify if the WIFI is enable, if not open it.
        if (!wifiMgmt.isWifiEnabled())
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled... making it enabled",
                    Toast.LENGTH_LONG).show();

            wifiMgmt.setWifiEnabled(true);
        }

        // Register the BroadcastReceiver to handle the WIFI scan.
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Ask the WIFI manager to start the scan.
        wifiMgmt.startScan();

        // Listening to single list item on click.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the selected item name.
                String TextSSID = ((TextView) view).getText().toString();

                //Start the activity to provision the selected GrowBe.
                Intent intentAct = new Intent(FoundActivity.this, ProvActivity.class);
                // Pass the SSID to the next Activity.
                intentAct.putExtra("EXTRA_SSID", TextSSID);
                startActivity(intentAct);
            }
        });
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            wifiList = wifiMgmt.getScanResults();

            // Initialize the wifis array size.
            //wifis = new String[wifiList.size()];

            int nbrOfGrowBe = 0;

            Log.i(ETIQUETTE, "Scan result found SSID: " + wifiList.size() + " wifi");

            for (int i = 0; i < wifiList.size(); i++) {

                Log.i(ETIQUETTE, "Scan result, found SSID: " + wifiList.get(i).SSID);

                String currentSSID = wifiList.get(i).SSID;

                //wifis[i] = wifiList.get(i).SSID;

                if (currentSSID.contains("GrowBe-")) {
                    // Save the number of GrowBe found.
                    nbrOfGrowBe++;
                }
            }

            Log.i(ETIQUETTE, "Scan result found: " + nbrOfGrowBe + " GrowBe");

            wifis = new String[nbrOfGrowBe];

            for (int i = 0, j=0; i < wifiList.size() && j < nbrOfGrowBe ; i++) {

                Log.i(ETIQUETTE, "Scan result, found SSID: " + wifiList.get(i).SSID);

                String currentSSID = wifiList.get(i).SSID;

                if (currentSSID.contains("GrowBe-")) {
                    // Save the number of GrowBe found.
                    wifis[j] = wifiList.get(i).SSID;
                    j++;
                }
            }

            if (nbrOfGrowBe != 0){

                list.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.label, wifis));
            }
            else {
                text.setText(R.string.no_growbe);
                text.setTextColor(Color.RED);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ETIQUETTE, "onStart Debug");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ETIQUETTE, "onRestart Debug");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ETIQUETTE, "onResume Debug");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ETIQUETTE, "onStop Debug");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ETIQUETTE, "onDestroy Debug");
    }

}
