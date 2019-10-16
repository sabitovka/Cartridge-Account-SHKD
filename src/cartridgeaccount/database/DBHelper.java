package cartridgeaccount.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

import cartridgeaccount.Main;
import cartridgeaccount.model.Cartridge;
import cartridgeaccount.utils.Log;
import cartridgeaccount.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static cartridgeaccount.utils.Utils.showErrorDlg;

public class DBHelper extends SQLIteOpenHelper {

	private static final String TAG = DBHelper.class.getName();
	
	private static final String DB_NAME = "database.db";
	
	private Connection mConnection;
	
	public DBHelper() {
		super(DB_NAME);
		mConnection = getDatabase().getConnection();
		Log.d(TAG, "DBHepler constructed");
	}

	@Override
	public void onCreate(SQLiteDatabase db) throws SQLException {
		FileReader reader;
		String[] filenames = {
			"create table cartridge.sql", "create table producer.sql", "create table refuelings.sql", "create table settings.sql", "create table states.sql" 
		};
		
		try {
			for (int i = 0; i < filenames.length; i++) {
				reader = new FileReader(new File(Main.class.getResource("..//assets/" + filenames[i]).toURI()));//"src/assets/" + filenames[i]);
				Scanner scan = new Scanner(reader);
				StringBuilder stringBuilder = new StringBuilder();
				while (scan.hasNext()) {
					stringBuilder.append(scan.nextLine());
				}
				db.getConnection().prepareStatement(stringBuilder.toString()).executeUpdate();
			}
		} catch (FileNotFoundException e) {
			Utils.showErrorDlg(e);
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Utils.showErrorDlg(e);
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
		}
	}
	
	public ObservableList<Cartridge> selectCartridges() {
		return selectCartridges("");
	}
	
	public ObservableList<Cartridge> selectCartridges(String whereClause) {
        String sql =
                "SELECT c.*, p.title as title_p, s.title as title_s \n" +
                "FROM [cartridge] as c \n" +
                "LEFT JOIN [producer] as p ON c.[producer] = p.[_id]\n" +
                "LEFT JOIN states as s ON c.state = s.[_id_s]\n" + 
                whereClause;

        Log.d(TAG, sql);
        
        
        ObservableList<Cartridge> cartridges = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = mConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String uuidString = resultSet.getString("uuid");
                String producer = resultSet.getString("title_p");
                String name = resultSet.getString("name");
                String num = resultSet.getString("num");
                String state = resultSet.getString("title_s");
                String note = resultSet.getString("note");

                cartridges.add(
                        new Cartridge(UUID.fromString(uuidString), producer, name, num, state, note)
                );
            }

        }catch (SQLException e) {
            e.printStackTrace();
            Utils.showErrorDlg(e);
            Log.d(TAG, e.getMessage());
            //log(getClass().getName() + ": " + e.getMessage());
            return cartridges;
        }

        return cartridges;
	}
	
	public ObservableList<String> selectStates() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		String sql = "SELECT title FROM STATES";
		
		try {
            PreparedStatement preparedStatement = mConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("title"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Произошла ошибка");
            alert.setHeaderText("Произошла ошибка связи с БД");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Log.d(TAG, getClass().getName() + ": " + e.getMessage());
            return list;
        }
		
		return list;
	}
	
	public ObservableList<String> selectProducers() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		String sql = "SELECT title FROM producer";
		
		try {
            PreparedStatement preparedStatement = mConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("title"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
            showErrorDlg(e);
            Log.d(TAG, getClass().getName() + ": " + e.getMessage());
            return list;
        }
		
		return list;
	}
	
    public boolean insertCartridge(Cartridge cartridge) {
        String sql_insert = "INSERT INTO 'cartridge'(" +
                "uuid, producer, name, full_name, num, state, note) VALUES (?,%s,?,?,?,%s,?)";
        String sql_select_prod = "(SELECT _id FROM producer WHERE title LIKE '" + cartridge.getProducer() + "')";
        String sql_select_state = "(Select _id_s FROM states WHERE title LIKE '" + cartridge.getState() + "')";

        try {
            prepareStatement(
                    String.format(sql_insert, sql_select_prod, sql_select_state), cartridge
            ).executeUpdate();
            return true;
        } catch (SQLException e) {
        	showErrorDlg(e);
        	Log.d(TAG, getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //возравщает true если есть какой-то элемент    
    public boolean checkCartridge(String name, String num) {
		String sql = "SELECT num FROM cartridge WHERE full_name LIKE ? AND num LIKE ?";
		try {
			PreparedStatement statement = mConnection.prepareStatement(sql);
			statement.setString(1, name); 
			statement.setString(2, num);
			return statement.executeQuery().next();
		} catch (SQLException e) {
			showErrorDlg(e);
        	Log.d(TAG, getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}


	public void updateCartridge(Cartridge cartridge) {
        String sql_update = "UPDATE cartridge SET " +
                "uuid = ?, producer = %s, name = ?, full_name = ?, num = ?, state = %s, note = ? " +
                "WHERE uuid = ?";
        String sql_select_prod = "(SELECT _id FROM producer WHERE title LIKE '" + cartridge.getProducer() + "')";
        String sql_select_state = "(Select _id_s FROM states WHERE title LIKE '" + cartridge.getState() + "')";
        
        try {
        	PreparedStatement statement = mConnection.prepareStatement(String.format(sql_update, sql_select_prod, sql_select_state));
        	
            statement.setString(1, cartridge.getUid().toString());
            statement.setString(2, cartridge.getName());
            statement.setString(3, cartridge.getFullName());
            statement.setString(4, cartridge.getNum());
            statement.setString(5, cartridge.getNote());
            statement.setString(6, cartridge.getUid().toString());
            
            statement.executeUpdate();
        } catch (SQLException e) {
        	showErrorDlg(e);
        	Log.d(TAG, getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteCartridge(Cartridge cartridge) {
        String sql_delete = "DELETE FROM cartridge WHERE uuid = '" + cartridge.getUid().toString() + "'";

        try {
            mConnection.prepareStatement(sql_delete).executeUpdate();
            return true;
        } catch (SQLException e) {
        	showErrorDlg(e);
        	Log.d(TAG, getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement prepareStatement(String query, Cartridge cartridge) throws SQLException {
        PreparedStatement statement = mConnection.prepareStatement(query);

        statement.setString(1, cartridge.getUid().toString());
        statement.setString(2, cartridge.getName());
        statement.setString(3, cartridge.getFullName());
        statement.setString(4, cartridge.getNum());
        statement.setString(5, cartridge.getNote());

        return statement;
    }
	

	public void closeConncetion() {
		try {
			if(mConnection != null) {
				mConnection.close();
				Log.d(TAG, getClass().getName() + ": Connection closed");
			}
			}
		    catch(SQLException e){
	        	Log.d(TAG, getClass().getName() + ": " + e.getMessage());
		    }
		
	}
	
}
