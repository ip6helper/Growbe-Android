package com.growbe.growbe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Etiquette pour le debug via les logs.
    String ETIQUETTE = "GrowBe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ETIQUETTE, "OnCreate Debug");
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

        Toast.makeText(this, "You Clicked Button found", Toast.LENGTH_LONG).show();

        Intent intentAct = new Intent(this, FoundActivity.class);
        startActivity(intentAct);
    }

    public void clickHandlerMonitor (View view)  {

        Toast.makeText(this, "You Clicked Button monitor", Toast.LENGTH_LONG).show();

        Intent intentAct = new Intent(this, MonitorActivity.class);
        startActivity(intentAct);
    }

}

