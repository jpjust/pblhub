package br.pro.just.pblhub;

import java.util.Date;

import br.pro.just.pblhub.PBLDBContract.DBSessions;
import br.pro.just.pblhub.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Array adapter for classes list
public class SessionCursorAdapter extends CursorAdapter {

	private final Context context;
	private final Cursor c;

	public SessionCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		this.c = c;
	}

	// Get view
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listview_session_item, parent, false);

		TextView textDate = (TextView) rowView.findViewById(R.id.listview_session_item_date);

		c.moveToPosition(position);
		
		// Transform date from SQL format to locale format
		int year, month, day;
		java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(context);
		String dateSQL = c.getString(c.getColumnIndex(DBSessions.COLUMN_NAME_DATE));
		String[] dateArray = dateSQL.split("-");

		year = Integer.parseInt(dateArray[0]) - 1900;
		month = Integer.parseInt(dateArray[1]) - 1;
		day = Integer.parseInt(dateArray[2]);
		Date d = new Date(year, month, day);
		
		textDate.setText(formatter.format(d));

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
