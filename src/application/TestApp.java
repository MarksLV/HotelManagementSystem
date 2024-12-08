import javafx.application.Application;
import javafx.stage.Stage;

public class TestApp {
    public static void main(String[] args) {
        try {
            // Create a dummy subclass of Application to launch your main JavaFX app
            Application.launch(Main.class, args);
        } catch (Exception e) {
            System.out.println("Error running JavaFX application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);  // Return error code 1 if the application fails to launch
        }
    }
}
