package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBGroups;
import br.pro.just.pblhub.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SessionPerformanceFragment extends Fragment {

	// Attributes for fragments communication
	final static String ARG_SESSION_ID = "session_id";

	// Attributes for adapter
	private PerformanceCursorAdapter adapter;

	// On create view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_performance, container, false);

		// Return the view
		return v;
	}

	// Update session performance
	private void updateSessionPerformance(int sessionId) {
		// Get the objects
		ListView listView = (ListView) getView().findViewById(R.id.listViewStudents);
		TextView textId = (TextView) getView().findViewById(R.id.session_item_id);
		TextView textGroupId = (TextView) getView().findViewById(R.id.session_group_id);

		// Get data
		PBLDBHelper mDbHelper = new PBLDBHelper(getView().getContext());
		Cursor c = mDbHelper.getPerformanceBySessionId(sessionId);
		Cursor cGroup = mDbHelper.getGroupBySessionId(sessionId);

		cGroup.moveToFirst();

		// Set data in text views
		textId.setText(String.valueOf(sessionId));
		textGroupId.setText(cGroup.getString(cGroup.getColumnIndex(DBGroups._ID)));

		// Fill the ListView
		adapter = new PerformanceCursorAdapter(getView().getContext(), c, true);
		listView.setAdapter(adapter);
	}

	// On start view
	@Override
	public void onStart() {
		super.onStart();

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already been
		// applied to the fragment at this point so we can safely call the method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			updateSessionPerformance(args.getInt(ARG_SESSION_ID));
		}
	}

	// Requery the cursor
	public void requeryCursor() {
		try {
			this.adapter.getCursor().requery();
		} catch (NullPointerException e) {
			// Just don't try to requery the cursor.
		}
	}
}
