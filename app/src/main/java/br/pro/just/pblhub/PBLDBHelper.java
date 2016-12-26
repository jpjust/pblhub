package br.pro.just.pblhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.pro.just.pblhub.PBLDBContract.*;

public class PBLDBHelper extends SQLiteOpenHelper {

	// Database attributes
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "PBLHub.db";

	// Create database statement
	private static final String SQL_CREATE_TABLE_CLASSES =
			"CREATE TABLE " + DBClasses.TABLE_NAME + " (" +
					DBClasses._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBClasses.COLUMN_NAME_NAME + " TEXT NOT NULL " +
					" )";

	private static final String SQL_CREATE_TABLE_GROUPS =
			"CREATE TABLE " + DBGroups.TABLE_NAME + " (" +
					DBGroups._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBGroups.COLUMN_NAME_CLASS_ID + " INTEGER REFERENCES " + DBClasses.TABLE_NAME + "(" + DBClasses._ID + ") ON DELETE CASCADE," +
					DBGroups.COLUMN_NAME_NAME + " TEXT NOT NULL " +
					" )";

	private static final String SQL_CREATE_TABLE_PROBLEMS =
			"CREATE TABLE " + DBProblems.TABLE_NAME + " (" +
					DBProblems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBProblems.COLUMN_NAME_GROUP_ID + " INTEGER REFERENCES " + DBGroups.TABLE_NAME + "(" + DBGroups._ID + ") ON DELETE CASCADE," +
					DBProblems.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
					DBProblems.COLUMN_NAME_DESCRIPTION + " TEXT" +
					" )";

	private static final String SQL_CREATE_TABLE_SESSIONS =
			"CREATE TABLE " + DBSessions.TABLE_NAME + " (" +
					DBSessions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBSessions.COLUMN_NAME_PROBLEM_ID + " INTEGER REFERENCES " + DBProblems.TABLE_NAME + "(" + DBProblems._ID + ") ON DELETE CASCADE," +
					DBSessions.COLUMN_NAME_DATE + " TEXT NOT NULL," +
					DBSessions.COLUMN_NAME_GOALS + " TEXT," +
					DBSessions.COLUMN_NAME_COORDINATOR_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE," +
					DBSessions.COLUMN_NAME_BOARD_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE," +
					DBSessions.COLUMN_NAME_RECORDER_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE," +
					DBSessions.COLUMN_NAME_GRADE_COORDINATOR + " REAL," +
					DBSessions.COLUMN_NAME_GRADE_BOARD + " REAL," +
					DBSessions.COLUMN_NAME_GRADE_RECORDER + " REAL," +
					DBSessions.COLUMN_NAME_NO_ACCOMPLISHMENT + " INTEGER" +
					" )";

	private static final String SQL_CREATE_TABLE_STUDENTS =
			"CREATE TABLE " + DBStudents.TABLE_NAME + " (" +
					DBStudents._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBStudents.COLUMN_NAME_GROUP_ID + " INTEGER REFERENCES " + DBGroups.TABLE_NAME + "(" + DBGroups._ID + ") ON DELETE CASCADE," +
					DBStudents.COLUMN_NAME_ID_NUMBER + " INTEGER NOT NULL," +
					DBStudents.COLUMN_NAME_NAME + " TEXT NOT NULL " +
					" )";

	private static final String SQL_CREATE_TABLE_PERFORMANCE =
			"CREATE TABLE " + DBPerformance.TABLE_NAME + " (" +
					DBPerformance._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					DBPerformance.COLUMN_NAME_ACCOMPLISHMENT + " REAL," +
					DBPerformance.COLUMN_NAME_BEHAVIOR + " REAL," +
					DBPerformance.COLUMN_NAME_PARTICIPATION + " REAL," +
					DBPerformance.COLUMN_NAME_SESSION_ID + " INTEGER REFERENCES " + DBSessions.TABLE_NAME + "(" + DBSessions._ID + ") ON DELETE CASCADE," +
					DBPerformance.COLUMN_NAME_STUDENT_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE " +
					" )";

	// Delete database statement
	private static final String SQL_DELETE_TABLE_CLASSES =    	"DROP TABLE IF EXISTS " + DBClasses.TABLE_NAME;
	private static final String SQL_DELETE_TABLE_GROUPS =    	"DROP TABLE IF EXISTS " + DBGroups.TABLE_NAME;
	private static final String SQL_DELETE_TABLE_PROBLEMS =    	"DROP TABLE IF EXISTS " + DBProblems.TABLE_NAME;
	private static final String SQL_DELETE_TABLE_STUDENTS =    	"DROP TABLE IF EXISTS " + DBStudents.TABLE_NAME;
	private static final String SQL_DELETE_TABLE_SESSIONS =    	"DROP TABLE IF EXISTS " + DBSessions.TABLE_NAME;
	private static final String SQL_DELETE_TABLE_PERFORMANCE =	"DROP TABLE IF EXISTS " + DBPerformance.TABLE_NAME;

	// Constructor
	public PBLDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON"); 
	}

	// Create a new database on device
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_CLASSES);
		db.execSQL(SQL_CREATE_TABLE_GROUPS);
		db.execSQL(SQL_CREATE_TABLE_PROBLEMS);
		db.execSQL(SQL_CREATE_TABLE_STUDENTS);
		db.execSQL(SQL_CREATE_TABLE_SESSIONS);
		db.execSQL(SQL_CREATE_TABLE_PERFORMANCE);
	}

	// Upgrade database on device
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int upgradeFrom = oldVersion;

		while (upgradeFrom < newVersion) {
			switch (upgradeFrom) {
			case 1:	// Upgrade from 1 to 2
				// Table 'performance', column 'role' will be deleted
				// Rename the old table
				db.execSQL("ALTER TABLE " + DBPerformance.TABLE_NAME + " RENAME TO performance_bak");
				
				// Create a new one withou the deleted column
				db.execSQL(SQL_CREATE_TABLE_PERFORMANCE);
				
				// Copy the backup
				db.execSQL("INSERT INTO " + DBPerformance.TABLE_NAME + " SELECT * FROM performance_bak");
				
				// Delete the backup table
				db.execSQL("DROP TABLE performance_bak");
				
				// Create new columns in table 'sessions'
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_COORDINATOR_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_BOARD_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_RECORDER_ID + " INTEGER REFERENCES " + DBStudents.TABLE_NAME + "(" + DBStudents._ID + ") ON DELETE CASCADE");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_GRADE_COORDINATOR + " REAL");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_GRADE_BOARD + " REAL");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_GRADE_RECORDER + " REAL");
				db.execSQL("ALTER TABLE " + DBSessions.TABLE_NAME + " ADD COLUMN " + DBSessions.COLUMN_NAME_NO_ACCOMPLISHMENT + " INTEGER");

				upgradeFrom++;
			}
		}
	}

	// ----- CLASSES TABLE -----
	// Returns a cursor with all classes
	public Cursor getAllClasses() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBClasses.TABLE_NAME,				// The table to query
				null,                			    // The columns to return
				null,                  			    // The columns for the WHERE clause
				null,           			        // The values for the WHERE clause
				null,               			    // don't group the rows
				null,                   			// don't filter by row groups
				DBClasses.COLUMN_NAME_NAME + " ASC"	// The sort order
				);

		return c;
	}

	// Returns a cursor with classes using a where clause
	public Cursor getClassWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBClasses.TABLE_NAME,	// The table to query
				null,                   // The columns to return
				whereClause,            // The columns for the WHERE clause
				whereArgs,              // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				DBClasses.COLUMN_NAME_NAME + " ASC"  // The sort order
				);

		return c;
	}

	// Insert or update a class
	public void insertOrUpdateClass(int id, String name) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();
		values.put(DBClasses.COLUMN_NAME_NAME, name);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBClasses._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			db.update(DBClasses.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			db.insert(DBClasses.TABLE_NAME, null, values);
		}

		//db.endTransaction();
	}

	// Delete a class
	public void deleteClass(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBClasses._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBClasses.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

	// ----- GROUPS TABLE -----
	// Returns a cursor with all groups
	public Cursor getAllGroups() {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBGroups.TABLE_NAME + ".*," +
						DBClasses.TABLE_NAME + "." + DBClasses.COLUMN_NAME_NAME + " AS class_name " +
						" FROM " + DBGroups.TABLE_NAME + "," + DBClasses.TABLE_NAME +
						" WHERE " + DBGroups.TABLE_NAME + "." + DBGroups.COLUMN_NAME_CLASS_ID + "=" + DBClasses.TABLE_NAME + "." + DBClasses._ID +
						" ORDER BY " + DBGroups.TABLE_NAME + "." + DBGroups.COLUMN_NAME_NAME + " ASC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns a cursor with groups using a where clause
	public Cursor getGroupWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBGroups.TABLE_NAME,				// The table to query
				null,              			        // The columns to return
				whereClause,     			        // The columns for the WHERE clause
				whereArgs,       			        // The values for the WHERE clause
				null,           			        // don't group the rows
				null,       			            // don't filter by row groups
				DBGroups.COLUMN_NAME_NAME + " ASC"	// The sort order
				);

		return c;
	}

	// Returns a group by session id
	public Cursor getGroupBySessionId(int sessionId) {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBGroups.TABLE_NAME + ".*" +
						" FROM " + DBGroups.TABLE_NAME + "," + DBProblems.TABLE_NAME + "," + DBSessions.TABLE_NAME +
						" WHERE " + DBGroups.TABLE_NAME + "." + DBGroups._ID + "=" + DBProblems.TABLE_NAME + "." + DBProblems.COLUMN_NAME_GROUP_ID +
						" AND " + DBProblems.TABLE_NAME + "." + DBProblems._ID + "=" + DBSessions.TABLE_NAME + "." + DBSessions.COLUMN_NAME_PROBLEM_ID +
						" AND " + DBSessions.TABLE_NAME + "." + DBSessions._ID + "=" + String.valueOf(sessionId) +
						" ORDER BY " + DBGroups.TABLE_NAME + "." + DBGroups.COLUMN_NAME_NAME + " ASC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Insert or update a group
	public void insertOrUpdateGroup(int id, int classId, String name) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();
		values.put(DBGroups.COLUMN_NAME_NAME, name);
		values.put(DBGroups.COLUMN_NAME_CLASS_ID, classId);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBGroups._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			db.update(DBGroups.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			db.insert(DBGroups.TABLE_NAME, null, values);
		}

		//db.endTransaction();
	}

	// Delete a group
	public void deleteGroup(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBGroups._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBGroups.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

	// ----- PROBLEMS TABLE -----
	// Returns a cursor with all problems
	public Cursor getAllProblems() {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBProblems.TABLE_NAME + ".*," +
						DBGroups.TABLE_NAME + "." + DBGroups.COLUMN_NAME_NAME + " AS group_name," +
						DBClasses.TABLE_NAME + "." + DBClasses.COLUMN_NAME_NAME + " AS class_name" +
						" FROM " + DBProblems.TABLE_NAME + "," + DBGroups.TABLE_NAME + "," + DBClasses.TABLE_NAME +
						" WHERE " + DBProblems.TABLE_NAME + "." + DBProblems.COLUMN_NAME_GROUP_ID + "=" + DBGroups.TABLE_NAME + "." + DBGroups._ID +
						" AND " + DBGroups.TABLE_NAME + "." + DBGroups.COLUMN_NAME_CLASS_ID + "=" + DBClasses.TABLE_NAME + "." + DBClasses._ID +
						" ORDER BY " + DBProblems.TABLE_NAME + "." + DBProblems.COLUMN_NAME_TITLE + " ASC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns a cursor with problems using a where clause
	public Cursor getProblemWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBProblems.TABLE_NAME,	// The table to query
				null,                   // The columns to return
				whereClause,            // The columns for the WHERE clause
				whereArgs,              // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				DBProblems.COLUMN_NAME_TITLE + " ASC"                    // The sort order
				);

		return c;
	}

	// Insert or update a problem
	public void insertOrUpdateProblem(int id, int groupId, String name, String description) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();
		values.put(DBProblems.COLUMN_NAME_TITLE, name);
		values.put(DBProblems.COLUMN_NAME_DESCRIPTION, description);
		values.put(DBProblems.COLUMN_NAME_GROUP_ID, groupId);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBProblems._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			db.update(DBProblems.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			db.insert(DBProblems.TABLE_NAME, null, values);
		}

		//db.endTransaction();
	}

	// Delete a problem
	public void deleteProblem(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBProblems._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBProblems.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

	// ----- STUDENTS TABLE -----
	// Returns a cursor with all problems
	public Cursor getAllStudents() {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBStudents.TABLE_NAME + ".*" +
						" FROM " + DBStudents.TABLE_NAME +
						" ORDER BY " + DBStudents.COLUMN_NAME_NAME;

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns a cursor with students using a where clause
	public Cursor getStudentsWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBStudents.TABLE_NAME,	// The table to query
				null,                   // The columns to return
				whereClause,            // The columns for the WHERE clause
				whereArgs,              // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				DBStudents.COLUMN_NAME_NAME + " ASC"                    // The sort order
				);

		return c;
	}

	// Returns a String array of students using a where clause
	public String[] getStudentsArray(String whereClause, String[] whereArgs) {
		Cursor c = getStudentsWhere(whereClause, whereArgs);
		String[] array = new String[c.getCount()];

		for (int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			array[i] = c.getString(c.getColumnIndex(DBStudents.COLUMN_NAME_NAME));
		}

		return array;
	}

	// Insert or update a student
	public void insertOrUpdateStudent(int id, int groupId, int number, String name) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();
		values.put(DBStudents.COLUMN_NAME_NAME, name);
		values.put(DBStudents.COLUMN_NAME_GROUP_ID, groupId);
		values.put(DBStudents.COLUMN_NAME_ID_NUMBER, number);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBStudents._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			db.update(DBStudents.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			db.insert(DBStudents.TABLE_NAME, null, values);
		}

		//db.endTransaction();
	}

	// Delete a student
	public void deleteStudent(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBStudents._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBStudents.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

	// ----- SESSIONS TABLE -----
	// Returns a cursor with all sessions
	public Cursor getAllSessions() {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBSessions.TABLE_NAME + ".*," +
						" FROM " + DBSessions.TABLE_NAME +
						" ORDER BY " + DBSessions.TABLE_NAME + "." + DBSessions.COLUMN_NAME_DATE + " DESC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns a cursor with sessions using a where clause
	public Cursor getSessionsWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBSessions.TABLE_NAME,	// The table to query
				null,                   // The columns to return
				whereClause,            // The columns for the WHERE clause
				whereArgs,              // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				DBSessions.COLUMN_NAME_DATE + " DESC"                    // The sort order
				);

		return c;
	}

	// Insert or update a session and returns its id
	public long insertOrUpdateSession(int id, int problemId, String date, String goals) {
		SQLiteDatabase db = getWritableDatabase();
        long new_id;

		// Create values object
		ContentValues values = new ContentValues();

		values.put(DBSessions.COLUMN_NAME_PROBLEM_ID, problemId);
		values.put(DBSessions.COLUMN_NAME_DATE, date);
		values.put(DBSessions.COLUMN_NAME_GOALS, goals);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBSessions._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			new_id = db.update(DBSessions.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			new_id = db.insert(DBSessions.TABLE_NAME, null, values);
		}

		//db.endTransaction();
		return new_id;
	}

	// Delete a session
	public void deleteSession(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBSessions._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBSessions.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

	// ----- PERFORMANCE TABLE -----
	// Returns a cursor with all performances
	public Cursor getAllPerformances() {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBPerformance.TABLE_NAME + ".*," +
						DBSessions.TABLE_NAME + "." + DBSessions.COLUMN_NAME_DATE + " AS session_date," +
						DBStudents.TABLE_NAME + "." + DBStudents.COLUMN_NAME_NAME + " AS student_name" +
						" FROM " + DBPerformance.TABLE_NAME + "," + DBSessions.TABLE_NAME + "," + DBStudents.TABLE_NAME +
						" WHERE " + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_SESSION_ID + "=" + DBSessions.TABLE_NAME + "." + DBSessions._ID +
						" AND " + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_STUDENT_ID + "=" + DBStudents.TABLE_NAME + "." + DBStudents._ID +
						" ORDER BY student_name ASC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns a cursor with performances using a where clause
	public Cursor getPerformancesWhere(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(
				DBPerformance.TABLE_NAME,	// The table to query
				null,                   // The columns to return
				whereClause,            // The columns for the WHERE clause
				whereArgs,              // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				null                    // The sort order
				);

		return c;
	}

	// Returns a cursor with performances using its id
	public Cursor getPerformanceById(int id) {
		String whereClause = DBPerformance._ID + "=?";
		String[] whereArgs = { String.valueOf(id) };

		return getPerformancesWhere(whereClause, whereArgs);
	}

	// Returns a performance row by session id
	public Cursor getPerformanceBySessionId(int sessionId) {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBPerformance.TABLE_NAME + ".*," +
						DBSessions.TABLE_NAME + "." + DBSessions.COLUMN_NAME_DATE + " AS session_date," +
						DBStudents.TABLE_NAME + "." + DBStudents.COLUMN_NAME_NAME + " AS student_name" +
						" FROM " + DBPerformance.TABLE_NAME + "," + DBSessions.TABLE_NAME + "," + DBStudents.TABLE_NAME +
						" WHERE " + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_SESSION_ID + "=" + String.valueOf(sessionId) +
						" AND " + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_STUDENT_ID + "=" + DBStudents.TABLE_NAME + "." + DBStudents._ID +
						" AND " + DBSessions.TABLE_NAME + "." + DBSessions._ID + "=" + String.valueOf(sessionId) +
						" ORDER BY student_name ASC";

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Returns students that are not in a session
	public Cursor getStudentsNotInSession(int sessionId, int groupId) {
		SQLiteDatabase db = getReadableDatabase();

		String SQL_QUERY = 
				"SELECT " + 
						DBStudents.TABLE_NAME + ".*" +
						" FROM " + DBStudents.TABLE_NAME +
						" WHERE " + DBStudents.TABLE_NAME + "." + DBStudents.COLUMN_NAME_GROUP_ID + "=" + String.valueOf(groupId) +
						" EXCEPT " +
						"SELECT " +
						DBStudents.TABLE_NAME + ".*" +
						" FROM " + DBStudents.TABLE_NAME + "," + DBPerformance.TABLE_NAME +
						" WHERE " + DBStudents.TABLE_NAME + "." + DBStudents._ID + "=" + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_STUDENT_ID +
						" AND " + DBPerformance.TABLE_NAME + "." + DBPerformance.COLUMN_NAME_SESSION_ID + "=" + String.valueOf(sessionId) +
						" ORDER BY " + DBStudents.TABLE_NAME + "." + DBStudents.COLUMN_NAME_NAME;

		Cursor c = db.rawQuery(SQL_QUERY, null);

		return c;
	}

	// Insert or update a performance row
	public void insertOrUpdateStudentPerformance(int id, int sessionId, int studentId, int accomplishment, int participation, int behavior) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();

		values.put(DBPerformance.COLUMN_NAME_SESSION_ID, sessionId);
		values.put(DBPerformance.COLUMN_NAME_STUDENT_ID, studentId);
		values.put(DBPerformance.COLUMN_NAME_ACCOMPLISHMENT, accomplishment);
		values.put(DBPerformance.COLUMN_NAME_PARTICIPATION, participation);
		values.put(DBPerformance.COLUMN_NAME_BEHAVIOR, behavior);

		// Check if there is an ID. If yes, we are updating. If not, we are inserting.
		if (id >= 0) {
			String whereClause = DBPerformance._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			db.update(DBPerformance.TABLE_NAME, values, whereClause, whereArgs);
		} else {
			db.insert(DBPerformance.TABLE_NAME, null, values);
		}

		//db.endTransaction();
	}

	// Update a accomplishment performance
	public void updateAccomplishmentPerformance(int id, float value) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();

		values.put(DBPerformance.COLUMN_NAME_ACCOMPLISHMENT, value);

		String whereClause = DBPerformance._ID + " = ?";
		String[] whereArgs = { String.valueOf(id) };
		db.update(DBPerformance.TABLE_NAME, values, whereClause, whereArgs);

		//db.endTransaction();
	}

	// Update a participation performance
	public void updateParticipationPerformance(int id, float value) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();

		values.put(DBPerformance.COLUMN_NAME_PARTICIPATION, value);

		String whereClause = DBPerformance._ID + " = ?";
		String[] whereArgs = { String.valueOf(id) };
		db.update(DBPerformance.TABLE_NAME, values, whereClause, whereArgs);

		//db.endTransaction();
	}

	// Update a behavior performance
	public void updateBehaviorPerformance(int id, float value) {
		SQLiteDatabase db = getWritableDatabase();

		// Create values object
		ContentValues values = new ContentValues();

		values.put(DBPerformance.COLUMN_NAME_BEHAVIOR, value);

		String whereClause = DBPerformance._ID + " = ?";
		String[] whereArgs = { String.valueOf(id) };
		db.update(DBPerformance.TABLE_NAME, values, whereClause, whereArgs);

		//db.endTransaction();
	}

	// Delete a performance row
	public void deletePerformance(int id) {
		SQLiteDatabase db = getWritableDatabase();

		// Define 'where' part of query.
		String selection = DBPerformance._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(id) };
		// Issue SQL statement.
		db.delete(DBPerformance.TABLE_NAME, selection, selectionArgs);

		//db.endTransaction();
	}

}
