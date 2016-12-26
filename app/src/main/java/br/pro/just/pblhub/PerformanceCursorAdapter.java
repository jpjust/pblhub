package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBPerformance;
import br.pro.just.pblhub.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

// Array adapter for classes list
public class PerformanceCursorAdapter extends CursorAdapter {

	private final Context context;
	private final Cursor c;

	public PerformanceCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		this.c = c;
	}

	// Get view
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listview_performance_student_item, parent, false);

		// Get views
		TextView textStudent = (TextView) rowView.findViewById(R.id.listview_performance_student_name);
		Button button = (Button) rowView.findViewById(R.id.listview_performance_student_delete);

		RatingBar ratingAccomplishment	= (RatingBar) rowView.findViewById(R.id.rating_accomplishment);
		RatingBar ratingParticipation	= (RatingBar) rowView.findViewById(R.id.rating_participation);
		RatingBar ratingBehavior		= (RatingBar) rowView.findViewById(R.id.rating_behavior);
		
		TextView textAccomplishment	= (TextView) rowView.findViewById(R.id.rating_accomplishment_text);
		TextView textParticipation	= (TextView) rowView.findViewById(R.id.rating_participation_text);
		TextView textBehavior		= (TextView) rowView.findViewById(R.id.rating_behavior_text);

		c.moveToPosition(position);

		// Set tags
		String tag = String.valueOf(c.getInt(c.getColumnIndex(DBPerformance._ID)));
		button.setTag(tag);
		ratingAccomplishment.setTag(tag);
		ratingParticipation.setTag(tag);
		ratingBehavior.setTag(tag);

		// Set data
		textStudent.setText(c.getString(c.getColumnIndex("student_name")));

		ratingAccomplishment.setRating(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_ACCOMPLISHMENT)) / 2);
		ratingParticipation.setRating(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_PARTICIPATION)) / 2);
		ratingBehavior.setRating(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_BEHAVIOR)) / 2);

		textAccomplishment.setText(String.valueOf(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_ACCOMPLISHMENT))));
		textParticipation.setText(String.valueOf(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_PARTICIPATION))));
		textBehavior.setText(String.valueOf(c.getFloat(c.getColumnIndex(DBPerformance.COLUMN_NAME_BEHAVIOR))));
		
		// Set listeners
		ratingAccomplishment.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				PBLDBHelper mDbHelper = new PBLDBHelper(context);
				int id = Integer.parseInt((String) ratingBar.getTag());
				mDbHelper.updateAccomplishmentPerformance(id, rating * 2);
				c.requery();
			}
		});

		ratingParticipation.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				PBLDBHelper mDbHelper = new PBLDBHelper(context);
				int id = Integer.parseInt((String) ratingBar.getTag());
				mDbHelper.updateParticipationPerformance(id, rating * 2);
				c.requery();
			}
		});

		ratingBehavior.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				PBLDBHelper mDbHelper = new PBLDBHelper(context);
				int id = Integer.parseInt((String) ratingBar.getTag());
				mDbHelper.updateBehaviorPerformance(id, rating * 2);
				c.requery();
			}
		});

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
