package library.ui.addmember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddMemberMain extends Application {

	public static void main(String[] args) {
			launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("LibraryAddMember.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("addMember.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
