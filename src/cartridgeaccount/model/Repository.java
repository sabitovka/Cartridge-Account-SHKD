package cartridgeaccount.model;

import java.util.Date;

import cartridgeaccount.database.DBHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Repository {

	private static Repository mInstance;
	
	private DBHelper mDB;
	private ObservableList<Cartridge> cartridges;
	private ObservableList<Cartridge> filteredCartridges;
	
	private Repository() {
		mDB = new DBHelper();
		cartridges = mDB.selectCartridges();
	}
	
	public static Repository getInstance() {
		if (mInstance == null) {
			mInstance = new Repository();
		}
		return mInstance;
	}
	
	public ObservableList<Cartridge> querryCartridge(String column, String value) {
		String sql = "WHERE %s LIKE %s";
		
		if (column.equals("state")) {
			sql = String.format(sql, column, "(SELECT _id_s FROM states WHERE title LIKE '" + value + "')");
		} else 
			if (!value.isEmpty()) {
				sql = String.format(sql, column, "'%" + value + "%'");
			} else 
				sql = "";
		
		if (sql.isEmpty()) {
			return cartridges;
		}
		
		filteredCartridges = mDB.selectCartridges(sql);
		
		return filteredCartridges;
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
		if (mDB.insertCartridge(cartridge)) 
			cartridges.add(cartridge);
	}

	public void updateCartridge(Cartridge selectedCartridge) {
		mDB.updateCartridge(selectedCartridge);
	}

	public void closeConnection() {
		mDB.closeConncetion();
	}

	public void deleteCartridge(Cartridge cartridge) {
		if (mDB.deleteCartridge(cartridge))
			cartridges.remove(cartridge);
			
	}
	
	public boolean checkCartridge(String name, String num) {
		return mDB.checkCartridge(name, num);
	}

	public ObservableList<Refueling> getRefuelings() {
		return FXCollections.observableArrayList(
				new Refueling(new Date(), new Date())
		);
	}
}
