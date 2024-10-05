package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

public class Controller {

    @FXML
    private ComboBox<String> modmenuSelect;
	
    // At start - run this code
    public void initialize() {
    	// Add selection menu in ComboBox
        ObservableList<String> themenu = FXCollections.observableArrayList("Client", "Admin");
        modmenuSelect.setItems(themenu);
    }
	
    // ComboBox selection between admin-client
	public void menuSelection() {
		
	    String selectedMenu = modmenuSelect.getValue();

	    try {
	        switch (selectedMenu) {
	            case "Client":				// If array is Client, change scene to Client Menu
	                openClientMenu();
	                break;
	            case "Admin":				// If array is Administrator, change scene to Administrator Menu
	                openAdminMenu();		
	                break;
	            default:
	                break;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// This will change from administrator to clients menu
	public void openClientMenu() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MainClient.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// This will change from client to administrator menu
	public void openAdminMenu() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MainAdmin.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// This will open login screen of client
	public void openClientLogin() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LoginClient.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// This will open register screen of client
	public void openClientRegister() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("RegisterClient.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// This will open login screen of administrator
	public void openAdminLogin() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LoginAdmin.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// Exit
	@FXML
    private void handleExit() {
        Platform.exit();
    }
}
