package com.donkeyenough.actewagl_meter_reader;

import com.donkeyenough.actewagl_meter_reader.ElectricityDbAdapter;
import com.donkeyenough.actewagl_meter_reader.ElectricityReading;
import com.donkeyenough.actewagl_meter_reader.GlobalVariables;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ElectricityActivity extends ListActivity {

    private ElectricityDbAdapter mDbHelper;
    private Cursor mReadingsCursor;
    
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private double totalElectricity;
    private double avgDailyCost;
    private double avgPeriodCost;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        mDbHelper = new ElectricityDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        
    }
    
    private void getStats() throws ParseException{
    	
    	//FIXME: Add calculateAverageDailyGasUsage logic
    	
    	if(mReadingsCursor == null)
    	{
    		mReadingsCursor = mDbHelper.fetchAllReadings();
    	}
    	
    	List<ElectricityReading> lea = new ArrayList<ElectricityReading>();

    	DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    	Calendar cal = Calendar.getInstance();
    	DateTime date;
    	
    	if (mReadingsCursor.moveToFirst()){
    		do{
    			ElectricityReading er = new ElectricityReading();    		
    			er.setDoe(mReadingsCursor.getString(mReadingsCursor.getColumnIndex(ElectricityDbAdapter.KEY_DOE)));
    			er.setElec04(mReadingsCursor.getString(mReadingsCursor.getColumnIndex(ElectricityDbAdapter.KEY_ELEC04)));
    			er.setElec05(mReadingsCursor.getString(mReadingsCursor.getColumnIndex(ElectricityDbAdapter.KEY_ELEC05)));
    			er.setElec06(mReadingsCursor.getString(mReadingsCursor.getColumnIndex(ElectricityDbAdapter.KEY_ELEC06)));
    			lea.add(er);
    			 
    		      // do what ever you want here
    		   }while(mReadingsCursor.moveToNext());
    		}
    	mReadingsCursor.close();
    	
    	if(lea.size()>0)
    	{    	
	    	ElectricityReading earliest = null;
	    	ElectricityReading latest = null;
	    	ElectricityReading secondLatest = null;
	    	DateTime earliestDT, latestDT, secondLatestDT;
	    	
	    	for(ElectricityReading er: lea)
	    	{
				try {	
					//cal.setTime(date);
					if(earliest == null || latest == null){
						earliest = er;
						secondLatest = er;
						latest = er;
					}
					else{
						
						earliestDT = new DateTime(sdf.parse(earliest.getDoe().toString()));
						latestDT = new DateTime(sdf.parse(latest.getDoe().toString()));
						secondLatestDT = new DateTime(sdf.parse(secondLatest.getDoe().toString()));
						date = new DateTime(sdf.parse(er.getDoe().toString()));
						
						if(date.isBefore(earliestDT))
						{
							earliest = er;
						}
						else if((date.isAfter(earliestDT) && date.isBefore(secondLatestDT)))
						{
							// do nothing
						}
						else if(date.isAfter(secondLatestDT) && date.isBefore(latestDT))
						{
							secondLatest = er;
						}
						else if(date.isAfter(latestDT))
						{
							secondLatest = latest;
							latest = er;
						}
					}
							    	
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
	    	}
	    	
	    	// get difference in dates between earliest and latest, then multiply by usage
	    	
	    	DateTime edt = new DateTime(sdf.parse(earliest.getDoe().toString()));
	    	DateTime ldt = new DateTime(sdf.parse(latest.getDoe().toString()));
	    	DateTime sldt = new DateTime(sdf.parse(secondLatest.getDoe().toString()));
	    	
	    	int totalDays = (Days.daysBetween(edt, ldt)).getDays(); 
	    	int periodDays = (Days.daysBetween(edt,  sldt)).getDays();
	    	
	    	try{
	    		
	    		int totalElec04 = Integer.parseInt(latest.getElec04()) - Integer.parseInt(earliest.getElec04());
	    		int totalElec05 = Integer.parseInt(latest.getElec05()) - Integer.parseInt(earliest.getElec05());
	    		int totalElec06 = Integer.parseInt(latest.getElec06()) - Integer.parseInt(earliest.getElec06());
	    		
	    		int periodElec04 = Integer.parseInt(latest.getElec04()) - Integer.parseInt(secondLatest.getElec04());
	    		int periodElec05 = Integer.parseInt(latest.getElec05()) - Integer.parseInt(secondLatest.getElec05());
	    		int periodElec06 = Integer.parseInt(latest.getElec06()) - Integer.parseInt(secondLatest.getElec06());
	    		
	    		
		    	double totalElecCost = 
		    			totalDays*GlobalVariables.getElec04Rate()*totalElec04 + 
		    			totalDays*GlobalVariables.getElec05Rate()*totalElec05 +
		    			totalDays*GlobalVariables.getElec06Rate()*totalElec06;
		    	
		    	double periodElecCost = 
		    			periodDays*GlobalVariables.getElec04Rate()*periodElec04 + 	    				
		    			periodDays*GlobalVariables.getElec05Rate()*periodElec05 +
		    			periodDays*GlobalVariables.getElec06Rate()*periodElec06;
		
		    	avgDailyCost = totalElecCost/totalDays;
		    	avgPeriodCost = periodElecCost/periodDays;
	    		
	    	}
	    	catch(Exception ex){
	    		
	    	}
	    	
    	}
    	
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
    	menu.add(0, INSERT_ID, 0, R.string.menu_electricity_insert);
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
    	Intent i = new Intent(this, EditElectricity.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mReadingsCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, EditElectricity.class);
        i.putExtra(ElectricityDbAdapter.KEY_ROWID, id);
        i.putExtra(ElectricityDbAdapter.KEY_DOE, c.getString(
                c.getColumnIndexOrThrow(ElectricityDbAdapter.KEY_DOE)));
        i.putExtra(ElectricityDbAdapter.KEY_ELEC04, c.getString(
                c.getColumnIndexOrThrow(ElectricityDbAdapter.KEY_ELEC04)));
        i.putExtra(ElectricityDbAdapter.KEY_ELEC05, c.getString(
                c.getColumnIndexOrThrow(ElectricityDbAdapter.KEY_ELEC05)));
        i.putExtra(ElectricityDbAdapter.KEY_ELEC06, c.getString(
                c.getColumnIndexOrThrow(ElectricityDbAdapter.KEY_ELEC06)));
        startActivityForResult(i, ACTIVITY_EDIT);     
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        Bundle extras = intent.getExtras();

        switch(requestCode) {
        case ACTIVITY_CREATE:
            String doe = extras.getString(ElectricityDbAdapter.KEY_DOE);
            String elec04 = extras.getString(ElectricityDbAdapter.KEY_ELEC04);
            String elec05 = extras.getString(ElectricityDbAdapter.KEY_ELEC05);
            String elec06 = extras.getString(ElectricityDbAdapter.KEY_ELEC06);
            mDbHelper.createReading(doe, elec04, elec05, elec06);
            fillData();
            break;
        case ACTIVITY_EDIT:
            Long mRowId = extras.getLong(ElectricityDbAdapter.KEY_ROWID);
            if (mRowId != null) {
                String editDoe = extras.getString(ElectricityDbAdapter.KEY_DOE);
                String editElec04 = extras.getString(ElectricityDbAdapter.KEY_ELEC04);
                String editElec05 = extras.getString(ElectricityDbAdapter.KEY_ELEC05);
                String editElec06 = extras.getString(ElectricityDbAdapter.KEY_ELEC06);
                mDbHelper.updateReading(mRowId, editDoe, editElec04, editElec05, editElec06);
            }
            fillData();
            break;
        }
        
    }
    
    private void fillData() {
    	
    	
        TextView statsTextView = (TextView)findViewById(R.id.electricity_stats);
		try {
			getStats();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        statsTextView.setText("Average Daily Gas Usage: " + Double.toString(totalElectricity) + " cubic meters.");
        statsTextView.append("\n" + "Average Daily Cost: $" + Double.toString(avgDailyCost));
        statsTextView.append("\n" + "Average Cost Last Period: $" + Double.toString(avgPeriodCost));
    	
    	
    	// Get all of the readings from the database and create the item list
    	mReadingsCursor = mDbHelper.fetchAllReadings();
    	startManagingCursor(mReadingsCursor);
    	
        String[] from = new String[] {ElectricityDbAdapter.KEY_DOE};
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter readings =
            new SimpleCursorAdapter(this, R.layout.row_electricity, mReadingsCursor, from, to);
        setListAdapter(readings);
         
    }
    
}
