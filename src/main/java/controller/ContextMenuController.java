package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.shipment.shp.Shipping;
import view.ProgramStart;

public class ContextMenuController {
    private ShippingTableViewController tableViewController = ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class));
    private Shipping shipping;

    @FXML
    private ContextMenu contextMenu;

    public ContextMenuController() {
        ControllerManager.add(this);
    }

    @FXML
    private void initialize() {
        shipping = tableViewController.getTableView().getSelectionModel().getSelectedItem();
    }

    @FXML
    private void copyShippingNumber() {
        if(shipping == null) return;
        putElementToClipboard(shipping.getShippingNumber());
    }

    @FXML
    private void refreshShipmentStatus() {
        ProgramStart.getTracker().updateShipmentData(shipping);
        tableViewController.refreshTable();
    }

    @FXML
    private void deleteShipmentFromList() {
        ProgramStart.getManager().remove(shipping);
        tableViewController.refreshTable();
    }

    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    private void putElementToClipboard(String string) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(string);
        clipboard.setContent(clipboardContent);
    }
}
