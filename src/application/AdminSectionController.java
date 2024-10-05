package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;

public class AdminSectionController {

    @FXML
    private Text adminNameLogin;

    // At start - run this code
    public void initialize(String Username) {
    	// Display administrators user name on welcome sign
        adminNameLogin.setText("Welcome, " + Username + "!");
    }
    
    // This will open Clients screen of administration
    public void openClients() throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("AdminSection_C.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}

    // This will open Administrators screen of administration
    public void openAdmins() throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("AdminSection_A.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}

    // This will open Rooms screen of administration
    public void openRooms() throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("AdminSection_R.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
    
    // This will go back to main menu
    public void logout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainAdmin.fxml"));
        Scene scene = new Scene(root);
        Main.globalStage.setScene(scene);
    }
}
