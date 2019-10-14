package cartridgeaccount;
	
import java.io.IOException;

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
			primaryStage.setTitle("���� ���������� ���");
			
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

	public boolean showCartridgeEditDialog(Cartridge cartridge) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("res/CartridgeEditDialog.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("�������� ��������");
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.initOwner(mPrimaryStage);
			Scene scene = new Scene(anchorPane);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			dialogStage.initStyle(StageStyle.UTILITY);
			
			CartridgeEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCartridge(cartridge);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked() && controller.isEdited();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		Repository.getInstance().closeConnection();
	}
}
