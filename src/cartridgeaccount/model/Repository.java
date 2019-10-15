package cartridgeaccount.model;

import java.util.Date;

import cartridgeaccount.database.DBHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Repository {

	private static Repository mInstance;
	
	private DBHelper mDB;
	private ObservableList<Cartridge> cartridges;
	
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
