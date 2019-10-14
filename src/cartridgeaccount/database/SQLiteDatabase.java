package cartridgeaccount.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cartridgeaccount.utils.Log;

public class SQLiteDatabase {

    private final String TAG = getClass().getName();

    private Connection mConnection;

    public SQLiteDatabase(String name) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:" + name;

            mConnection = DriverManager.getConnection(url);

            Log.d(TAG, "База подключена, " + url);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public Connection getConnection() {
        return mConnection;
    }

}
