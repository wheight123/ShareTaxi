package com.autonavi.routedemo;

import android.app.Application;

public class MyApplication extends Application {
	//not use current
	public static final String START_LAT="start_lat";
	public static final String START_LON="start_lon";
	public static final String END_LAT="end_lat";
	public static final String END_LON="end_lon";
	
	//start and end point
	public static double start_lat=0;
	public static double start_lon=0;
	public static double end_lat=0;
	public static double end_lon=0;
	
	//Handler message
	public static final int REFRESH_LOADED_MESSAGE=0;
	
	//UserID and passwd
	public static String UserId="drinking";
	public static String Login_Passwd="123";

}
