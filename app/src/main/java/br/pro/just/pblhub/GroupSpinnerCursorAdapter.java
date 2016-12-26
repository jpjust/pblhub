package br.pro.just.pblhub;

import br.pro.just.pblhub.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Array adapter for classes list
public class GroupSpinnerCursorAdapter extends CursorAdapter {

	private final Context context;
	private final Cursor c;

	public GroupSpinnerCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		this.c = c;
	}

	// Get view
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.spinner_group_item, parent, false);

		TextView textGroup = (TextView) rowView.findViewById(R.id.spinner_group_item_group_text);
		TextView textClass = (TextView) rowView.findViewById(R.id.spinner_group_item_class_text);

		c.moveToPosition(position);
		textGroup.setText(c.getString(c.getColumnIndex(PBLDBContract.DBGroups.COLUMN_NAME_NAME)));
		textClass.setText(c.getString(c.getColumnIndex("class_name")));

		return rowView;

	}
	
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}
