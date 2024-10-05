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

public class AdminSectionController_C {

    @FXML
    private TableColumn<Clients, String> colEmailClient;
    @FXML
    private TableColumn<Clients, String> colNameClient;
    @FXML
    private TableColumn<Clients, String> colPasswordClient;
    @FXML
    private TableColumn<Clients, Integer> colPhoneClient;
    @FXML
    private TableColumn<Clients, String> colSurnameClient;
    @FXML
    private TableView<Clients> tblClients;
    @FXML
    private TextField txtClientEmail;
    @FXML
    private TextField txtClientName;
    @FXML
    private TextField txtClientPassword;
    @FXML
    private TextField txtClientPhone;
    @FXML
    private TextField txtClientSurname;
    @FXML
    private Label lblid;
    @FXML
    private Label lblInfo;
	
    // At start - run this code
	public void initialize() throws SQLException {
		// Fill up all TableColumn in TableView
    	colNameClient.setCellValueFactory(new PropertyValueFactory<Clients, String>("name"));
    	colSurnameClient.setCellValueFactory(new PropertyValueFactory<Clients, String>("surname"));
    	colEmailClient.setCellValueFactory(new PropertyValueFactory<Clients, String>("email"));
    	colPasswordClient.setCellValueFactory(new PropertyValueFactory<Clients, String>("password"));
    	colPhoneClient.setCellValueFactory(new PropertyValueFactory<Clients, Integer>("phone"));
    	populate();
    }
    
    public void populate() throws SQLException {
    	// Establishing connection between database and getting ready using class Clients
        ObservableList<Clients> listofclients = FXCollections.observableArrayList();    	
    	DBConnection object = new DBConnection();
    	Connection objectConnection = object.connect();
    	
    	// Selecting all information known from database register
    	Statement st = objectConnection.createStatement();
    	ResultSet rs = st.executeQuery("select * from register");
        while(rs.next()) {
        	listofclients.add(new Clients(rs.getInt("idRegister"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Password"), rs.getString("Email"), rs.getInt("Phone")));
     }
        // Add items to the table  
    	tblClients.setItems(listofclients);    	
    }
    
    public void Add() throws SQLException {
        try {
            // Check for empty input fields
            if (txtClientName.getText().isEmpty() || txtClientSurname.getText().isEmpty() || txtClientPassword.getText().isEmpty() || txtClientEmail.getText().isEmpty() || txtClientPhone.getText().isEmpty()) {
                lblInfo.setText("Please fill in all fields before adding a client.");
                return;
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Setting up query to be executed and info added inside table
            PreparedStatement addquery = objectConnection.prepareStatement("insert into register "
                    + "(FirstName, LastName, Password, Email, Phone) " + "values (?, ?, ?, ?, ?)");
            addquery.setString(1, txtClientName.getText());
            addquery.setString(2, txtClientSurname.getText());
            addquery.setString(3, txtClientPassword.getText());
            addquery.setString(4, txtClientEmail.getText());
            addquery.setString(5, txtClientPhone.getText());

            addquery.execute(); // Execute add query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Client added successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while adding client!");
        }
    }

    public void Delete() {
        try {
            if (!isClientSelected()) {
                return; // Exit the method if no client is selected
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Get the selected item ID
            int id = tblClients.getSelectionModel().getSelectedItem().getId();
            PreparedStatement st = objectConnection.prepareStatement("delete from register where idRegister = ?");
            st.setInt(1, id);

            st.executeUpdate(); // Execute update query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Client deleted successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while deleting client!");
        }
    }


    public void Update() {
        try {
            if (!isClientSelected()) {
                return; // Exit the method if no client is selected
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Prepare the update query
            PreparedStatement st = objectConnection.prepareStatement(
                    "UPDATE register SET FirstName=?, LastName=?, Password=?, Email=?, Phone=? WHERE idRegister=?");

            // Set values for the parameters in the prepared statement
            st.setString(1, txtClientName.getText());
            st.setString(2, txtClientSurname.getText());
            st.setString(3, txtClientPassword.getText());
            st.setString(4, txtClientEmail.getText());
            st.setString(5, txtClientPhone.getText());
            st.setInt(6, Integer.parseInt(lblid.getText()));

            st.executeUpdate(); // Execute update query
            populate(); 		// Refresh table view with updated data
            Clean(); 			// Clear the input fields
            lblInfo.setText("Client updated successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while updating client!");
        }
    }

    private boolean isClientSelected() {
        if (tblClients.getSelectionModel().getSelectedItem() == null) {
            lblInfo.setText("Please select a client from the table.");
            return false;
        }
        return true;
    }
    
    // Clean the text
    public void Clean() {
    	txtClientName.setText(null);
    	txtClientSurname.setText(null);
    	txtClientPassword.setText(null);
    	txtClientEmail.setText(null);
    	txtClientPhone.setText(null);
    }
    
    // Updating text field
    public void UpdateTextFields() {
    	Clients emp = tblClients.getSelectionModel().getSelectedItem();
    	txtClientName.setText(emp.getName());
    	txtClientSurname.setText(emp.getSurname());
    	txtClientPassword.setText(String.valueOf(emp.getPassword()));
    	txtClientEmail.setText(String.valueOf(emp.getEmail()));
    	txtClientPhone.setText(String.valueOf(emp.getPhone()));    	
    	lblid.setText(String.valueOf(emp.getId()));
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
