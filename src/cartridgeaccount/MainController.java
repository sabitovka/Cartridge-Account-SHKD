package cartridgeaccount;

import java.net.URL;
import java.util.ResourceBundle;

import cartridgeaccount.model.Cartridge;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
        
        mTable.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> currCartridge = newValue);
    }
    
    Main mainApp;
    public void setMainApp(Main mainApp) {
    	this.mainApp = mainApp;
    	mTable.setItems(mainApp.getData());
    }
    
    public void Table_OnMouseCLicked() {
    	mainApp.showCartridgeEditDialog(currCartridge);
    }
}
