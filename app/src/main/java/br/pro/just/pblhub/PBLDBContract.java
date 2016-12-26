package br.pro.just.pblhub;

import android.provider.BaseColumns;

public final class PBLDBContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PBLDBContract() {}

    // Table: classes
    public static abstract class DBClasses implements BaseColumns {
        public static final String TABLE_NAME = "classes";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
    }

    // Table: groups
    public static abstract class DBGroups implements BaseColumns {
        public static final String TABLE_NAME = "groups";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_CLASS_ID = "id_class";
        public static final String COLUMN_NAME_NAME = "name";
    }
    
    // Table: problems
    public static abstract class DBProblems implements BaseColumns {
        public static final String TABLE_NAME = "problems";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_GROUP_ID = "id_group";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    // Table: sessions
    public static abstract class DBSessions implements BaseColumns {
        public static final String TABLE_NAME						= "sessions";
        public static final String _ID								= "_id";
        public static final String COLUMN_NAME_PROBLEM_ID			= "id_problem";
        public static final String COLUMN_NAME_DATE					= "date";
        public static final String COLUMN_NAME_GOALS				= "goals";
        public static final String COLUMN_NAME_COORDINATOR_ID		= "id_coordinator";
        public static final String COLUMN_NAME_BOARD_ID				= "id_board";
        public static final String COLUMN_NAME_RECORDER_ID			= "id_recorder";
        public static final String COLUMN_NAME_GRADE_COORDINATOR	= "grade_coordinator";
        public static final String COLUMN_NAME_GRADE_BOARD			= "grade_board";
        public static final String COLUMN_NAME_GRADE_RECORDER		= "grade_recorder";
        public static final String COLUMN_NAME_NO_ACCOMPLISHMENT	= "no_accomplishment";
    }

    // Table: students
    public static abstract class DBStudents implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_GROUP_ID = "id_group";
        public static final String COLUMN_NAME_ID_NUMBER = "id_number";
        public static final String COLUMN_NAME_NAME = "name";
    }

    // Table: performance
    public static abstract class DBPerformance implements BaseColumns {
        public static final String TABLE_NAME = "performance";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_SESSION_ID = "id_session";
        public static final String COLUMN_NAME_STUDENT_ID = "id_student";
        public static final String COLUMN_NAME_NOT_PRESENT = "not_present";
        public static final String COLUMN_NAME_ACCOMPLISHMENT = "accomplishment";
        public static final String COLUMN_NAME_PARTICIPATION = "participation";
        public static final String COLUMN_NAME_BEHAVIOR = "behavior";
        // Not used since DB version 2: public static final String COLUMN_NAME_ROLE = "role";
    }

}
