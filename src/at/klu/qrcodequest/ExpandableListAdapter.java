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

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private Intent intent;
	private List<String> listParents;
	private HashMap<String, List<String>> listChildren;
	private List<Quest> quests;
	
	public ExpandableListAdapter(Context context, List<String> listParents, ArrayList<Quest> quests) {
		this.context = context;
		this.listChildren = null;
		this.listParents = listParents;
		this.quests = quests;
	}
	
	@Override
	public int getGroupCount() {
		return this.listParents.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
        return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.listParents.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
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
			holder.anmelden = (Button)convertView.findViewById(R.id.sign);
			holder.bestenliste = (Button)convertView.findViewById(R.id.best);
			convertView.setTag(holder);
		}else{
			holder = (UserHolder2) convertView.getTag();
		}
		final int id = (int)getGroupId(groupPosition);
		String tquest = listParents.get(id);
		
		holder.anmelden.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(quests.get((int)getGroupId(id)).getDtRegistration() == 2){
					//QR-Code
					intent = new Intent(context,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //dadurch kann eine neue Activity außerhalb einer Activity gestartet werden
					intent.putExtra("questPk", quests.get((int)getGroupId(id)).getId());
					intent.putExtra("dtRegistration", quests.get((int)getGroupId(id)).getDtRegistration());
					context.startActivity(intent);
				}else if(quests.get((int)getGroupId(id)).getDtRegistration() == 3){
					intent = new Intent(context,NFCActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //dadurch kann eine neue Activity außerhalb einer Activity gestartet werden
					intent.putExtra("questPk", quests.get((int)getGroupId(id)).getId());
					intent.putExtra("dtRegistration", quests.get((int)getGroupId(id)).getDtRegistration());
					context.startActivity(intent);
				}else if(quests.get((int)getGroupId(id)).getDtRegistration() == 4){
					//GPS
				}
				
			}
		});
		
		holder.bestenliste.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				
				intent = new Intent(context, BestlistActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				intent.putExtra("questPk", quests.get((int)getGroupId(id)).getId());
				context.startActivity(intent);
				
			}
		});
	
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	static class UserHolder2{
		Button bestenliste;
		Button anmelden;
	}

}
