package com.autonavi.routedemo;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamsListAdapter extends ArrayAdapter<TeamInfo> {

	 private ArrayList<TeamInfo> TeamsInfo;
	 LayoutInflater mInflater;
		String start_point;
		String destination;
		String departure_time;
	 public TeamsListAdapter(Context context, ArrayList<TeamInfo> objects) {
		 super(context, 0, objects);
		TeamsInfo=objects;
		mInflater=LayoutInflater.from(context);
		
        start_point=context.getString(R.string.starting_point);
        destination=context.getString(R.string.destination);
        departure_time=context.getString(R.string.departure_time);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder=null;
      
        if (convertView == null) {
            convertView=mInflater.inflate(R.layout.team_item, null);
            
            viewholder=new ViewHolder();
            viewholder.from=(TextView)convertView.findViewById(R.id.location_from);
            viewholder.to=(TextView)convertView.findViewById(R.id.location_to);
            viewholder.time=(TextView)convertView.findViewById(R.id.departure_time);         
            
            convertView.setTag(viewholder);
            
        } else {
            viewholder = (ViewHolder)convertView.getTag();
        }
        
        TeamInfo team=TeamsInfo.get(position);
        
        viewholder.from.setText(start_point+team.getFrom());       
        viewholder.to.setText(destination+team.getTo());
        viewholder.time.setText(departure_time+team.getTime());

              
        return convertView;
	}
	static class ViewHolder {
	    TextView from;
	    TextView to;
	    TextView time;

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
	String details;
	TeamInfo(String name,String destination,String time,String count,String phone,String detail){
		from=name;
		to=destination;
		departure_time=time;
		member_count=count;
		leader_phone=phone;
		details=detail;
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
		return details;
	}
	public void print() {
		Log.d("[TeamInfo-Log]", from);
		Log.d("[TeamInfo-Log]", to);
		Log.d("[TeamInfo-Log]", departure_time);
		Log.d("[TeamInfo-Log]", member_count);
		Log.d("[TeamInfo-Log]", leader_phone);
		Log.d("[TeamInfo-Log]", details);		
	}
}
