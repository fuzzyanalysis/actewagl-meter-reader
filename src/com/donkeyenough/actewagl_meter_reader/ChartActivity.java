/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.donkeyenough.actewagl_meter_reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.achartengine.chartdemo.demo.GeneratedChartDemo;
import org.achartengine.chartdemo.demo.chart.IChart;
import org.achartengine.chartdemo.demo.chart.ElectricityUsageChart;
import org.achartengine.chartdemo.demo.chart.GasUsageChart;
//import org.achartengine.chartdemo.demo.chart.XYChartBuilder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChartActivity extends ListActivity {
  private IChart[] mCharts = new IChart[] { new GasUsageChart(), new ElectricityUsageChart() };

  private String[] mMenuText;

  private String[] mMenuSummary;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int length = mCharts.length;
    mMenuText = new String[length];
    mMenuSummary = new String[length];
    for (int i = 0; i < length; i++) {
      mMenuText[i] = mCharts[i].getName();
      mMenuSummary[i] = mCharts[i].getDesc();
    }
    setListAdapter(new SimpleAdapter(this, getListValues(), android.R.layout.simple_list_item_2,
        new String[] { IChart.NAME, IChart.DESC }, new int[] { android.R.id.text1,
            android.R.id.text2 }));
    
    //Display current costs etc
    
  }

  private List<Map<String, String>> getListValues() {
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    int length = mMenuText.length;
    for (int i = 0; i < length; i++) {
      Map<String, String> v = new HashMap<String, String>();
      v.put(IChart.NAME, mMenuText[i]);
      v.put(IChart.DESC, mMenuSummary[i]);
      values.add(v);
    }
    return values;
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent intent = null;  	
  	if (position <= mCharts.length + 1) {
      intent = mCharts[position].execute(this);
    } 
    startActivity(intent);
  }
}