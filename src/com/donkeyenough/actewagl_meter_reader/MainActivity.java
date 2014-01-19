package com.donkeyenough.actewagl_meter_reader;

import android.os.Bundle;
import android.app.TabActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle; 
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	/*
	 * Called when the activity is first created.
	 */	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        TabHost tabHost = getTabHost();
        
        // Tab for Gas 
        TabSpec gasspec = tabHost.newTabSpec("Gas");
        // Set Title and Icon for the Gas tab
        gasspec.setIndicator("Gas", getResources().getDrawable(R.drawable.actewagl_gas_48x48));
        Intent gasIntent = new Intent(this, GasActivity.class);
        gasspec.setContent(gasIntent);
        
        // Tab for Electricity
        TabSpec elecspec = tabHost.newTabSpec("Electricity");
        // Set Title and Icon for the Electricity tab
        elecspec.setIndicator("Electricity", getResources().getDrawable(R.drawable.actewagl_electricity_48x48));
        Intent elecIntent = new Intent(this, ElectricityActivity.class);
        elecspec.setContent(elecIntent);

        // Tab for Charts
        TabSpec chartspec = tabHost.newTabSpec("Charts");
        // Set Title and Icon for the Charts tab
        chartspec.setIndicator("Charts", getResources().getDrawable(R.drawable.achartengine));
        Intent chartIntent = new Intent(this, ChartActivity.class);
        chartspec.setContent(chartIntent);

        
        // Add all TabSpec to TabHost
        tabHost.addTab(gasspec);
        tabHost.addTab(elecspec);    
        tabHost.addTab(chartspec);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onOptionsItemSelected(item);
    }
}
