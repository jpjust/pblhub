package br.pro.just.pblhub;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class SessionDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	// Attributes for fragments communication
	final static String ARG_YEAR 	 = "year";
	final static String ARG_MONTH 	 = "month";
	final static String ARG_DAY 	 = "day";

	// Callback interface
    public interface SessionDateListener {
        public void onSessionDateClick(int year, int month, int day);
    }
    
    // Instance to deliver action events
    SessionDateListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int year, month, day;
		
		Bundle args = getArguments();
		
		if (args != null) {
			year = args.getInt(ARG_YEAR);
			month = args.getInt(ARG_MONTH);
			day = args.getInt(ARG_DAY);
		} else {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		mListener.onSessionDateClick(arg1, arg2, arg3);
	}
	
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SessionDateListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SessionDateListener");
        }
    }
}
