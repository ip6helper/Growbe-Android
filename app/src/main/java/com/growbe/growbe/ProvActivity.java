package com.growbe.growbe;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

public class ProvActivity extends AppCompatActivity {

    String ETIQUETTE = "GrowBe Prov";

    WifiManager wifiMgmt;
    Thread      Thread1 = null;
    String      networkSSID;
    String      networkPass = "12345678";

    public static final String  SERVERIP = "192.168.4.100";
    public static final int     SERVERPORT = 80;

    private Spinner AuthSpinner;
    private List<String> auths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prov);

        Log.i(ETIQUETTE, "onCreate Debug");

        // Get SSID from the previous Activity.
        networkSSID = getIntent().getStringExtra("EXTRA_SSID");
        Log.d(ETIQUETTE, "You provide the SSID: " + networkSSID);

        // Fill-up the Authentication spinner.
        AuthSpinner = (Spinner) findViewById(R.id.AuthSpinner);
        auths = new ArrayList<String>();

        auths.add("WPA2-home");
        auths.add("WPA2-Entreprise");
        auths.add("WEP");

        // Creating Adapter for Spinner.
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auths);
        // Drop down layout style.
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching data adapter to spinner.
        AuthSpinner.setAdapter(dataAdapter);

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
    }

    private class CfgThread implements Runnable {

        public void run() {

            Socket socket;

            wifiMgmt = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

            WifiInfo wifiInfo = wifiMgmt.getConnectionInfo();

            String wifiName = wifiInfo.getSSID();

            int wifiIP = wifiInfo.getIpAddress();

            String myIP = String.format("%d.%d.%d.%d", (wifiIP & 0xff),
                    (wifiIP >> 8 & 0xff),
                    (wifiIP >> 16 & 0xff),
                    (wifiIP >> 24 & 0xff));

            DhcpInfo dhcp = wifiMgmt.getDhcpInfo();
            int wifiGateway = dhcp.gateway;

            String myGateway = String.format("%d.%d.%d.%d", (wifiGateway & 0xff),
                    (wifiGateway >> 8 & 0xff),
                    (wifiGateway >> 16 & 0xff),
                    (wifiGateway >> 24 & 0xff));

            String NetInfo = wifiName+"\n"+myIP+"\n"+myGateway;

            Log.d("GrowBe", "Current setting: " + NetInfo);

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                socket = new Socket(serverAddr, SERVERPORT);

                if (socket.isConnected()){

                    // TODO: Loop here to send all data the the device.

                    String toSend = "<PROV><01>HOME_WIFI<01>/" +
                                    "<02>ThisIsAPassowrd35!<02>/" +
                                    "<03>3<03>/" +
                                    "<04>HOME_WIFI<04>/";

                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(toSend);
/*
                    //Test to wait 250 between each string...

                    try {
                        synchronized(this){
                            wait(250);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    String toSend1 = "<05>ThisIsAPassowrd35!<05>/" +
                                    "<06>3<06>/" +
                                    "<07>HOME_WIFI<07>/" +
                                    "</PROV>";

                    printWriter.println(toSend1);
*/
                    socket.close();
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
