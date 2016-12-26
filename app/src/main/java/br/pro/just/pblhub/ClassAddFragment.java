package br.pro.just.pblhub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.pro.just.pblhub.R;

@SuppressLint("ValidFragment") public class ClassAddFragment extends Fragment {

	// Attributes for fragments communication
	final static String ARG_ID		= "id";
	final static String ARG_ITEM	= "item";
	
	// On create view
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classes_add, container, false);
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
            updateClassName(args.getInt(ARG_ID), args.getString(ARG_ITEM));
        }
    }

    // Insert data into textbox for database update
    private void updateClassName(int id, String name) {
		TextView textId = (TextView) getActivity().findViewById(R.id.class_item_id);
		EditText editName = (EditText) getActivity().findViewById(R.id.editTextClassName);
		
		textId.setText(String.valueOf(id));
		editName.setText(name);

		// Make the delete button visible
		Button buttonDelete = (Button) getActivity().findViewById(R.id.buttonDelete);
		buttonDelete.setVisibility(View.VISIBLE);
    }
	
}
