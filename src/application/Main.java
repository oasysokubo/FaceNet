package application;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			// Load FXML Resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("JFX.fxml"));

			// Store root element for controllers use
			BorderPane root = (BorderPane) loader.load();

			// Set Background
			root.setStyle("-fx-background-color: whitesmoke;");

			// Set Scene
			Scene scene = new Scene(root, 600, 485);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Give Stage Name
			stage.setTitle("Cammy");
			stage.setResizable(false);
			stage.setScene(scene);
			// Show GUI
			stage.show();

			// Set proper behavior when closing application
			Controller controller = loader.getController();
			// Initialize controller Objects
			controller.initialize();
			stage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent e) {
					controller.setClosed();
				}

			}));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Load native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		launch(args);
	}
}
