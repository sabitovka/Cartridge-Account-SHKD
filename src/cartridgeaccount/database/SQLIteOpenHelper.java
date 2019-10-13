package com.karimsabitov.mysqliteopenhelper;

import java.io.File;

public abstract class SQLIteOpenHelper {

    private SQLiteDatabase mDatabase;

    public abstract void onCreate(SQLiteDatabase db);

    public SQLIteOpenHelper(String name) {
        boolean exists = new File(name).exists();

        mDatabase = new SQLiteDatabase(name);

        if (!exists) onCreate(mDatabase);
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

}
