package com.autonavi.routedemo;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.autonavi.mapapi.GeoPoint;
import com.autonavi.mapapi.MapActivity;
import com.autonavi.mapapi.MapView;
import com.autonavi.mapapi.PoiPagedResult;
import com.autonavi.mapapi.PoiSearch;
import com.autonavi.mapapi.PoiTypeDef;
import com.autonavi.mapapi.Route;
import com.autonavi.mapapi.RouteMessageHandler;
import com.autonavi.mapapi.RouteOverlay;
import com.autonavi.mapapi.MapView.LayoutParams;
import com.autonavi.mapapi.Route.FromAndTo;

/*
 * 显示MapView 实现PoiSearch及Route功能
 */
public class Main extends MapActivity implements RouteMessageHandler,
		OnGestureListener {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		setContentView(R.layout.main);
		mv = (MapView) this.findViewById(R.id.mapView);
		mv.setBuiltInZoomControls(true);
		setInitView();
	}

	private void setInitView() {
		/*
		calculateRoute = (Button) this.findViewById(R.id.CalculateRoute);
		setSearch = (Button) this.findViewById(R.id.setStartAndEnd);
		driving = (ImageButton) this.findViewById(R.id.driving);
		bus = (ImageButton) this.findViewById(R.id.bus);
		walk = (ImageButton) this.findViewById(R.id.walk);
		
		calculateRoute.setOnClickListener(clickListener);
		setSearch.setOnClickListener(clickListener);
		driving.setOnClickListener(clickListener);
		bus.setOnClickListener(clickListener);
		bus.setBackgroundResource(R.drawable.transit_down);
		walk.setOnClickListener(clickListener);
		*/
		mv.setLongClickable(true);
		mGestureDetector.setIsLongpressEnabled(true);
	}

	private void showPoiOverlay(String poiType, String cityStr, boolean start) {

		try {
			if (start) {
				// PoiSearch.Query 设置POI 查询条件
				// startStr为查询字符串，poiType为类型，cityStr是城市代码
				poiSearchStart = new PoiSearch(this, new PoiSearch.Query(
						startStr, poiType, cityStr));
				// 返回 PoiPagedResult
				resultStart = poiSearchStart.searchPOI();
				// PoiOverlaySelectPoint继承自PoiOverlay，resultStart.getPage(1)，取结果第一页
				selectPointStart = new PoiOverlaySelectPoint(null, resultStart
						.getPage(1), startPoint, endPoint, true, this);
				selectPointStart.addToMap(mv);
			} else {
				poiSearchEnd = new PoiSearch(this, new PoiSearch.Query(endStr,
						poiType, cityStr));
				// 返回 PoiPagedResult
				resultEnd = poiSearchEnd.searchPOI();
				// PoiOverlaySelectPoint继承自PoiOverlay，resultStart.getPage(1)，取结果第一页
				selectPointEnd = new PoiOverlaySelectPoint(null, resultEnd
						.getPage(1), startPoint, endPoint, false, this);
				selectPointEnd.addToMap(mv);
			}
		} catch (IOException e) {
			this.showToast("网络连接异常");
			e.printStackTrace();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.share_taxi:{
//				calculateRoute.setClickable(true);
				Intent intent = new Intent(Main.this, SearchInput.class);
				Bundle dle = new Bundle();
				dle.putString("start", startStr);
				dle.putString("end", endStr);
				dle.putString("mode", String.valueOf(mode));
				intent.putExtras(dle);
				if (routeOverlay != null) {
					routeOverlay.removeFromMap(mv);
				}
				Main.this.startActivityForResult(intent, REQUESTCODE);
			break;
		}
		case R.id.my_team:{
			//TODO assgined to wanghui
			// 向服务器获取 参与的队伍信息
			
			break;
			}
		case R.id.team_search:
			//进入队伍搜索Dialog，并发送至服务器以获取需要的队伍信息
			
			break;
		default:
			break;
		}
		return false;
	}

//	private void setModeBackResource(int mode) {
//
//		if (mode == Route.DrivingDefault) {
//			driving.setBackgroundResource(R.drawable.driving_down);
//			walk.setBackgroundResource(R.drawable.walk);
//			bus.setBackgroundResource(R.drawable.transit);
//		} else if (mode == Route.BusDefault) {
//			bus.setBackgroundResource(R.drawable.transit_down);
//			walk.setBackgroundResource(R.drawable.walk);
//			driving.setBackgroundResource(R.drawable.driving);
//		} else if (mode == Route.WalkDefault) {
//			walk.setBackgroundResource(R.drawable.walk_down);
//			bus.setBackgroundResource(R.drawable.transit);
//			driving.setBackgroundResource(R.drawable.driving);
//		}
//	}

	OnClickListener clickListener = new OnClickListener() {
		public void reCalculate(int mode, GeoPoint start, GeoPoint end) {
	//		setModeBackResource(mode);
			if ((startPoint != null) && (endPoint != null)) {
				if (oldMode != mode) {
					try {
						displayRoute(startPoint, endPoint, mode);
					} catch (IllegalArgumentException e) {

					} catch (Exception e1) {

					}
				}
			}

		}

		@Override
		public void onClick(View v) {
			/*
			// 计算路径
			if (calculateRoute.equals(v)) {
				if (selectPointStart != null && selectPointEnd != null) {
					startPoint = selectPointStart.getStartPoint();
					endPoint = selectPointEnd.getEndPoint();
				} else if (selectPointStart == null && selectPointEnd != null) {
					endPoint = selectPointEnd.getEndPoint();
				} else if (selectPointStart != null && selectPointEnd == null) {
					startPoint = selectPointStart.getStartPoint();
				}
				try {
					mv.getOverlays().clear();
					displayRoute(startPoint, endPoint, mode);
				} catch (IllegalArgumentException e) {

				} catch (Exception e1) {

				}
				setModeBackResource(mode);
			}
			// POISearch
			if (setSearch.equals(v)) {
				calculateRoute.setClickable(true);
				Intent intent = new Intent(Main.this, SearchInput.class);
				Bundle dle = new Bundle();
				dle.putString("start", startStr);
				dle.putString("end", endStr);
				dle.putString("mode", String.valueOf(mode));
				intent.putExtras(dle);
				if (routeOverlay != null) {
					routeOverlay.removeFromMap(mv);
				}
				Main.this.startActivityForResult(intent, REQUESTCODE);
			}
			// 自驾模式
			if (driving.equals(v)) {
				if (modeClick) {
					mode = Route.DrivingDefault;
					reCalculate(mode, startPoint, endPoint);

				}
			}
			// 公交模式
			if (bus.equals(v)) {
				if (modeClick) {
					mode = Route.BusDefault;
					reCalculate(mode, startPoint, endPoint);
				}
			}
			// 步行模式
			if (walk.equals(v)) {
				if (modeClick) {
					mode = Route.WalkDefault;
					reCalculate(mode, startPoint, endPoint);
				}
			}
			*/
			// MapView 点击选择起点
			if (popupDipStart != null && popupDipStart.equals(v)) {
				startPoint = tempPoint;
				Intent intent = new Intent(Main.this, SearchInput.class);
				Bundle dle = new Bundle();
				dle.putString("start", POINT_IN_MAP);
				dle.putString("end", endStr);
				dle.putString("mode", String.valueOf(mode));
				intent.putExtras(dle);
				mv.removeView(popupDipStart);
				Main.this.startActivityForResult(intent, REQUESTCODE);
				// MapView 点击选择终点
			} else if (popupDipEnd != null && popupDipEnd.equals(v)) {
				endPoint = tempPoint;

				Intent intent = new Intent(Main.this, SearchInput.class);
				Bundle dle = new Bundle();
				dle.putString("start", startStr);
				dle.putString("end", POINT_IN_MAP);
				dle.putString("mode", String.valueOf(mode));
				intent.putExtras(dle);
				mv.removeView(popupDipEnd);
				Main.this.startActivityForResult(intent, REQUESTCODE);
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				startStr = bundle.getString("start");
				endStr = bundle.getString("end");
				dip = bundle.getString("dip"); // 判断点击按钮
				String modeTemp = bundle.getString("mode");
				if (modeTemp != null && (!modeTemp.equals(""))) {
					mode = Integer.parseInt(modeTemp);
//					setModeBackResource(mode);
				}
				if (dip.equals("startDip")) {// 点击起点点选跳转到此
					showToast("在地图上点击您的起点");
					mv.setOnTouchListener(touchListener);
				} else if (dip.equals("endDip")) {// 点击终点点选跳转到此
					showToast("在地图上点击您的终点");
					mv.setOnTouchListener(touchListener);
				} else if (dip.equals("okDip")) {// 点击确定按钮跳转到此
					if (startStr.equals(POINT_IN_MAP)
							&& endStr.equals(POINT_IN_MAP)) {// 起终点都点选
//						calculateRoute.setClickable(false);
						try {
							//TODO 显示对话框包括创建队伍和搜索队伍
							TeamListDialog(startPoint,endPoint);
							Log.d(Integer.toString(startPoint.getLatitudeE6()),
									Integer.toString(startPoint.getLongitudeE6()));
							Log.d(Integer.toString(endPoint.getLatitudeE6()),
									Integer.toString(endPoint.getLongitudeE6()));
							double distance=CalculateMethod.GetDistance(startPoint.getLatitudeE6()*0.86, startPoint.getLongitudeE6(), endPoint.getLatitudeE6()*0.86, endPoint.getLongitudeE6());
							Toast.makeText(Main.this, "两地距离为"+Double.toString(distance)+"公里", Toast.LENGTH_LONG).show();
//							displayRoute(startPoint, endPoint, mode);
						} catch (IllegalArgumentException e) {

						} catch (Exception e1) {

						}
					} else if ((startStr.equals(POINT_IN_MAP))// 起点点选
							&& (!endStr.equals(POINT_IN_MAP))) {
						mv.getOverlays().clear();
						removeMyView(mv);
						showPoiOverlay(PoiTypeDef.PlaceAndAddress, "110000", false);
					} else if ((!startStr.equals(POINT_IN_MAP))// 终点点选
							&& (endStr.equals(POINT_IN_MAP))) {
						mv.getOverlays().clear();
						removeMyView(mv);
						showPoiOverlay(PoiTypeDef.PlaceAndAddress, "110000", true);

					} else if ((!startStr.equals(POINT_IN_MAP))// 起终点都非点选
							&& (!endStr.equals(POINT_IN_MAP))) {
						mv.getOverlays().clear();
						showPoiOverlay(PoiTypeDef.PlaceAndAddress, "110000", true);
						showPoiOverlay(PoiTypeDef.PlaceAndAddress, "110000", false);
					}
				}
			}
		}
	}

	private void displayRoute(GeoPoint startPoint, GeoPoint endPoint, int mode)
			throws IllegalArgumentException, Exception {
		if (startPoint != null && (!startPoint.equals("")) && endPoint != null
				&& (!endPoint.equals(""))) {
			// FromAndTo 传入参数分别为 GeoPoint from, GeoPoint to, int trans
			// 。trans设置是否偏转
			fromAndTo = new FromAndTo(startPoint, endPoint);
			try {
				if (routeOverlay != null) {
					routeOverlay.removeFromMap(mv);
					mv.getOverlays().clear();
				}
				// 计算路径，传入参数分别为 MapActivity act, FromAndTo ft, int mode，mode
				// 为路径模式，返回路径规划的List
				route = Route.calculateRoute(Main.this, fromAndTo, mode);

				// 构造RouteOverlay 参数为 MapActivity cnt, Route rt。这里只取了查到路径的第一条。
				if (route.size() > 0) {
					routeOverlay = new RouteOverlay(Main.this, route.get(0));
					routeOverlay.registerRouteMessage(Main.this);
					routeOverlay.addToMap(mv);
					oldRouteOverlay = routeOverlay;
					oldMode = mode;
					mv.setOnTouchListener(null);
				}

			} catch (IOException e) {
				showToast("网络异常");
				Log.v("SPAN", "not found load");
				e.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				showToast("参数不正确");
				throw new IllegalArgumentException();
			} catch (Exception e2) {
				e2.printStackTrace();
				showToast("路径查询异常");
				throw new Exception();
			} finally {

				mv.setOnTouchListener(null);
				calculateRoute.setClickable(false);
			}

		}
	}

	OnTouchListener touchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		
			return mGestureDetector.onTouchEvent(event);
		}

	};

	// RouteOverlay拖动过程中触发
	@Override
	public void onDrag(MapView mapView, RouteOverlay overlay, int index,
			GeoPoint newPos) {
		Log.v("SPAN", "on drag");
	
	}

	// RouteOverlay拖动开始时触发
	@Override
	public void onDragBegin(MapView mapView, RouteOverlay overlay, int index,
			GeoPoint pos) {
		Log.v("SPAN", "on drag Begin");
		calculateRoute.setClickable(false);
		

	}

	// RouteOverlay拖动完成触发
	@Override
	public void onDragEnd(MapView mapView, RouteOverlay overlay, int index,
			GeoPoint pos) {
		Log.v("SPAN", "on drag end");
		routeOverlay.removeFromMap(mv);
		try {
			startPoint = overlay.getStartPos();
			endPoint = overlay.getEndPos();
			overlay.renewOverlay(mapView);
			// displayRoute(overlay.getStartPos(), overlay.getEndPos(), mode);
		} catch (IllegalArgumentException e) {
			overlay.restoreOverlay(mv);
			overlayToBack(routeOverlay, mv);
		} catch (Exception e1) {
			overlay.restoreOverlay(mv);
			overlayToBack(routeOverlay, mv);
		}
	}

	private void overlayToBack(RouteOverlay overlay, MapView mapView) {
		startPoint = overlay.getStartPos();
		endPoint = overlay.getEndPos();
	}

	@Override
	public boolean onRouteEvent(MapView mapView, RouteOverlay overlay,
			int index, int action) {

		return false;
	}

	public void showToast(String showString) {
		Toast.makeText(getApplicationContext(), showString, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		// mv.removeAllViews(); //会隐掉API的上一步下一步按钮
		removeMyView(mv);
		float x = e.getX();
		float y = e.getY();
		tempPoint = mv.getProjection().fromPixels((int) x, (int) y);
		Resources res = Main.this.getResources();
		if (dip.equals("startDip")) {
			popupDipStart = new ImageButton(Main.this);
			if (draw == null) {
				draw = res.getDrawable(R.drawable.popup);
			}
			popupDipStart.setBackgroundDrawable(draw);
			mv.addView(popupDipStart, new MapView.LayoutParams(draw
					.getIntrinsicWidth(), draw.getIntrinsicHeight(), tempPoint,
					LayoutParams.BOTTOM_CENTER));
			popupDipStart.setOnClickListener(clickListener);
		} else if (dip.equals("endDip")) {
			popupDipEnd = new ImageButton(Main.this);
			if (draw == null) {
				draw = res.getDrawable(R.drawable.popup);
			}
			popupDipEnd.setBackgroundDrawable(draw);
			mv.addView(popupDipEnd, new MapView.LayoutParams(draw
					.getIntrinsicWidth(), draw.getIntrinsicHeight(), tempPoint,
					LayoutParams.BOTTOM_CENTER));
			popupDipEnd.setOnClickListener(clickListener);
		}
		mv.invalidate();
		return false;
	}

	private void removeMyView(MapView mv) {
		if (popupDipStart != null) {
			mv.removeView(popupDipStart);
		}
		if (popupDipEnd != null) {
			mv.removeView(popupDipEnd);
		}
	}

	private MapView mv;
	private PoiSearch poiSearchStart, poiSearchEnd;
	private PoiPagedResult resultStart, resultEnd;
	private PoiOverlaySelectPoint selectPointStart, selectPointEnd;
	private Button calculateRoute, setSearch;
	private GeoPoint tempPoint;
	private GeoPoint startPoint, endPoint;
	private FromAndTo fromAndTo;
	private List<Route> route;
	private RouteOverlay routeOverlay, oldRouteOverlay;
	private int mode = Route.BusDefault;
	private int oldMode = Route.BusDefault;
	private ImageButton driving, bus, walk;
	private String dip;
	private ImageButton popupDipStart, popupDipEnd;
	private boolean modeClick = true;
	private String POINT_IN_MAP = "地图上的点";
	private String startStr, endStr;
	private GestureDetector mGestureDetector = new GestureDetector(this);
	private Drawable draw;
	private final int REQUESTCODE = 1;
	
	//drinking's code
	private static final int SEARCH_TEAM=0;
	private static final int CREATE_TEAM=1;

	public void log(String msg){
		Log.d("[TaxiSystemLog]","++++++"+msg+"++++++");
	}
	public void TeamListDialog(GeoPoint start,GeoPoint end){
		new AlertDialog.Builder(Main.this).setTitle("操作").setItems(
		     new String[] { "查找队伍", "创建队伍" },new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	
		            	switch(which){
		            	case SEARCH_TEAM:
		            		//TODO 发送起始坐标，获取返回的队伍信息
		            		Intent i=new Intent(Main.this,PickTeamActivity.class);
		            		startActivity(i);
		            		log("search team");
		            		break;
		            	case CREATE_TEAM:
		            		log("create team");
		            		createTeamDialog();
		            		break;
		            	}
		            
		            }
		        })
		        .create().show();
		     
	}
	Spinner datesp;
	Spinner hoursp;
	Spinner minutesp;
	EditText startEditText;
	EditText destinationEditText;
	EditText phoneEditText;
	EditText detailsEditText;
	public void createTeamDialog() {
		
		
		LayoutInflater mInflater=LayoutInflater.from(Main.this);
		View createteam=mInflater.inflate(R.layout.create_team, null);
		startEditText=(EditText)createteam.findViewById(R.id.starting_point_editText);
		destinationEditText=(EditText)createteam.findViewById(R.id.destination_editText);
		phoneEditText=(EditText)createteam.findViewById(R.id.phone_number_editText);
		detailsEditText=(EditText)createteam.findViewById(R.id.details_editText);
		datesp=(Spinner)createteam.findViewById(R.id.datespinner);
		datesp.setAdapter(createTimeList());
		hoursp=(Spinner)createteam.findViewById(R.id.hourspinner);
		hoursp.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 24;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll=new LinearLayout(Main.this);
				TextView tv=new TextView(Main.this);
				tv.setText(Integer.toString(position)+"时");
				tv.setTextColor(Color.BLACK);
				ll.addView(tv);
				return ll;
			}
			
		});
		minutesp=(Spinner)createteam.findViewById(R.id.minutespinner);
		minutesp.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 12;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout ll=new LinearLayout(Main.this);
				TextView tv=new TextView(Main.this);
				tv.setText(Integer.toString(position*5)+"分");
				tv.setTextColor(Color.BLACK);
				ll.addView(tv);
				return ll;
			}
			
		});
		new AlertDialog.Builder(Main.this).setTitle("Create Team")
		.setView(createteam).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			Log.d(Integer.toString(datesp.getSelectedItemPosition()), "xxxxxxxxxxxx");
			Log.d(Integer.toString(hoursp.getSelectedItemPosition()), "xxxxxxxxxxxx");
			Log.d(Integer.toString(minutesp.getSelectedItemPosition()), "xxxxxxxxxxxx");
			TeamInfo selectedTeam=new TeamInfo(startEditText.getText().toString(),
					destinationEditText.getText().toString(),
					dateInWeek[datesp.getSelectedItemPosition()]
					           +"-"+Integer.toString(hoursp.getSelectedItemPosition())
					           +"-"+Integer.toString(minutesp.getSelectedItemPosition()*5),
					"0",phoneEditText.getText().toString(),
					detailsEditText.getText().toString());
			selectedTeam.print();
			//TODO 发送创建队伍信息
				
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		}).create().show();
		
	}
	String []dateInWeek=new String [7];
	public BaseAdapter createTimeList() {
		long today=System.currentTimeMillis();
		Calendar c = Calendar.getInstance();
		
		for(int i=0;i<7;i++) {
			dateInWeek[i]=Integer.toString(c.get(Calendar.YEAR))+"-"
						 +Integer.toString(c.get(Calendar.MONTH))+"-"
						 +Integer.toString(c.get(Calendar.DATE));
			today+=1000*60*60*24;
			c.setTimeInMillis(today);
		}
		BaseAdapter ba=new BaseAdapter() {


			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 7;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				LinearLayout ll=new LinearLayout(Main.this);
				TextView tv=new TextView(Main.this);
				tv.setTextColor(Color.BLACK);
				tv.setText(dateInWeek[position]);
				ll.addView(tv);
				
				return ll;				
			}
			
		};
		return ba;
		
	}
}