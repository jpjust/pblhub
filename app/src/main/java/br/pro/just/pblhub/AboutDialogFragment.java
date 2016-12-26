package br.pro.just.pblhub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import br.pro.just.pblhub.R;

public class AboutDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Package Info
		PackageInfo pInfo;
		String version = "";
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			version = getActivity().getString(R.string.app_name) + " " + pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.about_alert)
		.setTitle(version)
		.setPositiveButton(R.string.close, null);
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
