package cartridgeaccount.model;

import java.sql.SQLException;

import cartridgeaccount.database.DBHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static cartridgeaccount.utils.Utils.log;

public class Repository {

	private static Repository mInstance;
	
	private DBHelper mDB;
	private ObservableList<Cartridge> cartridges;
	
	private Repository() {
		
		try {
			mDB = new DBHelper();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			log(getClass().getName() + ": " + e.getMessage());
		}
		cartridges = mDB.selectCartridges();
	}
	
	public static Repository getInstance() {
		if (mInstance == null) {
			mInstance = new Repository();
		}
		return mInstance;
	}
	
	public ObservableList<Cartridge> getCartridges() {
		return cartridges;
	}
	
	public ObservableList<String> getProducers() {
		return mDB.selectProducers();
	}
	
	public ObservableList<String> getStates(){
		return mDB.selectStates();
	}

	public void addCartrige(Cartridge cartridge) {
		cartridges.add(cartridge);
		mDB.insertCartridge(cartridge);
	}

	public void updateCartridge(Cartridge selectedCartridge) {
		mDB.updateCartridge(selectedCartridge);
	}

	public void closeConnection() {
		mDB.closeConncetion();
	}

	public void deleteCartridge(Cartridge cartridge) {
		cartridges.remove(cartridge);
		mDB.deleteCartridge(cartridge);
	}

}
