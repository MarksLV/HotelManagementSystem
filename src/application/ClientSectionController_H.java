package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ClientSectionController_H {

	// This will open Room Booking screen of client
	public void openRoomBooking() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ClientSection_RB.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
	// This will open Help screen of client
	public void openHelp() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ClientSection_H.fxml"));
		Scene scene = new Scene(root);
		Main.globalStage.setScene(scene);
	}
	
    // This will go back to main menu
    public void logout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainClient.fxml"));
        Scene scene = new Scene(root);
        Main.globalStage.setScene(scene);
    }
}