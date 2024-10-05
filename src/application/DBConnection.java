package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {

	// Static database link
    public static Connection databaselink;

    public Connection connect() throws SQLException {

    	// Database login - connection details
        String db_emer = "hotelmanagement";
        String username = "root";
        String password = "1234";
        String url = "jdbc:mysql://localhost:3306/" + db_emer;

        try {
        	// Establish connection
            databaselink = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
        	// Failing to establish connection
            ex.printStackTrace(); 					// Handle the exception properly
            throw ex; 								// Propagate the exception to the caller
        }

        return databaselink;
    }
    
    public static void updateRoomAvailability(int roomId, boolean availability) throws SQLException {
    	// Convert boolean availability to integer 1 = true, 0 = false)
        int availabilityValue = availability ? 1 : 0;
        
        String sql = "UPDATE rooms SET RoomAvailability = ? WHERE idRooms = ?";
        // Try check statement to ensure the connection and statement are closed after use
        try (Connection connection = new DBConnection().connect();
        	PreparedStatement statement = connection.prepareStatement(sql)) {
        	// Set RoomAvailability in query to converted integer
            statement.setInt(1, availabilityValue);
            // Set idRooms parameter in query to provided roomId
            statement.setInt(2, roomId);
            // Execute the update query
            statement.executeUpdate();
        }
    }
}
