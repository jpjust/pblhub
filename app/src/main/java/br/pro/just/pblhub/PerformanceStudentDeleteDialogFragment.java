package br.pro.just.pblhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import br.pro.just.pblhub.R;

public class PerformanceStudentDeleteDialogFragment extends DialogFragment {

	// ID for database lookup
	private int id;

	// Attributes for fragments communication
	final static String ARG_ID = "_id";
	
	// getID
	public int getItemId() {
		return this.id;
	}
	
	// Callback interface
    public interface NoticeDialogListener {
        public void onDialogStudentsDeleteClick(DialogFragment dialog);
    }
    
    // Instance to deliver action events
    NoticeDialogListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.student_delete_alert)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Delete the item from database
		    	PBLDBHelper mDbHelper = new PBLDBHelper(getActivity());
		    	PerformanceStudentDeleteDialogFragment parent = PerformanceStudentDeleteDialogFragment.this;
		    	mDbHelper.deletePerformance(parent.getItemId());
		    	Toast.makeText(getActivity(), R.string.student_deleted, Toast.LENGTH_SHORT).show();
		    	
		    	mListener.onDialogStudentsDeleteClick(PerformanceStudentDeleteDialogFragment.this);
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

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
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
            this.id = args.getInt(ARG_ID);
        }
    }
}
