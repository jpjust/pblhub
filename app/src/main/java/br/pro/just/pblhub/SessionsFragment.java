package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBProblems;
import br.pro.just.pblhub.PBLDBContract.DBSessions;
import br.pro.just.pblhub.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

public class SessionsFragment extends Fragment {

	// Attributes for adapter
	private ProblemCursorAdapter adapterProblems;
	private SessionCursorAdapter adapterSessions;

	// Callback for fragments communication
	OnHeadlineSelectedListener mCallback;

	// On create view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_sessions, container, false);

		// Get the ListView and Spinner objects
		ListView listView = (ListView) v.findViewById(R.id.listViewSessions);
		Spinner spinner = (Spinner) v.findViewById(R.id.spinnerProblems);

		// Get data
		PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
		Cursor cProblems = mDbHelper.getAllProblems();

		// Fill the ListView and the Spinner
		adapterProblems = new ProblemCursorAdapter(container.getContext(), cProblems, true);
		spinner.setAdapter(adapterProblems);

		// Set listener for List View
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Cursor item = (Cursor) parent.getAdapter().getItem(position);
				mCallback.onSessionSelected(item);
			}
		}); 

		// Set listener for Spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view == null)
					return;

				// Filter sessions
				PBLDBHelper mDbHelper = new PBLDBHelper(view.getContext());
				Cursor item = (Cursor) parent.getAdapter().getItem(position);
				String whereClause = DBSessions.COLUMN_NAME_PROBLEM_ID + "= ?";
				String[] whereArgs = { item.getString(item.getColumnIndex(DBProblems._ID)) };
				Cursor cSessions = mDbHelper.getSessionsWhere(whereClause, whereArgs);

				// Get List View and set its adapter
				ListView listView = (ListView) parent.getRootView().findViewById(R.id.listViewSessions);
				setListViewAdapter(view.getContext(), listView, cSessions);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Another interface callback
			}
		}); 

		// Filter sessions
		Cursor item = (Cursor) spinner.getAdapter().getItem(0);
		if (item.getCount() > 0) {
			String whereClause = DBSessions.COLUMN_NAME_PROBLEM_ID + "= ?";
			String[] whereArgs = { item.getString(item.getColumnIndex(DBProblems._ID)) };
			Cursor cSessions = mDbHelper.getSessionsWhere(whereClause, whereArgs);

			// Get List View and set its adapter
			setListViewAdapter(container.getContext(), listView, cSessions);
		}

		// Return the view
		return v;
	}

	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onSessionSelected(Cursor item);
	}

	// On attach
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	// Set List View adapter
	private void setListViewAdapter(Context context, ListView l, Cursor c) {
		adapterSessions = new SessionCursorAdapter(context, c, true);
		l.setAdapter(adapterSessions);
	}
}
