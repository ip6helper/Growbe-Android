package com.growbe.growbe;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProvActivity extends AppCompatActivity {

    String ETIQUETTE = "GrowBe Prov";

    WifiManager wifiMgmt;
    Thread      Thread1 = null;
    String      networkSSID;
    //String      networkPass = "12345678";

    EditText    devName;
    EditText    idNet;
    EditText    psk;
    EditText    local;
    EditText    internet;

    Spinner     AuthSpinner;
    List<String>    authList;


    public static final String  SERVERIP = "192.168.10.100";
    public static final int     SERVERPORT = 80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prov);

        Log.i(ETIQUETTE, "onCreate Debug");

        // Get SSID from the previous Activity.
        networkSSID = getIntent().getStringExtra("EXTRA_SSID");
        Log.d(ETIQUETTE, "You provide the SSID: " + networkSSID);

        // Get ressources references.

        devName     = (EditText) findViewById(R.id.deviceName);
        idNet       = (EditText) findViewById(R.id.ssidName);
        psk         = (EditText) findViewById(R.id.preSharedKey);
        local       = (EditText) findViewById(R.id.localServer);
        internet    = (EditText) findViewById(R.id.internetServer);
        AuthSpinner = (Spinner) findViewById(R.id.authSpinner);


        // Fill-up the Authentication spinner.
        authList = new ArrayList<>();

        authList.add("WPA2-home");
        authList.add("WPA2-Entreprise");
        authList.add("WEP");

        // Creating Adapter for Spinner.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authList);
        // Drop down layout style.
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to spinner.
        AuthSpinner.setAdapter(dataAdapter);

        // Get the latest values enter by the user to speedup multiple device provisioning.
        SharedPreferences userPrefs = this.getSharedPreferences("userPrefs", MODE_PRIVATE );

        int userPrefsSize = userPrefs.getAll().size();

        String userPrefsString = "Sizeof of userPrefs: "+userPrefsSize;
        Log.i(ETIQUETTE, userPrefsString);

        if (userPrefsSize != 0) {

            String deviceName = userPrefs.getString("deviceName", "");
            devName.setText(deviceName);

            String ssidName = userPrefs.getString("ssidName", "");
            idNet.setText(ssidName);

            String preSharedKey = userPrefs.getString("preSharedKey", "");
            psk.setText(preSharedKey);

            String localServer = userPrefs.getString("localServer", "");
            local.setText(localServer);

            String internetServer = userPrefs.getString("internetServer", "");
            internet.setText(internetServer);
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

    public void clickHandlerProv (View view)  {

        // Start the thread to send all the configuration to the GrowBe device.
        this.Thread1 = new Thread(new CfgThread());
        this.Thread1.start();

        // Save configuration to the preference file.

        SharedPreferences userPrefs = this.getSharedPreferences("userPrefs", MODE_PRIVATE );
        SharedPreferences.Editor  mEditor = userPrefs.edit();

        mEditor.putString("deviceName", devName.getText().toString());
        mEditor.putString("ssidName", idNet.getText().toString());
        mEditor.putString("preSharedKey", psk.getText().toString());
        mEditor.putString("localServer", local.getText().toString());

        mEditor.putString("internetServer", internet.getText().toString());
        mEditor.apply();
    }

    private class CfgThread implements Runnable {

        public void run() {

            Socket socket;

            wifiMgmt = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

            WifiInfo wifiInfo = wifiMgmt.getConnectionInfo();

            String wifiName = wifiInfo.getSSID();

            int wifiIP = wifiInfo.getIpAddress();


            String myIP = String.format(Locale.getDefault(), "%d.%d.%d.%d", (wifiIP & 0xff),
                    (wifiIP >> 8 & 0xff),
                    (wifiIP >> 16 & 0xff),
                    (wifiIP >> 24 & 0xff));

            DhcpInfo dhcp = wifiMgmt.getDhcpInfo();
            int wifiGateway = dhcp.gateway;

            String myGateway = String.format(Locale.getDefault(), "%d.%d.%d.%d", (wifiGateway & 0xff),
                    (wifiGateway >> 8 & 0xff),
                    (wifiGateway >> 16 & 0xff),
                    (wifiGateway >> 24 & 0xff));

            String NetInfo = wifiName+"\n"+myIP+"\n"+myGateway;

            Log.d(ETIQUETTE, "Current setting: " + NetInfo);

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                socket = new Socket(serverAddr, SERVERPORT);

                if (socket.isConnected()){

                    // TODO: Loop here to send all data the the device.
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                    String devString = "<01 value="+devName.getText().toString()+" />";
                    printWriter.println(devString);
                    Log.d(ETIQUETTE, devString);

                    String idString = "<02 value="+idNet.getText().toString()+" />";
                    printWriter.println(idString);
                    Log.d(ETIQUETTE, idString);

                    String pskString = "<03 value="+psk.getText().toString()+" />";
                    printWriter.println(pskString);
                    Log.d(ETIQUETTE, pskString);

                    String localString = "<04 value="+local.getText().toString()+" />";
                    printWriter.println(localString);
                    Log.d(ETIQUETTE, localString);

                    String internetString = "<05 value="+internet.getText().toString()+" />";
                    printWriter.println(internetString);
                    Log.d(ETIQUETTE, internetString);

/*
                    //Test to wait 250 between each string...

                    try {
                        synchronized(this){
                            wait(250);
                        }
                    }
                    catch(InterruptedException ex){
                    }

*/
                    socket.close();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
