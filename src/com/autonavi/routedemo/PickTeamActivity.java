package com.autonavi.routedemo;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		showTeamDetail(position);
	}

	public void loadTeamsInfo() {
		for(int i=0;i<100;i++) {
			String name="drinking"+Integer.toString(i);
			teamsinfo.add(new TeamInfo(name,"aaaaaa","bbbbbb","cccccc","ddddd","eeeeee"));
		}
	}
	public void showTeamDetail(int position) {
		TeamInfo pickedTeam=teamsinfo.get(position);
		LayoutInflater li=LayoutInflater.from(PickTeamActivity.this);
		View detailView=li.inflate(R.layout.team_details, null);
		TextView stv=(TextView)detailView.findViewById(R.id.startpoint_textview);
		stv.setText(pickedTeam.getFrom());
		TextView dtv=(TextView)detailView.findViewById(R.id.destination_textview);
		dtv.setText(pickedTeam.getTo());
		TextView dttv=(TextView)detailView.findViewById(R.id.departure_time_textview);
		dttv.setText(pickedTeam.getTime());
		TextView ptv=(TextView)detailView.findViewById(R.id.phone_number_textview);
		ptv.setText(pickedTeam.getPhone());
		TextView detv=(TextView)detailView.findViewById(R.id.details);
		detv.setText(pickedTeam.getExplanation());
		TextView pctv=(TextView)detailView.findViewById(R.id.people_count_textview);
		pctv.setText(pickedTeam.getCount()+"/4");
		
		new AlertDialog.Builder(PickTeamActivity.this).setView(detailView)
		.setPositiveButton("组队", this).setNegativeButton("取消", this).create().show();
		
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		//which -1 positive ,-2 neagtive
		if(-1==which) {
			// TODO 提交组队信息 自己的ID和队伍ID 绑定
		}
	}

}
