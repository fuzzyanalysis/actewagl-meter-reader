package com.donkeyenough.actewagl_meter_reader;

import android.app.Application;

public class GlobalVariables extends Application 
{
      private static double _gasRate = 0.62359;
      private static double _elec04Rate = 0.2442;
      private static double _elec05Rate = 0.1870;
      private static double _elec06Rate = 0.1375;

      public static double getGasRate()
      {
        return _gasRate;
      }
      
      public static double getElec04Rate()
      {
        return _elec04Rate;
      }

      
      public static double getElec05Rate()
      {
        return _elec05Rate;
      }

      
      public static double getElec06Rate()
      {
        return _elec06Rate;
      }


}