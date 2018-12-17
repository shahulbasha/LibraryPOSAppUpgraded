package library.ui.main.toolbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import library.util.LibraryUtil;

public class ToolbarController {
	
	@FXML
    void loadAddBook(ActionEvent event) {

		LibraryUtil.loadWindow(getClass().getResource("/library/ui/addbook/LibraryAddBook.fxml"), "Add New Book",null);
    }

    @FXML
    void loadAddMember(ActionEvent event) {
		LibraryUtil.loadWindow(getClass().getResource("/library/ui/addmember/LibraryAddMember.fxml"), "Add New Member",null);
    }

    @FXML
    void loadBookList(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/listbook/LibraryBookList.fxml"), "View Book List",null);
    }

    @FXML
    void loadMemberList(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/listmember/LibraryMemberList.fxml"),"View Member List",null);
    }

    @FXML
    void loadSettings(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/settings/settings.fxml"),"Settings",null);
    }
}
