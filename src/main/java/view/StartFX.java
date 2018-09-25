package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartFX {
    private static Scene mainScene;

    public static void start(Stage primaryStage) throws IOException {
        mainScene = new Scene(new FXMLLoader(StartFX.class.getResource("/FXInterface/mainWindow.fxml")).load(), 800, 800);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(ProgramData.PROGRAM_NAME + " v" + ProgramData.VERSION);
        primaryStage.show();
        new FXMLLoader(StartFX.class.getResource("/FXInterface/contextMenu.fxml")).load();
    }

    public static Scene getMainScene() {
        return mainScene;
    }

}
