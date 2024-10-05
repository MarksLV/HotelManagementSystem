package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminSectionController_A {
	
    @FXML
    private TableColumn<Admins, String> colPasswordAdmin;
    @FXML
    private TableColumn<Admins, String> colUsernameAdmin;
    @FXML
    private TableView<Admins> tblAdmins;
    @FXML
    private TextField txtAdminPassword;
    @FXML
    private TextField txtAdminUsername;
    @FXML
    private Label lblid;
    @FXML
    private Label lblInfo;
	
    // At start - run this code
	public void initialize() throws SQLException {
		// Fill up all TableColumn in TableView
		colUsernameAdmin.setCellValueFactory(new PropertyValueFactory<Admins, String>("username"));
		colPasswordAdmin.setCellValueFactory(new PropertyValueFactory<Admins, String>("password"));
    	populate();
    }
    
    public void populate() throws SQLException {
    	// Establishing connection between database and getting ready using administrator class
        ObservableList<Admins> listofadmins = FXCollections.observableArrayList();    	
    	DBConnection object = new DBConnection();
    	Connection objectConnection = object.connect();
    	
    	// Selecting all information known from database rooms
    	Statement st = objectConnection.createStatement();
    	ResultSet rs = st.executeQuery("select * from adminregister");
        while(rs.next()) {
        	listofadmins.add(new Admins(rs.getInt("idAdmin"), rs.getString("Username"), rs.getString("Password")));
     }
        // Add items to the table    	    	
    	tblAdmins.setItems(listofadmins);    	
    }
    
    public void Add() throws SQLException {
        try {
            // Check for empty input fields
            if (txtAdminUsername.getText().isEmpty() || txtAdminPassword.getText().isEmpty()) {
                lblInfo.setText("Please fill in all fields before adding an admin.");
                return;
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Setting up query to be executed and info added inside table
            PreparedStatement addquery = objectConnection.prepareStatement("insert into adminregister "
                    + "(Username, Password) "
                    + "values (?, ?)");
            addquery.setString(1, txtAdminUsername.getText());
            addquery.setString(2, txtAdminPassword.getText());

            addquery.execute(); // Execute add query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Admin added successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while adding admin!");
        }
    }


    public void Delete() {
        try {
            if (!isAdminSelected()) {
                return; // Exit the method if no administrator is selected
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Get the selected item ID
            int id = tblAdmins.getSelectionModel().getSelectedItem().getId();
            PreparedStatement st = objectConnection.prepareStatement("delete from adminregister where idAdmin = ?");
            st.setInt(1, id);

            st.executeUpdate(); // Execute update query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Admin deleted successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while deleting admin!");
        }
    }
    
    public void Update() {
        try {
            if (!isAdminSelected()) {
                return; // Exit the method if no administrator is selected
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Prepare the update query
            PreparedStatement st = objectConnection.prepareStatement(
                    "UPDATE adminregister SET Username=?, Password=? WHERE idAdmin=?");

            // Set values for the parameters in the prepared statement
            st.setString(1, txtAdminUsername.getText());
            st.setString(2, txtAdminPassword.getText());
            st.setInt(3, Integer.parseInt(lblid.getText()));

            st.executeUpdate(); // Execute update query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Admin updated successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while updating admin!");
        }
    }
    
    // Clean the text
    public void Clean() {
    	txtAdminUsername.setText(null);
    	txtAdminPassword.setText(null);
    }
    
    // Updating text field
    public void UpdateTextFields() {
    	Admins emp = tblAdmins.getSelectionModel().getSelectedItem();
    	txtAdminUsername.setText(emp.getUsername());
    	txtAdminPassword.setText(emp.getPassword());
    	lblid.setText(String.valueOf(emp.getId()));
    }
    
    private boolean isAdminSelected() {
        if (tblAdmins.getSelectionModel().getSelectedItem() == null) {
            lblInfo.setText("Please select an admin from the table.");
            return false;
        }
        return true;
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
