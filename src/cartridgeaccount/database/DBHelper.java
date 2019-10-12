package cartridgeaccount.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.sqlite.SQLiteException;

import cartridgeaccount.model.Cartridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static cartridgeaccount.utils.Utils.log;

public class DBHelper {

	private Connection mConnection;
	
	public DBHelper() throws ClassNotFoundException, SQLException {
		mConnection = null;
		Class.forName("org.sqlite.JDBC");
		mConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
		
		log(getClass().getName() + ": Base connected");
	}

	
	public ObservableList<Cartridge> selectCartridges() {
        String sql =
                "SELECT c.*, p.title as title_p, s.title as title_s \n" +
                "FROM [cartridge] as c \n" +
                "LEFT JOIN [producer] as p ON c.[producer] = p.[_id]\n" +
                "LEFT JOIN states as s ON c.state = s.[_id_s]";

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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Произошла ошибка");
            alert.setHeaderText("Произошла ошибка связи с БД");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            log(getClass().getName() + ": " + e.getMessage());
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
            log(getClass().getName() + ": " + e.getMessage());
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Произошла ошибка");
            alert.setHeaderText("Произошла ошибка связи с БД");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            log(getClass().getName() + ": " + e.getMessage());
            return list;
        }
		
		return list;
	}
	
    public void insertCartridge(Cartridge cartridge) {
    	if (!checkRecord(cartridge)) {
    		
    		return;
    	}
    	
        String sql_insert = "INSERT INTO 'cartridge'(" +
                "uuid, producer, name, full_name, num, state, note) VALUES (?,%s,?,?,?,%s,?)";
        String sql_select_prod = "(SELECT _id FROM producer WHERE title LIKE '" + cartridge.getProducer() + "')";
        String sql_select_state = "(Select _id_s FROM states WHERE title LIKE '" + cartridge.getState() + "')";

        try {
            prepareStatement(
                    String.format(sql_insert, sql_select_prod, sql_select_state), cartridge
            ).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkRecord(Cartridge cartridge) {
		String sql = "SELECT num FRoM ? WHERE full_name LIKE ?";
		try {
			PreparedStatement statement = mConnection.prepareStatement(sql);
			ResultSet resSet = statement.executeQuery();
			resSet.getFetchSize();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

        	System.out.println(String.format(sql_update, sql_select_prod, sql_select_state));
        	
            statement.setString(1, cartridge.getUid().toString());
            statement.setString(2, cartridge.getName());
            statement.setString(3, cartridge.getFullName());
            statement.setString(4, cartridge.getNum());
            statement.setString(5, cartridge.getNote());
            statement.setString(6, cartridge.getUid().toString());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCartridge(Cartridge cartridge) {
        String sql_delete = "DELETE FROM cartridge WHERE uuid = '" + cartridge.getUid().toString() + "'";

        try {
            mConnection.prepareStatement(sql_delete).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
				log(getClass().getName() + ": Connection closed");
			}
			}
		    catch(SQLException e){
		    	System.err.println(e);
		    }
		
	}
	
}
