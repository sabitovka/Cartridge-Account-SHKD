package cartridgeaccount.res;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.sun.swing.internal.plaf.metal.resources.metal_zh_TW;

import cartridgeaccount.Main;
import cartridgeaccount.model.Cartridge;
import cartridgeaccount.model.Refueling;
import cartridgeaccount.model.Repository;
import cartridgeaccount.ui.DatePickerCell;
import cartridgeaccount.utils.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CartridgeEditDialogController {

	public enum ActionMode { ADD, EDIT}
	
	private static final String TAG = CartridgeEditDialogController.class.getName(); 
	
	@FXML
	private TableColumn<Refueling, Date> startDateColumn;

	@FXML
	private TableColumn<Refueling, Date> endDateColumn;
	
	@FXML
    private TableView<Refueling> refTable;
	
	@FXML
    private Tab tabRefuelings;

    @FXML
    private Tab tabSettings;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Button_Cancel;

    @FXML
    private Button Button_OK;

    @FXML
    private ComboBox<String> ProdCB;

    @FXML
    private TextField NameTF;

    @FXML
    private TextField FullNameTF;

    @FXML
    private TextField NumTF;

    @FXML
    private ComboBox<String> StateCB;

    @FXML
    private TextArea NoteTF;
    
    private Stage stage;
    private boolean okClicked;
    private Main mainApp;
    private ActionMode actionMode;
    
    private Cartridge mCartridge;
    
    @FXML
    void initialize() {
        assert Button_Cancel != null : "fx:id=\"Button_Cancel\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert Button_OK != null : "fx:id=\"Button_OK\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert ProdCB != null : "fx:id=\"ProdCB\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert NameTF != null : "fx:id=\"NameTF\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert FullNameTF != null : "fx:id=\"FullNameTF\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert NumTF != null : "fx:id=\"NumTF\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert StateCB != null : "fx:id=\"StateCB\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";
        assert NoteTF != null : "fx:id=\"NoteTF\" was not injected: check your FXML file 'CartridgeEditDialog.fxml'.";

        //StateCB.setOnAction(event -> mCartridge.setState(StateCB.getValue()));
        
    }
    
    public void setCartridge(Cartridge cartridge) {
    	this.mCartridge = cartridge;
    	
    	FullNameTF.setText(cartridge.getFullName());
    	NumTF.setText(cartridge.getNum());
    	NameTF.setText(cartridge.getName());
    	NoteTF.setText(cartridge.getNote());
    	
    	StateCB.setItems(Repository.getInstance().getStates());
    	StateCB.setValue(cartridge.getState());
    	    	
    	ProdCB.setItems(Repository.getInstance().getProducers());
    	ProdCB.setValue(cartridge.getProducer());
    	
    }
    
    @FXML
    private void handleCancel() {
    	stage.close();
    }
    
    @FXML
    private void handleOk() {
    	if (validInput()) {
    		if (actionMode == ActionMode.ADD) {
    			if (Repository.getInstance().checkCartridge(FullNameTF.getText(), NumTF.getText())) {
    		    	Alert alert = new Alert(AlertType.ERROR);
    		    	alert.setTitle("Ошибка добавления");
    		    	alert.setHeaderText("Данный картридж уже существует");
    		    	alert.showAndWait();		
    		    	return;
    			}
    		}
	    	mCartridge.setState(StateCB.getValue());
	    	mCartridge.setProducer(ProdCB.getValue());
	    	mCartridge.setName(NameTF.getText());
	    		
	    	mCartridge.setNote(NoteTF.getText());
	    	mCartridge.setNum(NumTF.getText());
	    		
	    	okClicked = true;
	    	stage.close();
    	}
    }

    private boolean validInput() {
		String errorMessage = "";
		if (ProdCB.getValue().isEmpty()) {
			errorMessage += "Вы не выбрали производителя\n";
		}
		if (NameTF.getText().isEmpty()) {
			errorMessage += "Вы не ввели название\n";
		}
		if (NumTF.getText().isEmpty()) {
			errorMessage += "Вы не ввели номер\n";
		}
		if (errorMessage.isEmpty())
			return true;
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Произошла ошибка");
			alert.setHeaderText(errorMessage);
			
			alert.showAndWait();
			return false;
		}
	}

	@FXML
    void handleTypeCB(ActionEvent event) {
    	setFullNameTF();
    }
    
    @FXML
    void kayPressed(KeyEvent event) {
    	setFullNameTF();
    }
    
    public void setDialogStage(Stage stage) {
    	this.stage = stage;
    }
    
    public boolean isOkClicked() {
    	return okClicked;
    }
    
    private void setFullNameTF() {
    	FullNameTF.setText(
    			ProdCB.getValue() + " " + NameTF.getText());
    }

	public void setActionMode(ActionMode actionMode) {
		this.actionMode = actionMode;
		if (actionMode == ActionMode.ADD) {
			tabRefuelings.setDisable(true);
			tabSettings.setDisable(true);
		}
		if (actionMode == ActionMode.EDIT) {
			tabRefuelings.setDisable(false);
			tabSettings.setDisable(false);
		}
		
	}
}
