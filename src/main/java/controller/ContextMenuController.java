package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.shipment.shp.Shipping;
import view.ProgramStart;

public class ContextMenuController {
    private ShippingTableViewController tableViewController = ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class));
    private Shipping shipping;
    @FXML private ContextMenu contextMenu;
    @FXML private CheckBox shippingNumberCheckBox;
    @FXML private CheckBox titleCheckBox;
    @FXML private CheckBox statusCheckBox;
    @FXML private CheckBox mainShipmentDataCheckBox;
    @FXML private CheckBox detailsShipmentDataCheckBox;

    public ContextMenuController() {
        ControllerManager.add(this);
    }

    @FXML
    void onShown() {
        shipping = getSelectedItem();
        resetCheckboxes();
    }

    @FXML
    private void copyElementsToClipboard() {
        StringBuilder sb = new StringBuilder();

        if(shippingNumberCheckBox.isSelected()) sb.append(prepareShipmentNumberToCopy());
        if(titleCheckBox.isSelected()) sb.append(prepareTitleToCopy());
        if(statusCheckBox.isSelected()) sb.append(prepareStatusToCopy());
        if(mainShipmentDataCheckBox.isSelected()) sb.append(prepareMainShipmentDataToCopy());
        if(detailsShipmentDataCheckBox.isSelected()) sb.append(prepareDetailsShipmentDataToCopy());

        if(sb.length() > 0) putElementToClipboard(sb.toString());
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
        ((MainShippingDataController)ControllerManager.get(MainShippingDataController.class)).setMainDataToDefault();
        ((DetailsShippingDataController)ControllerManager.get(DetailsShippingDataController.class)).clearTableData();
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

    private Shipping getSelectedItem() {
        return tableViewController.getTableView().getSelectionModel().getSelectedItem();
    }

    private boolean isSelectedMoreThanOneItem() {
        int counter = 0;
        if(shippingNumberCheckBox.isSelected()) counter ++;
        if(titleCheckBox.isSelected()) counter ++;
        if(statusCheckBox.isSelected()) counter ++;
        if(mainShipmentDataCheckBox.isSelected()) counter ++;
        if(detailsShipmentDataCheckBox.isSelected()) counter ++;
        return counter > 1;
    }

    private void resetCheckboxes() {
        shippingNumberCheckBox.setSelected(false);
        titleCheckBox.setSelected(false);
        statusCheckBox.setSelected(false);
        mainShipmentDataCheckBox.setSelected(false);
        detailsShipmentDataCheckBox.setSelected(false);
    }

    private String prepareShipmentNumberToCopy() {
        return isSelectedMoreThanOneItem() ? "Numer przesyłki: " + shipping.getShippingNumber() + System.lineSeparator() : shipping.getShippingNumber();
    }

    private String prepareTitleToCopy() {
        return isSelectedMoreThanOneItem() ? "Tytuł: " + shipping.getTitle() + System.lineSeparator() : shipping.getTitle();
    }

    private String prepareStatusToCopy() {
        String status = tableViewController.getStatusValueAsString(shipping);
        return isSelectedMoreThanOneItem() ? "Status: " + status + System.lineSeparator() : status;
    }

    private String prepareMainShipmentDataToCopy() {
        StringBuilder sb = new StringBuilder("-----------------").append(System.lineSeparator()).append("Dane szczegółowe o przesyłce: ").append(System.lineSeparator());
        if(shipping.getMainData() == null) sb.append("Brak");
        else sb.append(shipping.getMainData());
        return sb.append(System.lineSeparator()).toString();
    }

    private String prepareDetailsShipmentDataToCopy() {
        StringBuilder sb = new StringBuilder("-----------------").append(System.lineSeparator()).append("Szczegóły paczek: ").append(System.lineSeparator());
        if(shipping.getDetailsData() == null) sb.append("Brak");
        else shipping.getDetailsData().forEach(sb::append);
        return sb.append(System.lineSeparator()).toString();
    }
}
