// Junfeng Wang NetID: jw139, Joey Zheng NetID: jz813
package songList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class SongLib extends Application {

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/songList/List.fxml"));
        AnchorPane root = (AnchorPane) loader.load();

        Scene scene = new Scene(root, 869, 633);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}