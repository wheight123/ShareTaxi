package com.autonavi.routedemo;


import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class PickTeamActivity extends ListActivity implements OnClickListener {

	TeamsListAdapter mTeamsAdapter;
	ArrayList<TeamInfo> teamsinfo=new ArrayList<TeamInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.teams_list);
        
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        //显示模式进一步考虑
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setFastScrollEnabled(true);
        loadTeamsInfo();

        
        mTeamsAdapter=new TeamsListAdapter(PickTeamActivity.this,teamsinfo);
        this.setListAdapter(mTeamsAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("xxxxxxxxxxxxx", Integer.toString(position));
		//TODO
	}

	public void loadTeamsInfo() {
		for(int i=0;i<100;i++) {
			String name="drinking"+Integer.toString(i);
			teamsinfo.add(new TeamInfo(name));
		}
	}
	public void showTeamDetail() {
		//TODO
	}

}
