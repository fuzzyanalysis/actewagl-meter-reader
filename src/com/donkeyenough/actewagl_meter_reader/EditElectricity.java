package com.donkeyenough.actewagl_meter_reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditElectricity extends Activity {

	private DatePicker mDatePicker;
	private EditText mElec04Text;
	private EditText mElec05Text;
	private EditText mElec06Text;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_electricity);
		setTitle(R.string.edit_reading);
		
		mDatePicker = (DatePicker) findViewById(R.id.electricitydoedp);		
		mElec04Text = (EditText) findViewById(R.id.elec04);
		mElec05Text = (EditText) findViewById(R.id.elec05);
		mElec06Text = (EditText) findViewById(R.id.elec06);
		
		Button confirmButton = (Button) findViewById(R.id.confirm);
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
		    String doe = extras.getString(ElectricityDbAdapter.KEY_DOE);
		    String elec04 = extras.getString(ElectricityDbAdapter.KEY_ELEC04);
		    String elec05 = extras.getString(ElectricityDbAdapter.KEY_ELEC05);
		    String elec06 = extras.getString(ElectricityDbAdapter.KEY_ELEC06);
		    mRowId = extras.getLong(ElectricityDbAdapter.KEY_ROWID);

		    if (doe != null) {
		    	// set the datePicker to the date value, doe
		    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		    	Calendar cal = Calendar.getInstance();

		    	Date date;
				try {
					date = sdf.parse(doe);
					cal.setTime(date);
			    	mDatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			    	
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		    }
		    if (elec04 != null) {
		        mElec04Text.setText(elec04);
		    }
		    if (elec05 != null) {
		        mElec05Text.setText(elec05);
		    }
		    if (elec04 != null) {
		        mElec06Text.setText(elec06);
		    }
		    
//	        TextView statsTextView = (TextView)findViewById(R.id.electricity_stats);
//	        String avg = avgDailyElecUsage();
//	        String cost = avgDailyElecCost();
//	        String period = avgDailyElecLastPeriod();
//	        statsTextView.setText("Average Daily Electricity Usage: " + avg + " kWh");
//	        statsTextView.append("\n" + "Average Daily Cost: " + cost);
//	        statsTextView.append("\n" + "Average Cost Last Period: " + period);

		}
				
		confirmButton.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View view) {
		    	Bundle bundle = new Bundle();

				//mDoeText = (EditText) findViewById(R.id.doe);
		    	mDatePicker = (DatePicker)findViewById(R.id.electricitydoedp);
		    	int day = mDatePicker.getDayOfMonth();
		    	int month = mDatePicker.getMonth() + 1;
		    	int year = mDatePicker.getYear();
		    	
		    	String sDate = day + "." + month + "." + year;
		    	
				mElec04Text = (EditText) findViewById(R.id.elec04);
				mElec05Text = (EditText) findViewById(R.id.elec05);
				mElec06Text = (EditText) findViewById(R.id.elec06);
		    			    	
		    	bundle.putString(ElectricityDbAdapter.KEY_DOE, sDate);
		    	bundle.putString(ElectricityDbAdapter.KEY_ELEC04, mElec04Text.getText().toString());
		    	bundle.putString(ElectricityDbAdapter.KEY_ELEC05, mElec05Text.getText().toString());
		    	bundle.putString(ElectricityDbAdapter.KEY_ELEC06, mElec06Text.getText().toString());
		    	if (mRowId != null) {
		    	    bundle.putLong(ElectricityDbAdapter.KEY_ROWID, mRowId);
		    	}
		    	Intent mIntent = new Intent();
		    	mIntent.putExtras(bundle);
		    	setResult(RESULT_OK, mIntent);
		    	finish();
		    }

		});
		
	}
		
	
    private String avgDailyElecUsage(){
    	
    	//FIXME: Add calculateAverageDailyElectricityUsage logic
    	
    	return "0.2";
    }
    
    private String avgDailyElecCost(){
    	
    	//FIXME: Add calculateAverageDailyElectricityCost logic	    	
    	
    	return "$1.20";
    }
    
    private String avgDailyElecLastPeriod() {
    	//FIXME: Add avgDailyElecLastPeriod() logic
    	
    	return "$1.23";
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        this.finish(); 
	        return false;
	    }

	    return super.onKeyDown(keyCode, event);
	}

}
