package com.donkeyenough.actewagl_meter_reader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ElectricityReading {
	
	private String _doe;
	private String _elec04;
	private String _elec05;
	private String _elec06;
	DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	public ElectricityReading(){
		this._doe = sdf.format(new Date());
		this._elec04 = "0";
		this._elec05 = "0";
		this._elec06 = "0";
	}
	 
	public String getDoe() {
		return _doe;
	}
	
	public void setDoe(String val) {
		this._doe = val;
	}
	
	public String getElec04() {
		return _elec04;
	}
	
	public void setElec04(String val) {
		this._elec05 = val;
	}
	
	public String getElec05() {
		return _elec05;
	}

	public void setElec05(String val) {
		this._elec05 = val;
	}
	
	public String getElec06() {
		return _elec06;
	}
	
	public void setElec06(String val) {
		this._elec06 = val;
	}

}
