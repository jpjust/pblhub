package br.pro.just.pblhub;

import java.text.ParseException;
import java.util.Date;

import br.pro.just.pblhub.R;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks,
ClassesFragment.OnHeadlineSelectedListener,
GroupsFragment.OnHeadlineSelectedListener,
ProblemsFragment.OnHeadlineSelectedListener,
StudentsFragment.OnHeadlineSelectedListener,
SessionsFragment.OnHeadlineSelectedListener,
SessionStudentsDialogFragment.NoticeDialogListener,
PerformanceStudentDeleteDialogFragment.NoticeDialogListener,
SessionDateDialogFragment.SessionDateListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	// User has clicked an item from left menu
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();

		switch (position) {
		case 0:
			fragmentManager.beginTransaction()
			.replace(R.id.container, new SessionsFragment())
			.commit();
			break;

		case 1:
			fragmentManager.beginTransaction()
			.replace(R.id.container, new StudentsFragment())
			.commit();
			break;

		case 2:
			fragmentManager.beginTransaction()
			.replace(R.id.container, new ProblemsFragment())
			.commit();
			break;

		case 3:
			fragmentManager.beginTransaction()
			.replace(R.id.container, new GroupsFragment())
			.commit();
			break;

		case 4:
			fragmentManager.beginTransaction()
			.replace(R.id.container, new ClassesFragment())
			.commit();
			break;

		}

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 0:
			mTitle = getString(R.string.title_section_sessions);
			break;
		case 1:
			mTitle = getString(R.string.title_section_students);
			break;
		case 2:
			mTitle = getString(R.string.title_section_problems);
			break;
		case 3:
			mTitle = getString(R.string.title_section_groups);
			break;
		case 4:
			mTitle = getString(R.string.title_section_classes);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	// Adds the actions to the ActionBar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	// Responds to Action Buttons (ActionBar)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_about) {
			new AboutDialogFragment().show(getSupportFragmentManager(), null);
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			//TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			//textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	// ----- CLASSES FRAGMENT -----
	// Button "Add new class" clicked
	public void clickAddClass(View view) {
		// Replace fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, new ClassAddFragment());

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Button "Save class" clicked
	public void clickSaveClass(View view) {
		// Get data
		EditText textClassName = (EditText) findViewById(R.id.editTextClassName);
		TextView textId = (TextView) findViewById(R.id.class_item_id);

		// Insert the new class into the database
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		mDbHelper.insertOrUpdateClass(
				Integer.parseInt(textId.getText().toString()),
				textClassName.getText().toString()
				);

		Toast.makeText(this, R.string.class_added, Toast.LENGTH_SHORT).show();

		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Cancel" clicked (class add fragment)
	public void clickClassAddCancel(View view) {
		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Delete" clicked (class add fragment)
	public void clickClassAddDelete(View view) {
		new ClassDeleteDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Callback to ClassesFragment
	@Override
	public void onClassSelected(Cursor item) {
		// Create fragment and give it an argument for the selected item
		ClassAddFragment newFragment = new ClassAddFragment();

		Bundle args = new Bundle();
		args.putInt(ClassAddFragment.ARG_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBClasses._ID)));
		args.putString(ClassAddFragment.ARG_ITEM, item.getString(item.getColumnIndex(PBLDBContract.DBClasses.COLUMN_NAME_NAME)));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace fragment
		transaction.replace(R.id.container, newFragment);

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// ----- GROUPS FRAGMENT -----
	// Button "Add new group" clicked
	public void clickAddGroup(View view) {
		// First, we need to check if at least one class have already been added
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		if (mDbHelper.getAllClasses().getCount() == 0) {
			Toast.makeText(this, R.string.groups_no_existing_class, Toast.LENGTH_LONG).show();
			return;
		}

		// Replace fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, new GroupAddFragment());

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Button "Save group" clicked
	public void clickSaveGroup(View view) {
		// Get data from fragment
		EditText textGroupName = (EditText) findViewById(R.id.editTextGroupName);
		TextView textId = (TextView) findViewById(R.id.group_item_id);
		Spinner spinnerClass = (Spinner) findViewById(R.id.spinnerClasses);
		Cursor c = (Cursor) spinnerClass.getSelectedItem();

		// Insert the new group into the database
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		mDbHelper.insertOrUpdateGroup(
				Integer.parseInt(textId.getText().toString()),
				c.getInt(c.getColumnIndex(PBLDBContract.DBClasses._ID)),
				textGroupName.getText().toString()
				);

		Toast.makeText(this, R.string.group_added, Toast.LENGTH_SHORT).show();

		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Cancel" clicked (group add fragment)
	public void clickGroupAddCancel(View view) {
		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Delete" clicked (groups add fragment)
	public void clickGroupAddDelete(View view) {
		new GroupDeleteDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Callback to GroupsFragment
	public void onGroupSelected(Cursor item) {
		// Create fragment and give it an argument for the selected item
		GroupAddFragment newFragment = new GroupAddFragment();

		Bundle args = new Bundle();
		args.putInt(GroupAddFragment.ARG_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBGroups._ID)));
		args.putInt(GroupAddFragment.ARG_CLASS_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBGroups.COLUMN_NAME_CLASS_ID)));
		args.putString(GroupAddFragment.ARG_ITEM, item.getString(item.getColumnIndex(PBLDBContract.DBGroups.COLUMN_NAME_NAME)));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace fragment
		transaction.replace(R.id.container, newFragment);

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// ----- PROBLEMS FRAGMENT -----
	// Button "Add new problem" clicked
	public void clickAddProblem(View view) {
		// First, we need to check if at least one group have already been added
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		if (mDbHelper.getAllGroups().getCount() == 0) {
			Toast.makeText(this, R.string.problems_no_existing_group, Toast.LENGTH_LONG).show();
			return;
		}

		// Replace fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, new ProblemAddFragment());

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Button "Save problem" clicked
	public void clickSaveProblem(View view) {
		// Get data from fragment
		EditText textProblemName = (EditText) findViewById(R.id.editTextProblemName);
		EditText textProblemDesc = (EditText) findViewById(R.id.editTextProblemDesc);
		TextView textId = (TextView) findViewById(R.id.problem_item_id);
		Spinner spinnerGroup = (Spinner) findViewById(R.id.spinnerGroups);
		Cursor c = (Cursor) spinnerGroup.getSelectedItem();

		// Insert the new group into the database
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		mDbHelper.insertOrUpdateProblem(
				Integer.parseInt(textId.getText().toString()),
				c.getInt(c.getColumnIndex(PBLDBContract.DBGroups._ID)),
				textProblemName.getText().toString(),
				textProblemDesc.getText().toString()
				);

		Toast.makeText(this, R.string.problem_added, Toast.LENGTH_SHORT).show();

		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Cancel" clicked (problem add fragment)
	public void clickProblemAddCancel(View view) {
		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Delete" clicked (problems add fragment)
	public void clickProblemAddDelete(View view) {
		new ProblemDeleteDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Callback to ProblemsFragment
	public void onProblemSelected(Cursor item) {
		// Create fragment and give it an argument for the selected item
		ProblemAddFragment newFragment = new ProblemAddFragment();

		Bundle args = new Bundle();
		args.putInt(ProblemAddFragment.ARG_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBProblems._ID)));
		args.putInt(ProblemAddFragment.ARG_GROUP_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBProblems.COLUMN_NAME_GROUP_ID)));
		args.putString(ProblemAddFragment.ARG_ITEM, item.getString(item.getColumnIndex(PBLDBContract.DBProblems.COLUMN_NAME_TITLE)));
		args.putString(ProblemAddFragment.ARG_DESC, item.getString(item.getColumnIndex(PBLDBContract.DBProblems.COLUMN_NAME_DESCRIPTION)));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace fragment
		transaction.replace(R.id.container, newFragment);

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// ----- STUDENTS FRAGMENT -----
	// Button "Add new student" clicked
	public void clickAddStudent(View view) {
		// First, we need to check if at least one group have already been added
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		if (mDbHelper.getAllGroups().getCount() == 0) {
			Toast.makeText(this, R.string.students_no_existing_group, Toast.LENGTH_LONG).show();
			return;
		}

		// Replace fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, new StudentAddFragment());

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Button "Save student" clicked
	public void clickSaveStudent(View view) {
		// Get data from fragment
		EditText textStudentName = (EditText) findViewById(R.id.editTextStudentName);
		EditText textStudentNumber = (EditText) findViewById(R.id.editTextStudentNumber);
		TextView textId = (TextView) findViewById(R.id.student_item_id);
		Spinner spinnerGroup = (Spinner) findViewById(R.id.spinnerGroups);
		Cursor c = (Cursor) spinnerGroup.getSelectedItem();

		// Insert the new group into the database
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		mDbHelper.insertOrUpdateStudent(
				Integer.parseInt(textId.getText().toString()),
				c.getInt(c.getColumnIndex(PBLDBContract.DBGroups._ID)),
				Integer.parseInt("0" + textStudentNumber.getText().toString()),
				textStudentName.getText().toString()
				);

		Toast.makeText(this, R.string.student_added, Toast.LENGTH_SHORT).show();

		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Cancel" clicked (student add fragment)
	public void clickStudentAddCancel(View view) {
		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Delete" clicked (students add fragment)
	public void clickStudentAddDelete(View view) {
		new StudentDeleteDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Callback to StudentsFragment
	public void onStudentSelected(Cursor item) {
		// Create fragment and give it an argument for the selected item
		StudentAddFragment newFragment = new StudentAddFragment();

		Bundle args = new Bundle();
		args.putInt(StudentAddFragment.ARG_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBStudents._ID)));
		args.putInt(StudentAddFragment.ARG_GROUP_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBStudents.COLUMN_NAME_GROUP_ID)));
		args.putString(StudentAddFragment.ARG_ITEM, item.getString(item.getColumnIndex(PBLDBContract.DBStudents.COLUMN_NAME_NAME)));
		args.putInt(StudentAddFragment.ARG_NUMBER, item.getInt(item.getColumnIndex(PBLDBContract.DBStudents.COLUMN_NAME_ID_NUMBER)));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace fragment
		transaction.replace(R.id.container, newFragment);

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// ----- SESSIONS FRAGMENT -----
	// Button "Add new session" clicked
	public void clickAddSession(View view) {
		// First, we need to check if at least one problem have already been added
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		if (mDbHelper.getAllProblems().getCount() == 0) {
			Toast.makeText(this, R.string.sessions_no_existing_problem, Toast.LENGTH_LONG).show();
			return;
		}

		// Replace fragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, new SessionAddFragment());

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Button "Save session" clicked
	public void clickSaveSession(View view) {
		// Get data from fragment
		EditText textDate = (EditText) findViewById(R.id.editTextSessionDate);
		EditText textGoals = (EditText) findViewById(R.id.editTextSessionGoals);
		TextView textId = (TextView) findViewById(R.id.session_item_id);
		Spinner spinnerProblem = (Spinner) findViewById(R.id.spinnerProblems);
		Cursor c = (Cursor) spinnerProblem.getSelectedItem();

		// Transform date from locale format to SQL format
		int year, month, day;
		java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this);
		String dateSQL = "0000-00-00";

		try {
			Date d = formatter.parse(textDate.getText().toString());
			year = d.getYear() + 1900;
			month = d.getMonth() + 1;
			day = d.getDate();
			dateSQL = String.format("%d-%02d-%02d", year, month, day);
			Toast.makeText(this, dateSQL, Toast.LENGTH_SHORT).show();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Insert the new session into the database
		PBLDBHelper mDbHelper = new PBLDBHelper(this);
		mDbHelper.insertOrUpdateSession(
				Integer.parseInt(textId.getText().toString()),
				c.getInt(c.getColumnIndex(PBLDBContract.DBProblems._ID)),
				dateSQL,
				textGoals.getText().toString()
				);

		Toast.makeText(this, R.string.session_added, Toast.LENGTH_SHORT).show();

		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Cancel" clicked (student add fragment)
	public void clickSessionAddCancel(View view) {
		// Go back to the classes list
		getSupportFragmentManager().popBackStack();
	}

	// Button "Delete" clicked (students add fragment)
	public void clickSessionAddDelete(View view) {
		new SessionDeleteDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Callback to SessionsFragment
	public void onSessionSelected(Cursor item) {
		// Create fragment and give it an argument for the selected item
		SessionAddFragment newFragment = new SessionAddFragment();

		// Transform date from SQL format to locale format
		int year, month, day;
		java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this);
		String dateSQL = item.getString(item.getColumnIndex(PBLDBContract.DBSessions.COLUMN_NAME_DATE));
		String[] dateArray = dateSQL.split("-");

		year = Integer.parseInt(dateArray[0]) - 1900;
		month = Integer.parseInt(dateArray[1]) - 1;
		day = Integer.parseInt(dateArray[2]);
		Date d = new Date(year, month, day);

		// Insert args
		Bundle args = new Bundle();
		args.putInt(SessionAddFragment.ARG_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBSessions._ID)));
		args.putString(SessionAddFragment.ARG_DATE, formatter.format(d));
		args.putString(SessionAddFragment.ARG_GOALS, item.getString(item.getColumnIndex(PBLDBContract.DBSessions.COLUMN_NAME_GOALS)));
		args.putInt(SessionAddFragment.ARG_PROBLEM_ID, item.getInt(item.getColumnIndex(PBLDBContract.DBSessions.COLUMN_NAME_PROBLEM_ID)));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace fragment
		transaction.replace(R.id.container, newFragment);

		// Put the older fragment to the back stack
		transaction.addToBackStack(null);
		transaction.commit();
	}

	// Pick a date for session
	public void onSessionDateClick(int year, int month, int day) {
		java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this);
		Date d = new Date(year - 1900, month, day);

		EditText editDate = (EditText) findViewById(R.id.editTextSessionDate);
		editDate.setText(formatter.format(d));
	}

	// Edit date for session clicked
	public void clickSessionDate(View view) {
		SessionDateDialogFragment dialog = new SessionDateDialogFragment();
		EditText editDate = (EditText) findViewById(R.id.editTextSessionDate);

		java.text.DateFormat formatter = android.text.format.DateFormat.getDateFormat(this);
		try {
			Date d = formatter.parse(editDate.getText().toString());

			Bundle args = new Bundle();
			args.putInt(SessionDateDialogFragment.ARG_YEAR, d.getYear() + 1900);
			args.putInt(SessionDateDialogFragment.ARG_MONTH, d.getMonth());
			args.putInt(SessionDateDialogFragment.ARG_DAY, d.getDate());
			dialog.setArguments(args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		dialog.show(getSupportFragmentManager(), "datePicker");
	}

	// Opens performance fragment
	public void openPerformance() {
		// Create fragment and give it an argument for the selected item
		SessionPerformanceFragment newFragment = new SessionPerformanceFragment();
		TextView textId = (TextView) findViewById(R.id.session_item_id);

		Bundle args = new Bundle();
		args.putInt(SessionPerformanceFragment.ARG_SESSION_ID, Integer.parseInt(textId.getText().toString()));
		newFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Check if the fragment is already shown
		SessionPerformanceFragment fragment = (SessionPerformanceFragment) getSupportFragmentManager().findFragmentByTag("fragment_performance");

		// Put the older fragment to the back stack only if there was no fragment shown yet
		if (fragment == null) {
			transaction.replace(R.id.container, newFragment, "fragment_performance");
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			fragment.requeryCursor();
		}
	}

	// Button "Performance" clicked
	public void clickSessionPerformance(View view) {
		// Check if the session is already created
		TextView textId = (TextView) findViewById(R.id.session_item_id);
		if (Integer.parseInt(textId.getText().toString()) == -1) {
			Toast.makeText(this, R.string.session_not_saved_yet, Toast.LENGTH_SHORT).show();
		} else {
			openPerformance();
		}
	}

	// Button "Add student" clicked (performance fragment)
	public void clickSessionAddStudent(View view) {
		new SessionStudentsDialogFragment().show(getSupportFragmentManager(), null);
	}

	// Student choosed
	@Override
	public void onDialogStudentsClick(DialogFragment dialog) {
		openPerformance();
	}

	// Button "Delete" clicked (students performance)
	public void clickPerformanceStudentDelete(View view) {
		// Get data from fragment and delete it
		int id = Integer.parseInt((String)view.getTag());

		Bundle args = new Bundle();
		args.putInt(PerformanceStudentDeleteDialogFragment.ARG_ID, id);

		PerformanceStudentDeleteDialogFragment newFragment = new PerformanceStudentDeleteDialogFragment();
		newFragment.setArguments(args);
		newFragment.show(getSupportFragmentManager(), null);
	}

	// Student removed
	@Override
	public void onDialogStudentsDeleteClick(DialogFragment dialog) {
		openPerformance();
	}

}
