package com.growbe.growbe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MonitorActivity extends AppCompatActivity {

    // Etiquette pour le debug via les logs.
    String ETIQUETTE = "GrowBe Monitor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
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
}
