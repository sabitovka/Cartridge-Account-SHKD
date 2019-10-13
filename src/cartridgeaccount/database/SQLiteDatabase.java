package com.karimsabitov.mysqliteopenhelper;

import com.karimsabitov.mysqliteopenhelper.utils.Log;

import java.sql.*;

public class SQLiteDatabase {

    private final String TAG = getClass().getSimpleName();

    private Connection mConnection;

    public SQLiteDatabase(String name) {
        try {
            Class.forName("org:sqlite:JDBC");

            String url = "jdbc:sqlite:" + name;

            mConnection = DriverManager.getConnection(url);

            Log.d(TAG, "База подключена");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public Connection getConnection() {
        return mConnection;
    }

}
