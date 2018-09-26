package view;

import com.google.common.base.Preconditions;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogMessage {
    public static void showInfoMessage(String title, String message) {
        showMessage(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showErrorMessage(String title, String message) {
        showMessage(Alert.AlertType.ERROR, title, message);
    }

    public static Optional<ButtonType> showConfirmDialog(String title, String message) {
        return showMessage(Alert.AlertType.CONFIRMATION, title, message);
    }

    private static Optional<ButtonType> showMessage(Alert.AlertType type, String title, String message) {
        Preconditions.checkNotNull(message);
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait();
    }
}
