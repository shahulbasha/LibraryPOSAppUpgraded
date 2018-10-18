package library.ui.listmember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemberListLoader extends Application {

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("LibraryMemberList.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("memberList.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("MemberList");
		primaryStage.show();
		
	}

}
