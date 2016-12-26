package br.pro.just.pblhub;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import br.pro.just.pblhub.R;

public class ClassesFragment extends Fragment {

	// Attributes for adapter
	private SimpleCursorAdapter adapter;
	
	// Callback for fragments communication
	OnHeadlineSelectedListener mCallback;

	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_classes, container, false);

        // Get the ListView object
        ListView listView = (ListView) v.findViewById(R.id.listViewClasses);
		
		// Get data
    	PBLDBHelper mDbHelper = new PBLDBHelper(container.getContext());
    	Cursor c = mDbHelper.getAllClasses();
    	
    	// Fill the ListView
    	String[] adapterCols = new String[]{"name"};
    	int[] adapterRowViews = new int[]{android.R.id.text1};
    	adapter = new SimpleCursorAdapter(container.getContext(), android.R.layout.simple_list_item_1, c, adapterCols, adapterRowViews, 0);
    	listView.setAdapter(adapter);

    	// Set listener
    	listView.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Cursor item = (Cursor) parent.getAdapter().getItem(position);
    			mCallback.onClassSelected(item);
    		}
    	}); 

        // Inflate the layout for this fragment
        return v;
    }
	
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onClassSelected(Cursor item);
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
