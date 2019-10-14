package cartridgeaccount.utils;

import java.util.Date;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

	public static void showErrorDlg(Exception e){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Произошла ошибка");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}
	
}
