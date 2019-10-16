package cartridgeaccount;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import cartridgeaccount.model.Cartridge;
import cartridgeaccount.model.Repository;
import cartridgeaccount.res.CartridgeEditDialogController;
import cartridgeaccount.res.MainController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private Stage mPrimaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mPrimaryStage = primaryStage;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("res/Main.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root,640,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Учет картриджей ШКД");
			
			MainController controller = loader.getController();
			controller.setMainApp(this);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public ObservableList<Cartridge> getData(){
		return Repository.getInstance().getCartridges();
	}

	public boolean showCartridgeEditDialog(Cartridge cartridge, CartridgeEditDialogController.ActionMode actionMode) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("res/CartridgeEditDialog.fxml"));
			TabPane anchorPane = (TabPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Изменить картридж");
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(mPrimaryStage);
			Scene scene = new Scene(anchorPane);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UTILITY);
			
			CartridgeEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCartridge(cartridge);
			controller.setActionMode(actionMode);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String showSearchDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("res/SearchDialog.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Поиск картриджа");
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(mPrimaryStage);
			Scene scene = new Scene(anchorPane);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UTILITY);
			
			//SearchDialogController controller = loader.getController();
			//controller.setDialogStage(dialogStage);
			
			dialogStage.showAndWait();
			
			return "";
		} catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		System.setOut(new PrintStream(new File("log.txt")));
		launch(args);
		Repository.getInstance().closeConnection();
	}
}
