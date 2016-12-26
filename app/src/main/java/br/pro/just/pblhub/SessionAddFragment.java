package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBProblems;
import br.pro.just.pblhub.R;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ValidFragment") public class SessionAddFragment extends Fragment {

	// Attributes for fragments communication
	final static String ARG_ID	 	 = "id";
	final static String ARG_DATE 	 = "date";
	final static String ARG_GOALS 	 = "goals";
	final static String ARG_PROBLEM_ID = "problem_id";
	
	// Attributes for adapter
	private ProblemCursorAdapter adapter;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sessions_add, container, false);
        
		// Get the spinner object
		Spinner spinner = (Spinner) v.findViewById(R.id.spinnerProblems);

	  	// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor c = mDbHelper.getAllProblems();
    	
    	// Fill the spinner
    	adapter = new ProblemCursorAdapter(container.getContext(), c, true);
    	spinner.setAdapter(adapter);
    	
    	// Return the view
    	return v;
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
            updateSessionName(args.getInt(ARG_ID), args.getInt(ARG_PROBLEM_ID), args.getString(ARG_DATE), args.getString(ARG_GOALS));
        }
    }

    // Insert data into textbox for database update
    private void updateSessionName(int id, int problemId, String date, String goals) {
		TextView textId = (TextView) getActivity().findViewById(R.id.session_item_id);
		EditText editDate = (EditText) getActivity().findViewById(R.id.editTextSessionDate);
		EditText editGoals = (EditText) getActivity().findViewById(R.id.editTextSessionGoals);
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerProblems);
		
		textId.setText(String.valueOf(id));
		editDate.setText(date);
		editGoals.setText(goals);
		
		// Select the correct item in the spinner
		for (int i = 0; i < spinner.getCount(); i++) {
			Cursor c = (Cursor)spinner.getItemAtPosition(i);
			
			if ((c != null) && (c.getInt(c.getColumnIndex(DBProblems._ID)) == problemId)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		// Make the delete button visible
		Button buttonDelete = (Button) getActivity().findViewById(R.id.buttonDelete);
		buttonDelete.setVisibility(View.VISIBLE);
    }
	
}
