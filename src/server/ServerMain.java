package server;

import client.ClientUI;
import common.IcmUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerMain extends Application implements ClientUI {

    /**
     * Start the server
     * @param args- the server scene.
     */
	public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Load the scene
     */
    public void start(Stage primaryStage) {

        // load the scene
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/server/serverUI/ServerUI.fxml").openStream());
            Scene scene = new Scene(root, 560, 550);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ICM Server");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            IcmUtils.displayErrorMsg(
                    "Load scene error",
                    "Load scene error",
                    e.getMessage() + "\nPlease contact ICM support team");
        }
    }

    @Override
    public void handleMessageFromClientController(ServerService serverService) {

    }
}
