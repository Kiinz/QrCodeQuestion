package at.klu.qrcodequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import at.klu.qrcodequest.QuestCustomAdapter.UserHolder;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private Intent intent;
	private List<String> listParents;
	private HashMap<String, List<String>> listChildren;
	
	public ExpandableListAdapter(Context context, List<String> listParents , HashMap<String, List<String>> listChildren) {
		this.context = context;
		this.listChildren = listChildren;
		this.listParents = listParents;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.listParents.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		int childrenCount = 1;
		return childrenCount;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listParents.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String text = (String) getGroup(groupPosition);
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group, parent, false);	
		}
		
	
		TextView textView = (TextView) convertView.findViewById(R.id.textView1);
		textView.setText(text);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
UserHolder2 holder;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_child, parent, false);
			holder = new UserHolder2();
			holder.anmelden = (Button)convertView.findViewById(R.id.button1);
			holder.bestenliste = (Button)convertView.findViewById(R.id.button2);
			convertView.setTag(holder);
		}else{
			holder = (UserHolder2) convertView.getTag();
		}
		String tquest = listParents.get(groupPosition);
		
		holder.anmelden.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				intent = new Intent(context,BestlistActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //dadurch kann eine neue Activity außerhalb einer Activity gestartet werden
				context.startActivity(intent);
			}
		});
	
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class UserHolder2{
		Button bestenliste;
		Button anmelden;
	}

}
