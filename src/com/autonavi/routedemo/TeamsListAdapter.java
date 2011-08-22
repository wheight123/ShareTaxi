package com.autonavi.routedemo;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamsListAdapter extends ArrayAdapter<TeamInfo> {

	 private ArrayList<TeamInfo> TeamsInfo;
	public TeamsListAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<TeamInfo> teams) {
		super(context, resource, textViewResourceId, teams);
		// TODO Auto-generated constructor stub
		TeamsInfo=teams;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
        LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.team_item, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        TeamInfo team=TeamsInfo.get(position);
        TextView from=(TextView)newView.findViewById(R.id.location_from);
        TextView to=(TextView)newView.findViewById(R.id.location_to);
        TextView time=(TextView)newView.findViewById(R.id.departure_time);
        TextView count=(TextView)newView.findViewById(R.id.menber_count);
        TextView phone=(TextView)newView.findViewById(R.id.leader_phone);
        TextView explanation=(TextView)newView.findViewById(R.id.explanation);

        from.setText(team.getFrom());
        to.setText(team.getTo());
        time.setText(team.getTime());
        count.setText(team.getCount());
        phone.setText(team.getPhone());
        explanation.setText(team.getExplanation());
        
        
        return newView;
	}
	

}
/**
 * 
 * @author drinking
 * 变量值依次含义为
 * 起点，终点，出发时间，成员数
 * 组长电话，详细说明
 *
 */
class TeamInfo{
	String from;
	String to;
	String departure_time;
	String member_count;
	String leader_phone;
	String explanation;
	TeamInfo(){
		
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
	public String getTime() {
		return departure_time;
	}
	public String getCount() {
		return member_count;
	}
	public String getPhone() {
		return leader_phone;
	}
	public String getExplanation() {
		return explanation;
	}
}
