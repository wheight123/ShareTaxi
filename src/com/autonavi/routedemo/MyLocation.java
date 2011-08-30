package com.autonavi.routedemo;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.autonavi.mapapi.GeoPoint;
import com.autonavi.mapapi.MapView;
import com.autonavi.mapapi.MyLocationOverlay;
import com.autonavi.mapapi.Projection;
import com.autonavi.mapapi.MapView.LayoutParams;

public class MyLocation extends MyLocationOverlay implements LocationListener{

	Context mycontext;
	Bitmap locbitmap=null;
	Point myloc=null;

	public MyLocation(Context arg0, MapView arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		mycontext=arg0;
		locbitmap=BitmapFactory.decodeResource(mycontext.getResources(),R.drawable.people);
	}
	protected  void	drawMyLocation(android.graphics.Canvas canvas, MapView mapView, android.location.Location lastFix, GeoPoint myLocation, long when) 
	{	
		Paint paint=new Paint();
		Projection projection=mapView.getProjection();
		myloc=projection.toPixels(myLocation, null);		
		canvas.drawBitmap(locbitmap, myloc.x-16, myloc.y-16, paint);
	}


}
