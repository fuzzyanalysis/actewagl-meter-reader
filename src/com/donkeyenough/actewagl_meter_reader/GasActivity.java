package com.donkeyenough.actewagl_meter_reader;

import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GasActivity extends ListActivity {

    private GasDbAdapter mDbHelper;
    private Cursor mReadingsCursor;
    
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
	    
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);
        mDbHelper = new GasDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        
        TextView statsTextView = (TextView)findViewById(R.id.gas_stats);
        String avg = avgDailyGasUsage();
        String cost = avgDailyGasCost();
        String period = avgDailyElecLastPeriod();
        statsTextView.setText("Average Daily Gas Usage: " + avg + " cubic meters.");
        statsTextView.append("\n" + "Average Daily Cost: " + cost);
        statsTextView.append("\n" + "Average Cost Last Period: " + period);        
        
        final ProgressDialog myPd_ring=ProgressDialog.show(GasActivity.this, "Please wait", "Loading please wait..", true);
        myPd_ring.setCancelable(true);
        new Thread(new Runnable() {  
              @Override
              public void run() {
                    // TODO Auto-generated method stub
                    try
                    {
                          Thread.sleep(5000);
                    }catch(Exception e){}
                    myPd_ring.dismiss();
              }
        }).start();     
    }
    
    private String avgDailyGasUsage(){
    	
    	//FIXME: Add calculateAverageDailyGasUsage logic
    	
    	if(mReadingsCursor == null)
    	{
    		mReadingsCursor = mDbHelper.fetchAllReadings();
    	}
    	
    	
    	
    	return "0.2";
    }
    
    private String avgDailyGasCost(){
    	
    	//FIXME: Add calculateAverageDailyGasCost logic
    	
    	return "$1.20";
    }
    
    private String avgDailyElecLastPeriod() {
    	//FIXME: Add avgDailyGasLastPeriod() logic
    	
    	return "1.24";
    }
    

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbHelper.deleteReading(info.id);
            fillData();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
    	menu.add(0, INSERT_ID, 0, R.string.menu_gas_insert);
        // TODO Auto-generated method stub
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            createReading();
            return true;
        }
       
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // do something on back.
    	super.onBackPressed();
    }
    
    private void createReading() {
    	/*
    	Date dt = new Date(); 
    	String reading = Integer.toString(mReadingNumber++);
    	String readingDOE = dt.toString();
    	mDbHelper.createReading(readingDOE, reading);
    	fillData();
    	*/
    	Intent i = new Intent(this, EditGas.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mReadingsCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, EditGas.class);
        i.putExtra(GasDbAdapter.KEY_ROWID, id);
        i.putExtra(GasDbAdapter.KEY_DOE, c.getString(
                c.getColumnIndexOrThrow(GasDbAdapter.KEY_DOE)));
        i.putExtra(GasDbAdapter.KEY_GAS, c.getString(
                c.getColumnIndexOrThrow(GasDbAdapter.KEY_GAS)));
        startActivityForResult(i, ACTIVITY_EDIT);     
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        Bundle extras = intent.getExtras();

        switch(requestCode) {
        case ACTIVITY_CREATE:
            String doe = extras.getString(GasDbAdapter.KEY_DOE);
            String gas = extras.getString(GasDbAdapter.KEY_GAS);
            mDbHelper.createReading(doe, gas);
            fillData();
            break;
        case ACTIVITY_EDIT:
            Long mRowId = extras.getLong(GasDbAdapter.KEY_ROWID);
            if (mRowId != null) {
                String editDoe = extras.getString(GasDbAdapter.KEY_DOE);
                String editGas = extras.getString(GasDbAdapter.KEY_GAS);
                mDbHelper.updateReading(mRowId, editDoe, editGas);
            }
            fillData();
            break;
        }
        
    }
    
    private void fillData() {
    	// Get all of the readings from the database and create the item list
    	mReadingsCursor = mDbHelper.fetchAllReadings();
    	startManagingCursor(mReadingsCursor);
    	
        String[] from = new String[] {ElectricityDbAdapter.KEY_DOE};
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter readings =
            new SimpleCursorAdapter(this, R.layout.row_gas, mReadingsCursor, from, to);
        setListAdapter(readings);
    }
    
}
