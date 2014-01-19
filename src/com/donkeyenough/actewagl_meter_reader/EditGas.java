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

public class EditGas extends Activity {

	private DatePicker mDatePicker;
	private EditText mGasText;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_gas);
		setTitle(R.string.edit_reading);
		
		mDatePicker = (DatePicker) findViewById(R.id.gasdoedp);		
		mGasText = (EditText) findViewById(R.id.gas);
		
		Button confirmButton = (Button) findViewById(R.id.confirm);
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
		    String doe = extras.getString(GasDbAdapter.KEY_DOE);
		    String gas = extras.getString(GasDbAdapter.KEY_GAS);
		    mRowId = extras.getLong(GasDbAdapter.KEY_ROWID);

		    if (doe != null) {
		    	// set the datePicker to the date value, doe
		    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		    	Calendar cal = Calendar.getInstance();
		    	//SimpleDateFormat smf = new SimpleDateFormat("MM");
		    	//SimpleDateFormat syf = new SimpleDateFormat("yyyy");
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
		    if (gas != null) {
		    	// set the gas to the gas value
		        mGasText.setText(gas);
		    }
		}
		
		confirmButton.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View view) {
		    	Bundle bundle = new Bundle();

				//mDoeText = (EditText) findViewById(R.id.doe);
		    	mDatePicker = (DatePicker)findViewById(R.id.gasdoedp);
		    	int day = mDatePicker.getDayOfMonth();
		    	int month = mDatePicker.getMonth() + 1;
		    	int year = mDatePicker.getYear();
		    	
		    	String sDate = day + "." + month + "." + year;
		    	
				mGasText = (EditText) findViewById(R.id.gas);
		    			    	
		    	bundle.putString(GasDbAdapter.KEY_DOE, sDate);
		    	bundle.putString(GasDbAdapter.KEY_GAS, mGasText.getText().toString());
		    	if (mRowId != null) {
		    	    bundle.putLong(GasDbAdapter.KEY_ROWID, mRowId);
		    	}
		    	Intent mIntent = new Intent();
		    	mIntent.putExtras(bundle);
		    	setResult(RESULT_OK, mIntent);
		    	finish();
		    }

		});
		
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
