package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBGroups;
import br.pro.just.pblhub.PBLDBContract.DBStudents;
import br.pro.just.pblhub.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

public class StudentsFragment extends Fragment {

	// Attributes for adapter
	private GroupSpinnerCursorAdapter adapterGroups;
	private SimpleCursorAdapter adapterStudents;
	
	// Callback for fragments communication
	OnHeadlineSelectedListener mCallback;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_students, container, false);

        // Get the ListView and Spinner objects
        ListView listView = (ListView) v.findViewById(R.id.listViewStudents);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinnerGroups);
        
	  	// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor cGroups = mDbHelper.getAllGroups();

    	// Fill the ListView and the Spinner
    	adapterGroups = new GroupSpinnerCursorAdapter(container.getContext(), cGroups, true);
    	spinner.setAdapter(adapterGroups);

    	// Set listener for List View
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Cursor item = (Cursor) parent.getAdapter().getItem(position);
    			mCallback.onStudentSelected(item);
    		}
    	}); 
    	
    	// Set listener for Spinner
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    			if (view == null)
    				return;
    			
    			// Filter students
    			PBLDBHelper mDbHelper = new PBLDBHelper(view.getContext());
    			Cursor item = (Cursor) parent.getAdapter().getItem(position);
    			String whereClause = DBStudents.COLUMN_NAME_GROUP_ID + "= ?";
        		String[] whereArgs = { item.getString(item.getColumnIndex(DBGroups._ID)) };
        		Cursor cStudents = mDbHelper.getStudentsWhere(whereClause, whereArgs);
        		
        		// Get List View and set its adapter
        		ListView listView = (ListView) parent.getRootView().findViewById(R.id.listViewStudents);
        		setListViewAdapter(view.getContext(), listView, cStudents);
    		}
    		
    		public void onNothingSelected(AdapterView<?> parent) {
    	        // Another interface callback
    	    }
    	}); 

    	// Filter list
		Cursor item = (Cursor) spinner.getAdapter().getItem(0);
		if (item.getCount() > 0) {
			String whereClause = DBStudents.COLUMN_NAME_GROUP_ID + "= ?";
			String[] whereArgs = { item.getString(item.getColumnIndex(DBGroups._ID)) };
			Cursor cStudents = mDbHelper.getStudentsWhere(whereClause, whereArgs);
			
			// Get List View and set its adapter
			setListViewAdapter(container.getContext(), listView, cStudents);
		}
		
		// Return the view
    	return v;
	}
	
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onStudentSelected(Cursor item);
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
    	String[] adapterCols = new String[]{"name"};
    	int[] adapterRowViews = new int[]{android.R.id.text1};
    	adapterStudents = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, adapterCols, adapterRowViews, 0);
    	l.setAdapter(adapterStudents);
    }
}
