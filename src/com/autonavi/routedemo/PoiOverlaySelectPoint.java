package com.autonavi.routedemo;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autonavi.mapapi.GeoPoint;
import com.autonavi.mapapi.PoiItem;
import com.autonavi.mapapi.PoiOverlay;
/*
 * 继承PoiOverlay ，重写getPopupView 方法，实现在弹出窗口中添加设置起点/终点功能
 */
public class PoiOverlaySelectPoint extends PoiOverlay {

	public PoiOverlaySelectPoint(Drawable pic, List<PoiItem> items,
			String catagory, GeoPoint startPoint, GeoPoint endPoint,
			boolean start,Context context) {
		super(pic, items, catagory);
		this.start = start;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.context=context;
	}

	public PoiOverlaySelectPoint(Drawable pic, List<PoiItem> items,
			GeoPoint startPoint, GeoPoint endPoint, boolean start,Context context) {
		super(pic, items);
		this.start = start;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	    this.context=context;
	}
//重写PoiOverlay方法，在弹出窗口添加设置起点、终点功能
	protected View getPopupView(PoiItem item) {
		View view = super.getPopupView(item);	
		LinearLayout fill = new LinearLayout(view.getContext());
		fill.setOrientation(LinearLayout.VERTICAL);
		fill.setGravity(Gravity.CENTER);
		fill.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		startView = new TextView(view.getContext());
		startView.setClickable(true);
		startView.setTextColor(Color.BLUE);
		startView.setTextSize(16);
		endView = new TextView(view.getContext());
		endView.setClickable(true);
		endView.setTextColor(Color.BLUE);
		endView.setTextSize(16);
		if (start) {
			startView.setText(Html.fromHtml("<u>设为起点</u>"));
			startView.setOnClickListener(listener);
			fill.addView(startView, LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			endView.setText(Html.fromHtml("<u>设为终点</u>"));
			endView.setOnClickListener(listener);
			fill.addView(endView, LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		return fill;
	}

	protected boolean onTap(int index) {
		super.onTap(index);
		point = super.getItem(index).getPoint();
		return true;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (startView.equals(v)) {
				startPoint = point;
				PoiOverlaySelectPoint.this.closePopupWindow();
			} else if (endView.equals(v)) {
				endPoint = point;
				PoiOverlaySelectPoint.this.closePopupWindow();
			}

		}
	};

	public GeoPoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(GeoPoint startPoint) {
		this.startPoint = startPoint;
	}

	public GeoPoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(GeoPoint endPoint) {
		this.endPoint = endPoint;
	}

	private boolean start = true;
	private TextView startView, endView;
	private GeoPoint startPoint, endPoint, point;
	private Context context;
}
