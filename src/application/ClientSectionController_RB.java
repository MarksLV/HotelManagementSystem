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
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientSectionController_RB {
	
    @FXML
    private TableColumn<Rooms, Double> colRoomCost;
    @FXML
    private TableColumn<Rooms, Integer> colRoomNumber;
    @FXML
    private TableColumn<Rooms, Boolean> colRoomStatus;
    @FXML
    private TableView<Rooms> tblRooms;
    @FXML
    private Label lblBookedRoom;
    @FXML
    private Label lblBookedRoomInfo;
    
    // At start - run this code
    public void initialize() throws SQLException {
    	// Fill up all TableColumn in TableView
		colRoomCost.setCellValueFactory(new PropertyValueFactory<Rooms, Double>("roomCost"));
		colRoomNumber.setCellValueFactory(new PropertyValueFactory<Rooms, Integer>("roomNumber"));
		colRoomStatus.setCellValueFactory(new PropertyValueFactory<Rooms, Boolean>("roomAvailability"));
    	populate();
    	displayBookedRoomInfo();
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

    private void displayBookedRoomInfo() throws SQLException {
        int userId = getCurrentUserId();
        DBConnection object = new DBConnection();
        Connection connection = object.connect();
        
        // Query to find the room booked by the current user
        String query = "SELECT rooms.RoomNumb FROM rooms JOIN takenrooms ON rooms.idRooms = takenrooms.idRoom WHERE takenrooms.idRegister = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        
        // If the user has booked a room, display the room number
        if (rs.next()) {
            int roomNumber = rs.getInt("RoomNumb");
            lblBookedRoom.setText("You have booked room number: " + roomNumber);
        } else {
            lblBookedRoom.setText("No room booked.");
        }
        
        // Close connections
        rs.close();
        ps.close();
        connection.close();
    }
    
    @FXML
    private void unbookRoom() {    // Un-Book a room
        try {
            int idRegister = getCurrentUserId();
            
            // Check if the user has booked a room
            if (!userHasBookedRoom(idRegister)) {
                lblBookedRoomInfo.setText("You haven't booked a room.");
                return;
            }
            
            // Database connection established
            DBConnection object = new DBConnection();
            Connection connection = object.connect();
            connection.setAutoCommit(false); // Ensure auto commit is off
            
            // Find the booked room id
            String selectRoomQuery = "SELECT idRoom FROM takenrooms WHERE idRegister = ?";
            PreparedStatement psSelectRoom = connection.prepareStatement(selectRoomQuery);
            psSelectRoom.setInt(1, idRegister);
            ResultSet rs = psSelectRoom.executeQuery();
            rs.next();
            int roomId = rs.getInt("idRoom");
            
            // Update the room availability to true - available
            String updateRoomQuery = "UPDATE rooms SET RoomAvailability = ? WHERE idRooms = ?";
            PreparedStatement psUpdateRoom = connection.prepareStatement(updateRoomQuery);
            psUpdateRoom.setBoolean(1, true);
            psUpdateRoom.setInt(2, roomId);
            psUpdateRoom.executeUpdate();
            
            // Delete the booking from the taken rooms table
            String deleteTakenRoomQuery = "DELETE FROM takenrooms WHERE idRegister = ?";
            PreparedStatement psDeleteTakenRoom = connection.prepareStatement(deleteTakenRoomQuery);
            psDeleteTakenRoom.setInt(1, idRegister);
            psDeleteTakenRoom.executeUpdate();
            
            // Commit changes
            connection.commit();
            
            // Close the prepared statements and connection
            psSelectRoom.close();
            psUpdateRoom.close();
            psDeleteTakenRoom.close();
            connection.close();
            
            // Re-populate the table view
            populate();
            
            // Update the booked room information label
            displayBookedRoomInfo();
            
            // Display a confirmation message
            lblBookedRoomInfo.setText("Room unbooked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void bookRoom() {	// Book a room
        // Get the selected room from the table
        Rooms selectedRoom = tblRooms.getSelectionModel().getSelectedItem();
        
        // Check if a room is selected
        if (selectedRoom != null) {
            try {
                int idRegister = getCurrentUserId();
                
                // Checking possibilities if room has been booked or user already booked an room
                if (!selectedRoom.getRoomAvailability()) {
                    lblBookedRoomInfo.setText("The selected room is already booked.");
                    return;
                }
                if (userHasBookedRoom(idRegister)) {
                    lblBookedRoomInfo.setText("You have already booked a room.");
                    return;
                }
                
                // Update the room availability to false - booked
                selectedRoom.setRoomAvailability(false);
                
                // Update the database
                DBConnection object = new DBConnection();
                Connection connection = object.connect();
                
                // Update the room availability
                connection.setAutoCommit(false);		// Updating and Inserting, but not commit in-case of error
                String updateRoomQuery = "UPDATE rooms SET RoomAvailability = ? WHERE idRooms = ?";
                PreparedStatement psUpdateRoom = connection.prepareStatement(updateRoomQuery);
                psUpdateRoom.setBoolean(1, false);
                psUpdateRoom.setInt(2, selectedRoom.getId());
                psUpdateRoom.executeUpdate();
                
                // Insert into database
                String insertTakenRoomQuery = "INSERT INTO takenrooms (idRoom, idRegister) VALUES (?, ?)";
                PreparedStatement psInsertTakenRoom = connection.prepareStatement(insertTakenRoomQuery);
                psInsertTakenRoom.setInt(1, selectedRoom.getId());
                psInsertTakenRoom.setInt(2, idRegister);
                psInsertTakenRoom.executeUpdate();
                connection.commit();					// Commit changes
                
                // Close the prepared statements and connection
                psUpdateRoom.close();
                psInsertTakenRoom.close();
                connection.close();
                
                // Refresh the table view
                tblRooms.refresh();
                
                // Update the booked room information label
                displayBookedRoomInfo();
                
                // Display a confirmation message
                lblBookedRoomInfo.setText("Room booked successfully!");
            // Catching errors
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            lblBookedRoomInfo.setText("Please select a room before booking.");
        }
    }
    
    private boolean userHasBookedRoom(int idRegister) throws SQLException {
    	// Database connection established
        DBConnection object = new DBConnection();
        Connection connection = object.connect();

        // Select query finding by ID in register
        String query = "SELECT COUNT(*) FROM takenrooms WHERE idRegister = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, idRegister);
        ResultSet rs = ps.executeQuery();
        rs.next();
        
        // Checking between 1 and 0, true and false
        boolean hasBooked = rs.getInt(1) > 0;

        // Close connections
        rs.close();
        ps.close();
        connection.close();

        // Return the value
        return hasBooked;
    }

    public class UserSession {	// Temporary session by ID
        private static int userId;

        // User session ID getter-setter method
        public static void setUserId(int id) {
            userId = id;
        }
        public static int getUserId() {
            return userId;
        }
    }
    
    private int getCurrentUserId() {	// Calling for current session User ID
        return UserSession.getUserId();
    }

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