package library.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.DatabaseHandler;
import library.model.SettingsModel;

public class SettingsLoader extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Thread(()->DatabaseHandler.getInstance()).start();
		
		
		
		Parent root=(Parent)FXMLLoader.load(getClass().getResource("settings.fxml"));
		Scene scene=new Scene(root);
	//	scene.getStylesheets().add(getClass().getResource("resources/common.css").toExternalForm());
		primaryStage.setTitle("Settings");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		
		SettingsModel.initConfig();
		
	}

}
