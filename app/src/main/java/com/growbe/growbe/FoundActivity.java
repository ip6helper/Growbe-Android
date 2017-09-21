package com.growbe.growbe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FoundActivity extends AppCompatActivity {

    TextView mainText;
    WifiManager wifiMgmt;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        mainText = (TextView) findViewById(R.id.textViewFound);

        mainText.setMovementMethod(new ScrollingMovementMethod());

        wifiMgmt = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

        if (wifiMgmt.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();

            wifiMgmt.setWifiEnabled(true);
        }

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMgmt.startScan();
        mainText.setText("Starting Scan...");
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            wifiList = wifiMgmt.getScanResults();
            sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");

            for(int i = 0; i < wifiList.size(); i++){

                //sb.append(new Integer(i+1).toString() + ". ");
                //sb.append((wifiList.get(i)).toString());
                //sb.append("\n\n");

                sb.append(wifiList.get(i).SSID.toString());
                sb.append("\n\n");
            }

            mainText.setText(sb);
        }

    }

    public void clickHandlerSelect (View view)  {

        Button button = (Button)view;

        // The SSID is embedded in the button name.
        String TextSSID = button.getText().toString();

        Intent intentAct = new Intent(this, ProvActivity.class);

        // Pass the SSID to the next Activity.
        intentAct.putExtra("EXTRA_SSID", TextSSID);

        startActivity(intentAct);
    }

}
