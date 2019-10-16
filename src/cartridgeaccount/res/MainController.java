package cartridgeaccount.res;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import cartridgeaccount.Main;
import cartridgeaccount.model.Cartridge;
import cartridgeaccount.model.Repository;
import cartridgeaccount.res.CartridgeEditDialogController.ActionMode;
import cartridgeaccount.utils.Log;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    private static final String TAG = MainController.class.getName();
    
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
    @FXML
    private ComboBox<String> SearchCB;
    @FXML
    private ComboBox<String> SearchStateCB;
    @FXML
    private TextField SearchTF;
    @FXML
    private Button SearchBtn;

    Main mainApp;
    
    @FXML
    void initialize() {
    	
        CNameCol.setCellValueFactory(cellData -> cellData.getValue().getFullNameProperty());
        CNumCol.setCellValueFactory(cellData -> cellData.getValue().getNumProperty());
        CStateCol.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
        CNoteCol.setCellValueFactory(cellData -> cellData.getValue().getNoteProperty());
        
        mTable.setRowFactory(tv -> {
        	TableRow<Cartridge> row = new TableRow<>();
        	row.setOnMouseClicked(event -> {
        		if (event.getClickCount() == 2 && (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY)) {
        			handleEditCartridge(null);
        		}
        	});
        	return row;
        });
        
        SearchStateCB.setItems(Repository.getInstance().getStates());
        SearchStateCB.setValue("Заправлен в ЗиПе");
        SearchCB.setItems(FXCollections.observableArrayList("Названию", "Номеру", "Состоянию"));
        SearchCB.setValue("Названию");
    }
    
    @FXML
    private void handleEditCartridge(ActionEvent event) {
    	Cartridge selectedCartridge = mTable.getSelectionModel().getSelectedItem();
    	if (selectedCartridge != null) {
    		boolean okClicked = mainApp.showCartridgeEditDialog(selectedCartridge, CartridgeEditDialogController.ActionMode.EDIT);
    		if (okClicked) {
    			Repository.getInstance().updateCartridge(selectedCartridge);
    		}
    	}
    }
    
    @FXML
    private void handleAddCartridge(ActionEvent event) {
    	Cartridge cartridge = new Cartridge(UUID.randomUUID(), "", "", "", "", "");
    	boolean isOkClicked = mainApp.showCartridgeEditDialog(cartridge, CartridgeEditDialogController.ActionMode.ADD);
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
	    	boolean isOkClicked = mainApp.showCartridgeEditDialog(cartridge, ActionMode.ADD);
	    	if(isOkClicked) {
	    		Repository.getInstance().addCartrige(cartridge);
	    	}
    	}
    }
    
    @FXML
    void handleSearchCartridge(ActionEvent event) {
    	String column = "", value = "";
    	if (SearchCB.getValue().equals("Состоянию")) {
    		column = "state"; 
    		value = SearchStateCB.getValue();
    	} else {
    		if (SearchCB.getValue().equals("Названию")) 
    			column = "full_name";
    		else 
    			column = "num";
    		value = SearchTF.getText();
    	}
    	mTable.setItems(Repository.getInstance().querryCartridge(column, value));
    }
    
    @FXML
    void handleSwapCartridge(ActionEvent event) {
    	if (SearchCB.getValue().equals("Состоянию")) {
    		SearchStateCB.setVisible(true);
    		SearchTF.setVisible(false);
    	} else {
    		SearchStateCB.setVisible(false);
    		SearchTF.setVisible(true);
    	}
    }
    
    @FXML
    void handleRemoveQuerry(ActionEvent event) {
    	SearchTF.setText("");
    	SearchCB.setValue("Названию");
    	handleSearchCartridge(event);
    }
    
    public void setMainApp(Main mainApp) {
    	this.mainApp = mainApp;
    	mTable.setItems(mainApp.getData());
    }
}
