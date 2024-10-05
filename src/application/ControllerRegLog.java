package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.ClientSectionController_RB.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllerRegLog {

    @FXML
    private TextField clientEmail;

    @FXML
    private TextField clientName;

    @FXML
    private TextField clientPassword;

    @FXML
    private TextField clientPhone;

    @FXML
    private TextField clientSurname;

    @FXML
    private Text clientNameLogin;
    
    @FXML
    private TextField adminPassword;

    @FXML
    private TextField adminUsername;
    
    @FXML
    private Label lblEro;
    
    @FXML
    public void addClient() {	// Client Register section
    	// Establish Connection
        DBConnection object = new DBConnection();
        Connection objectConnection = null;
        PreparedStatement addquery = null;
        ResultSet generatedKeys = null;

        try {
            objectConnection = object.connect();

            // Validate if phone number has only numbers
            String phone = clientPhone.getText();
            if (!phone.matches("\\d+")) {
                throw new IllegalArgumentException("Phone number must contain numbers only!");
            }

            // Creating execute SQL query to add new Client
            String sql = "INSERT INTO register (FirstName, LastName, Password, Email, Phone) VALUES (?, ?, ?, ?, ?)";
            addquery = objectConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            addquery.setString(1, clientName.getText());
            addquery.setString(2, clientSurname.getText());
            addquery.setString(3, clientPassword.getText());
            addquery.setString(4, clientEmail.getText());
            addquery.setString(5, phone);
            addquery.executeUpdate();

            // Get the generated client ID
            generatedKeys = addquery.getGeneratedKeys();	// Added to retrieve the generated keys
            if (generatedKeys.next()) {
                int newClientId = generatedKeys.getInt(1);
                UserSession.setUserId(newClientId);			// Added to set the user session with the new client ID
            }

            // Load Client Section FXML file and switch to it
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientSection.fxml"));
            Parent root = loader.load();
            
            // Pass the Clients name to ClientSectionController
            ClientSectionController clientSectionController = loader.getController();
            clientSectionController.initialize(clientName.getText());

            Scene scene = new Scene(root);
            Stage stage = (Stage) clientName.getScene().getWindow();	// Get the current stage
            stage.setScene(scene);
            stage.show();
            
        // Catching possible Errors
        } catch (SQLException e) {
            e.printStackTrace();
            lblEro.setText("Failed to register client.");
        } catch (IllegalArgumentException e) {
            lblEro.setText("Failed to register client.");
        } catch (IOException e) {
            lblEro.setText("Failed to load ClientSection.fxml.");
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (addquery != null) addquery.close();
                if (objectConnection != null) objectConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    @FXML
    public void loginClient() {		// Client lOGIN section
    	// Establish Connection
        DBConnection object = new DBConnection();
        Connection objectConnection = null;
        PreparedStatement loginQuery = null;
        ResultSet resultSet = null;

        try {
        	// Check if client email and password in register are for same person
            objectConnection = object.connect();
            String sql = "SELECT * FROM register WHERE Email = ? AND Password = ?";
            loginQuery = objectConnection.prepareStatement(sql);
            loginQuery.setString(1, clientEmail.getText());
            loginQuery.setString(2, clientPassword.getText());
            resultSet = loginQuery.executeQuery();

            if (resultSet.next()) {
                // Load Client Section FXML file and switch to it
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientSection.fxml"));
                Parent root = loader.load();

                // Pass the Clients name to ClientSectionController
                ClientSectionController clientSectionController = loader.getController();
                clientSectionController.initialize(resultSet.getString("FirstName"));;

                Scene scene = new Scene(root);
                Stage stage = (Stage) clientEmail.getScene().getWindow();
                UserSession.setUserId(resultSet.getInt("idRegister"));
                stage.setScene(scene);
                stage.show();
            } else {
            	// If email or password in invalid
                lblEro.setText("Invalid email or password!");
            }

        // Catching possible Errors
        } catch (SQLException e) {
            e.printStackTrace();
            lblEro.setText("Failed to log in client.");
        } catch (IOException e) {
            lblEro.setText("Failed to load ClientSection.fxml.");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (loginQuery != null) loginQuery.close();
                if (objectConnection != null) objectConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    public void loginAdmin() {		// Administrator Login section
    	// Establish Connection
        DBConnection object = new DBConnection();
        Connection objectConnection = null;
        PreparedStatement loginQuery = null;
        ResultSet resultSet = null;

        try {
        	// Check if administrators user name and password in register are for same person
            objectConnection = object.connect();
            String sql = "SELECT * FROM adminregister WHERE Username = ? AND Password = ?";
            loginQuery = objectConnection.prepareStatement(sql);
            loginQuery.setString(1, adminUsername.getText());
            loginQuery.setString(2, adminPassword.getText());
            resultSet = loginQuery.executeQuery();

            if (resultSet.next()) {
                // Load new FXML file and switch to it
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminSection.fxml"));
                Parent root = loader.load();

                // Pass the Administrator user name to ClientSectionController
                AdminSectionController adminSectionController = loader.getController();
                adminSectionController.initialize(resultSet.getString("Username"));

                Scene scene = new Scene(root);
                Stage stage = (Stage) adminUsername.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
            	// If email or password in invalid
            	lblEro.setText("Invalid username or password!");
            }
        // Catching possible Errors
        } catch (SQLException e) {
            e.printStackTrace();
            lblEro.setText("Failed to log in admin.");
        } catch (IOException e) {
        	lblEro.setText("Failed to load AdminSection.fxml.");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (loginQuery != null) loginQuery.close();
                if (objectConnection != null) objectConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // This will go back to main menu - administrator
    public void getbackAdmin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainAdmin.fxml"));
        Scene scene = new Scene(root);
        Main.globalStage.setScene(scene);
    }
    
    // This will go back to main menu - client
    public void getbackClient() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainClient.fxml"));
        Scene scene = new Scene(root);
        Main.globalStage.setScene(scene);
    }
}
