package library.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.DatabaseHandler;
import library.util.LibraryUtil;

public class MainApplication extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Thread(()->DatabaseHandler.getInstance()).start();
		
		Parent root=(Parent)FXMLLoader.load(getClass().getResource("/library/ui/login/loginController.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		primaryStage.setTitle("Library Checkout Application");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		LibraryUtil.setStageIcon(primaryStage);
		
		
		
	}

}
