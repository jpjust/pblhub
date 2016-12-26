package br.pro.just.pblhub;

import br.pro.just.pblhub.PBLDBContract.DBStudents;
import br.pro.just.pblhub.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SessionStudentsDialogFragment extends DialogFragment {

	// Callback interface
    public interface NoticeDialogListener {
        public void onDialogStudentsClick(DialogFragment dialog);
    }
    
    // Instance to deliver action events
    NoticeDialogListener mListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Get list of students
		TextView textSession = (TextView) getActivity().findViewById(R.id.session_item_id);
		TextView textGroup = (TextView) getActivity().findViewById(R.id.session_group_id);
		final PBLDBHelper mDbHelper = new PBLDBHelper(getActivity());

		final Cursor c = mDbHelper.getStudentsNotInSession(
				Integer.parseInt(textSession.getText().toString()),
				Integer.parseInt(textGroup.getText().toString())
			);
		
		// Create adapter
		String[] adapterCols = new String[]{"name"};
		int[] adapterRowViews = new int[]{android.R.id.text1};
		SimpleCursorAdapter adapterStudents = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, c, adapterCols, adapterRowViews, 0);

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.performance_choose_student)
		.setAdapter(adapterStudents, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				TextView textSession = (TextView) getActivity().findViewById(R.id.session_item_id);
				c.moveToPosition(which);
				
				mDbHelper.insertOrUpdateStudentPerformance(
						-1,
						Integer.parseInt(textSession.getText().toString()),
						c.getInt(c.getColumnIndex(DBStudents._ID)),
						0,
						0,
						0);

				mListener.onDialogStudentsClick(SessionStudentsDialogFragment.this);
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
}
