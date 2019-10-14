package cartridgeaccount.database;

import java.io.File;
import java.sql.SQLException;

import cartridgeaccount.utils.Log;

public abstract class SQLIteOpenHelper {

	private static final String TAG = SQLIteOpenHelper.class.getName();
	
    private SQLiteDatabase mDatabase;

    public abstract void onCreate(SQLiteDatabase db) throws SQLException;

    public SQLIteOpenHelper(String name) {
        boolean exists = new File(name).exists();

        mDatabase = new SQLiteDatabase(name);

        if (!exists)
			try {
				onCreate(mDatabase);
			} catch (SQLException e) {
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

}
