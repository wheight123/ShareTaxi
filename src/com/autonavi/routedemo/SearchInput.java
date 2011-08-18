package com.autonavi.routedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * 显示起点、终点输入框及点击按钮，方便以两种方式设定Route的起点、终点
 */
public class SearchInput extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_input);
		search = (Button) this.findViewById(R.id.search);
		startPut = (EditText) findViewById(R.id.startPut);
		endPut = (EditText) findViewById(R.id.endPut);
		pointStart = (ImageButton) this.findViewById(R.id.pointStart);
		pointEnd = (ImageButton) this.findViewById(R.id.pointEnd);
		pointStart.setOnClickListener(listener);
		pointEnd.setOnClickListener(listener);
		search.setOnClickListener(listener);
	}

	public void onStart() {
		super.onStart();
		Bundle mBundle = this.getIntent().getExtras();
		if (mBundle != null) {
			String mStart = mBundle.getString("start");
			String mEnd = mBundle.getString("end");
			mode = mBundle.getString("mode");
			// 起点不空
			if (mStart != null && (!mStart.equals(""))) {
				if (mStart.equals(POINT_IN_MAP)) {
					startPut.setHint(POINT_IN_MAP);
				} else {
					startPut.setText(mStart);
					startPut.setHint("请输入起点");
				}
			}
			// 终点不空
			if (mEnd != null && (!mEnd.equals(""))) {
				if (mEnd.equals(POINT_IN_MAP)) {
					endPut.setHint(POINT_IN_MAP);
				} else {
					endPut.setText(mEnd);
					endPut.setHint("请输入终点");
				}
			}
		}
	}

	OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			// 点击确定按钮
			if (search.equals(v)) {
				if (startPut.getText().toString().equals("")&&(!startPut.getHint().toString().equals(POINT_IN_MAP))) {
					Toast.makeText(getApplicationContext(), "请输入起点",
							Toast.LENGTH_SHORT).show();
				} else if (endPut.getText().toString().equals("")&&(!endPut.getHint().toString().equals(POINT_IN_MAP))) {
					Toast.makeText(getApplicationContext(), "请输入终点",
							Toast.LENGTH_SHORT).show();
				} else {
					prepDataSkip("okDip");
				}
				// 点击点选起点
			} else if (pointStart.equals(v)) {
				this.prepDataSkip("startDip");
				// 点击点选终点
			} else if (pointEnd.equals(v)) {
				this.prepDataSkip("endDip");
			}
		}

		private void prepDataSkip(String dipString) {
			Intent intent = new Intent(SearchInput.this, Main.class);
			Bundle bul = new Bundle();
			if ((startPut.getText().toString().equals(""))&& startPut.getHint() != null
					&& startPut.getHint().equals(POINT_IN_MAP)) {
				bul.putString("start", POINT_IN_MAP);
			} else {
				bul.putString("start", startPut.getText().toString());
			}
			if ((endPut.getText().toString().equals(""))&&endPut.getHint() != null
					&& endPut.getHint().equals(POINT_IN_MAP)) {
				bul.putString("end", POINT_IN_MAP);
			} else {
				bul.putString("end", endPut.getText().toString());
			}
			bul.putString("mode", mode);
			bul.putString("dip", dipString);
			intent.putExtras(bul);
			SearchInput.this.setResult(REQUESTCODE, intent);
			SearchInput.this.finish();
		}
	};
	private EditText startPut, endPut;
	private Button search;
	private ImageButton pointStart, pointEnd;
	private String mode;
	private final int REQUESTCODE = 1;
	private final String POINT_IN_MAP = "地图上的点";
}
