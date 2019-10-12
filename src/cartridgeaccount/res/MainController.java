package cartridgeaccount.res;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import cartridgeaccount.Main;
import cartridgeaccount.model.Cartridge;
import cartridgeaccount.model.Repository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private MenuItem AddMenuItem;

    @FXML
    private MenuItem EditMenuItem;
    
    @FXML
    private MenuItem DeleteMenuItem;

    @FXML
    private TableView<Cartridge> mTable;
    
    @FXML
    private TableColumn<Cartridge, String> CNameCol;

    @FXML
    private TableColumn<Cartridge, String> CNumCol;

    @FXML
    private TableColumn<Cartridge, String> CStateCol;

    @FXML
    private TableColumn<Cartridge, String> CNoteCol;

    Main mainApp;
    
    private Cartridge currCartridge;
    
    @FXML
    void initialize() {
    	
        assert mTable != null : "fx:id=\"mTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert CNameCol != null : "fx:id=\"CNameCol\" was not injected: check your FXML file 'Main.fxml'.";
        assert CNumCol != null : "fx:id=\"CNumCol\" was not injected: check your FXML file 'Main.fxml'.";
        assert CStateCol != null : "fx:id=\"CStateCol\" was not injected: check your FXML file 'Main.fxml'.";
        assert CNoteCol != null : "fx:id=\"CNoteCol\" was not injected: check your FXML file 'Main.fxml'.";

        CNameCol.setCellValueFactory(cellData -> cellData.getValue().getFullNameProperty());
        CNumCol.setCellValueFactory(cellData -> cellData.getValue().getNumProperty());
        CStateCol.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
        CNoteCol.setCellValueFactory(cellData -> cellData.getValue().getNoteProperty());
        
        mTable.setRowFactory(tv -> {
        	TableRow<Cartridge> row = new TableRow<>();
        	row.setOnMouseClicked(event -> {
        		if (event.getClickCount() == 2 && (! row.isEmpty())) {
        			handleEditCartridge(null);
        		}
        	});
        	return row;
        });
    }
    
    @FXML
    private void handleEditCartridge(ActionEvent event) {
    	Cartridge selectedCartridge = mTable.getSelectionModel().getSelectedItem();
    	if (selectedCartridge != null) {
    		boolean okClicked = mainApp.showCartridgeEditDialog(selectedCartridge);
    		if (okClicked) {
    			Repository.getInstance().updateCartridge(selectedCartridge);
    		}
    	}
    }
    
    @FXML
    private void handleAddCartridge(ActionEvent event) {
    	Cartridge cartridge = new Cartridge(UUID.randomUUID(), "", "", "", "", "");
    	boolean isOkClicked = mainApp.showCartridgeEditDialog(cartridge);
    	if(isOkClicked) {
    		Repository.getInstance().addCartrige(cartridge);
    	}
    }
    
    @FXML
    void handleDeleteCartridge(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Удалить картридж?");
    	alert.setHeaderText("Вы действительно хотите удалить картридж?");
    	
    	if(alert.showAndWait().get() == ButtonType.OK) {
	    	Cartridge cartridge = mTable.getSelectionModel().getSelectedItem();
	    	if (cartridge != null) {
	    		Repository.getInstance().deleteCartridge(cartridge);
	    	}
    	}
    }
    
    @FXML
    void handleDuplicateCartridge(ActionEvent event) {
    	Cartridge selCartridge = mTable.getSelectionModel().getSelectedItem();
	    if (selCartridge != null) {
	    	Cartridge cartridge = new Cartridge(selCartridge);
	    	cartridge.setNum("");
	    	boolean isOkClicked = mainApp.showCartridgeEditDialog(cartridge);
	    	if(isOkClicked) {
	    		Repository.getInstance().addCartrige(cartridge);
	    	}
    	}
    }
    
    public void setMainApp(Main mainApp) {
    	this.mainApp = mainApp;
    	mTable.setItems(mainApp.getData());
    }
}
