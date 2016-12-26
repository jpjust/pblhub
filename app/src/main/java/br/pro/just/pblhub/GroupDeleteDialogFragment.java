package br.pro.just.pblhub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;

import br.pro.just.pblhub.R;

public class GroupDeleteDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.group_delete_alert)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Delete the item from database
		    	PBLDBHelper mDbHelper = new PBLDBHelper(getActivity());
		    	
		    	// Get data from fragment and delete it
		    	TextView textId = (TextView) getActivity().findViewById(R.id.group_item_id);
		    	mDbHelper.deleteGroup(Integer.parseInt(textId.getText().toString()));
		    	Toast.makeText(getActivity(), R.string.group_deleted, Toast.LENGTH_SHORT).show();    	
		    	
		    	// Go back to the classes list
		    	getActivity().getSupportFragmentManager().popBackStack();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
