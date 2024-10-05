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

public class AdminSectionController_R {

    @FXML
    private TableColumn<Rooms, Double> colRoomCost;
    @FXML
    private TableColumn<Rooms, Integer> colRoomNumber;
    @FXML
    private TableColumn<Rooms, Boolean> colRoomStatus;
    @FXML
    private TableView<Rooms> tblRooms;
    @FXML
    private TextField txtRoomCost;
    @FXML
    private TextField txtRoomNumber;
    @FXML
    private TextField txtRoomStatus;
    @FXML
    private Label lblid;
    @FXML
    private Label lblInfo;
    
    // At start - run this code
	public void initialize() throws SQLException {
		// Fill up all TableColumn in TableView
		colRoomCost.setCellValueFactory(new PropertyValueFactory<Rooms, Double>("roomCost"));
		colRoomNumber.setCellValueFactory(new PropertyValueFactory<Rooms, Integer>("roomNumber"));
		colRoomStatus.setCellValueFactory(new PropertyValueFactory<Rooms, Boolean>("roomAvailability"));
    	populate();
    }
    
    public void populate() throws SQLException {
    	// Establishing connection between database and getting ready using class Rooms
        ObservableList<Rooms> listofrooms = FXCollections.observableArrayList();    	
    	DBConnection object = new DBConnection();
    	Connection objectConnection = object.connect();
    	
    	// Selecting all information known from database rooms
    	Statement st = objectConnection.createStatement();
    	ResultSet rs = st.executeQuery("select * from rooms");
        while(rs.next()) {
        	listofrooms.add(new Rooms(rs.getInt("idRooms"), rs.getInt("RoomNumb"), rs.getDouble("RoomCost"), rs.getBoolean("RoomAvailability")));
     }
        // Add items to the table    	
        tblRooms.setItems(listofrooms);    	
    }
	
    public void Add() throws SQLException {
        try {
            // Check for empty input fields
            if (txtRoomNumber.getText().isEmpty() || txtRoomCost.getText().isEmpty() || txtRoomStatus.getText().isEmpty()) {
                lblInfo.setText("Please fill in all fields before adding a room.");
                return;
            }

            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();

            // Setting up query to be executed and info added inside table
            PreparedStatement addquery = objectConnection.prepareStatement("insert into rooms "
                    + "(RoomNumb, RoomCost, RoomAvailability) "
                    + "values (?, ?, ?)");
            addquery.setString(1, txtRoomNumber.getText());
            addquery.setString(2, txtRoomCost.getText());
            addquery.setString(3, txtRoomStatus.getText());

            addquery.execute(); // Execute add query
            populate();         // Refresh table view with updated data
            Clean();            // Clear the input fields
            lblInfo.setText("Room added successfully.");
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while adding room!");
        }
    }

    public void Delete() {
        try {
            if (!isRoomSelected()) {
                return;  // Exit the method if no room is selected
            }
            
            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();
            
            // Get the selected item ID
            int id = tblRooms.getSelectionModel().getSelectedItem().getId();
            PreparedStatement st = objectConnection.prepareStatement("delete from rooms where idRooms = ?");
            st.setInt(1, id);
            
            st.executeUpdate(); // Execute update query
            populate();         // Refresh table view with updated data
            Clean();            // Clear the input fields
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while deleting room!");
        }
    }

    public void Update() {
        try {
            if (!isRoomSelected()) {
                return;  // Exit the method if no room is selected
            }
            
            // Connect to the database
            DBConnection object = new DBConnection();
            Connection objectConnection = object.connect();
            
            // Prepare the update query
            PreparedStatement st = objectConnection.prepareStatement(
                    "UPDATE rooms SET RoomNumb=?, RoomCost=?, RoomAvailability=? WHERE idRooms=?");

            // Set values for the parameters in the prepared statement
            st.setString(1, txtRoomNumber.getText());
            st.setString(2, txtRoomCost.getText());
            st.setString(3, txtRoomStatus.getText());
            st.setInt(4, Integer.parseInt(lblid.getText()));

            st.executeUpdate(); // Execute update query
            populate();         // Refresh table view with updated data
            Clean();            // Clear the input fields
        } catch (SQLException e) {
            lblInfo.setText("Error occurred while updating room!");
        }
    }

    // Clean the text
    public void Clean() {
    	txtRoomNumber.setText(null);
    	txtRoomCost.setText(null);
    	txtRoomStatus.setText(null);
    }
    
    // Updating text field
    public void UpdateTextFields() {
        Rooms emp = tblRooms.getSelectionModel().getSelectedItem();
        txtRoomNumber.setText(String.valueOf(emp.getRoomNumber()));
        txtRoomCost.setText(String.valueOf(emp.getRoomCost())); 
        txtRoomStatus.setText(emp.getRoomAvailability() ? "1" : "0");
        lblid.setText(String.valueOf(emp.getId()));
    }

    private boolean isRoomSelected() {
        if (tblRooms.getSelectionModel().getSelectedItem() == null) {
            lblInfo.setText("Please select a room from the table.");
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
