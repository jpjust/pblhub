package br.pro.just.pblhub;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import br.pro.just.pblhub.R;

public class GroupsFragment extends Fragment {

	// Attributes for adapter
	private GroupCursorAdapter adapter;
	
	// Callback for fragments communication
	OnHeadlineSelectedListener mCallback;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_groups, container, false);

        // Get the ListView object
        ListView listView = (ListView) v.findViewById(R.id.listViewGroups);
        
	  	// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor c = mDbHelper.getAllGroups();

    	// Fill the ListView
    	adapter = new GroupCursorAdapter(container.getContext(), c, true);
    	listView.setAdapter(adapter);

    	// Set listener
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Cursor item = (Cursor) parent.getAdapter().getItem(position);
    			mCallback.onGroupSelected(item);
    		}
    	}); 
    	
    	// Return the view
    	return v;
	}
	
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onGroupSelected(Cursor item);
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

}
