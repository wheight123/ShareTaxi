package com.autonavi.routedemo;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class PickTeamActivity extends ListActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams_list);
        
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        //显示模式进一步考虑
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setFastScrollEnabled(true);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
