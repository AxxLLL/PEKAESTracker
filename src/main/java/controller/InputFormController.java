package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import view.DialogMessage;
import view.ProgramStart;

import java.util.Timer;
import java.util.TimerTask;

public class InputFormController {
    @FXML private TextField shippingNumberInput;
    @FXML private TextField titleInput;
    @FXML private Label addStatusLabel;
    @FXML private Button addButton;
    private Timer timer;

    public InputFormController() {
        ControllerManager.add(this);
    }

    @FXML
    private void onShipmentAddedToTracking() {
        String shNumber = shippingNumberInput.getText();
        String title = titleInput.getText();

        if(!Shipping.isValidShippingNumberFormat(shNumber)) {
            showErrorMessage("Niepoprawny format numeru przesyłki");
        } else {
            try {
                Shipping shp = new Shipping(shNumber, title);
                if(shp.getStatus() == ShipmentStatus.INVALID_DATA_FORMAT) {
                    String message = "" +
                            "Wystąpił błąd w połączeniu z serwerem PEKAES!" + System.lineSeparator() +
                            "Serwer odrzucił połączenie, jest wyłączony lub zmieniła się" + System.lineSeparator() +
                            "struktura odpowiedzi." + System.lineSeparator() +
                            "Stan serwera można sprawdzić pod adresem:" + System.lineSeparator() +
                            "www.pekaes.pl/pl/dla-klienta.html";
                    DialogMessage.showErrorMessage("Błąd odpwiedzi", message);
                } else if(shp.getStatus() == ShipmentStatus.INVALID_SHIPMENT_NUMBER) {
                    showErrorMessage("Nieodnaleziono przesyłki o podanym numerze na stronie przewoźnika");
                } else {
                    ProgramStart.getManager().add(shp);
                    ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class)).refreshTable();
                    shippingNumberInput.clear();
                    titleInput.clear();
                    showConfirmMessage("Dodano przesyłkę do listy");
                }
            } catch (IllegalArgumentException e) {
                showErrorMessage(e.getMessage());
            }
        }
    }

    public void hideMessage() {
        stopTimerIfExists();
        addStatusLabel.setVisible(false);
    }

    private void stopTimerIfExists() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void showErrorMessage(String message) {
        showMessage("red", message);
    }

    private void showConfirmMessage(String message) {
        showMessage("green", message);
    }

    private void showMessage(String color, String message) {
        addStatusLabel.setStyle("-fx-text-fill: "+color+";");
        addStatusLabel.setText(message);
        addStatusLabel.setVisible(true);

        stopTimerIfExists();
        timer = new Timer();
        timer.schedule(new ShowMessageTimer(), 4000);
    }

    private class ShowMessageTimer extends TimerTask {
        @Override
        public void run() {
            hideMessage();
        }
    }

}
