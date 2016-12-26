package br.pro.just.pblhub;

import br.pro.just.pblhub.R;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("ValidFragment") public class GroupAddFragment extends Fragment {

	// Attributes for fragments communication
	final static String ARG_ID	 	 = "id";
	final static String ARG_ITEM 	 = "item";
	final static String ARG_CLASS_ID = "class_id";
	
	// Attributes for adapter
	private SimpleCursorAdapter adapter;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_groups_add, container, false);
        
		// Get the spinner object
		Spinner spinner = (Spinner) v.findViewById(R.id.spinnerClasses);

	  	// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor c = mDbHelper.getAllClasses();
    	
    	// Fill the spinner
    	String[] adapterCols = new String[]{"name", "_id"};
    	int[] adapterRowViews = new int[]{android.R.id.text1, android.R.id.text2};
    	adapter = new SimpleCursorAdapter(container.getContext(), android.R.layout.simple_spinner_item, c, adapterCols, adapterRowViews, 0);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            updateGroupName(args.getInt(ARG_ID), args.getInt(ARG_CLASS_ID), args.getString(ARG_ITEM));
        }
    }

    // Insert data into textbox for database update
    private void updateGroupName(int id, int idClass, String name) {
		TextView textId = (TextView) getActivity().findViewById(R.id.group_item_id);
		EditText editName = (EditText) getActivity().findViewById(R.id.editTextGroupName);
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerClasses);
		
		textId.setText(String.valueOf(id));
		editName.setText(name);
		
		// Select the correct item in the spinner
		for (int i = 0; i < spinner.getCount(); i++) {
			Cursor c = (Cursor)spinner.getItemAtPosition(i);
			
			if ((c != null) && (c.getInt(c.getColumnIndex(PBLDBContract.DBClasses._ID)) == idClass)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		// Make the delete button visible
		Button buttonDelete = (Button) getActivity().findViewById(R.id.buttonDelete);
		buttonDelete.setVisibility(View.VISIBLE);
    }
	
}
