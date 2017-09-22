package com.growbe.growbe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // Etiquette pour le debug via les logs.
    String ETIQUETTE = "GrowBe Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ETIQUETTE, "onCreate Debug");
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

    public void clickHandlerFound (View view)  {

        //Start the activity to discover all GrowBe around the Android device.

        Intent intentAct = new Intent(this, FoundActivity.class);
        startActivity(intentAct);
    }

    public void clickHandlerMonitor (View view)  {

        //Start the activity to Monitor all GrowBe around the Android device.

        Intent intentAct = new Intent(this, MonitorActivity.class);
        startActivity(intentAct);
    }

}

