package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBGroups;
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

@SuppressLint("ValidFragment") public class ProblemAddFragment extends Fragment {

	// Attributes for fragments communication
	final static String ARG_ID	 	 = "id";
	final static String ARG_ITEM 	 = "item";
	final static String ARG_DESC 	 = "desc";
	final static String ARG_GROUP_ID = "group_id";
	
	// Attributes for adapter
	private GroupSpinnerCursorAdapter adapter;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_problems_add, container, false);
        
		// Get the spinner object
		Spinner spinner = (Spinner) v.findViewById(R.id.spinnerGroups);

	  	// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor c = mDbHelper.getAllGroups();
    	
    	// Fill the spinner
    	adapter = new GroupSpinnerCursorAdapter(container.getContext(), c, true);
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
            updateProblemName(args.getInt(ARG_ID), args.getInt(ARG_GROUP_ID), args.getString(ARG_ITEM), args.getString(ARG_DESC));
        }
    }

    // Insert data into textbox for database update
    private void updateProblemName(int id, int idGroup, String name, String description) {
		TextView textId = (TextView) getActivity().findViewById(R.id.problem_item_id);
		EditText editName = (EditText) getActivity().findViewById(R.id.editTextProblemName);
		EditText editDesc = (EditText) getActivity().findViewById(R.id.editTextProblemDesc);
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerGroups);
		
		textId.setText(String.valueOf(id));
		editName.setText(name);
		editDesc.setText(description);
		
		// Select the correct item in the spinner
		for (int i = 0; i < spinner.getCount(); i++) {
			Cursor c = (Cursor)spinner.getItemAtPosition(i);
			
			if ((c != null) && (c.getInt(c.getColumnIndex(DBGroups._ID)) == idGroup)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		// Make the delete button visible
		Button buttonDelete = (Button) getActivity().findViewById(R.id.buttonDelete);
		buttonDelete.setVisibility(View.VISIBLE);
    }
	
}
