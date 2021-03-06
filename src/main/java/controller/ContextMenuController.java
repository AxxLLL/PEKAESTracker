package controller;

import controller.manager.ControllerManager;
import controller.utils.ClipboardUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.shipment.shp.Shipping;
import view.DialogMessage;
import view.ProgramStart;

import java.util.Optional;

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
        selectAllCheckBoxes(false);
    }

    @FXML
    private void copyElementsToClipboard() {
        StringBuilder sb = new StringBuilder();

        if(shippingNumberCheckBox.isSelected()) sb.append(prepareShipmentNumberToCopy());
        if(titleCheckBox.isSelected()) sb.append(prepareTitleToCopy());
        if(statusCheckBox.isSelected()) sb.append(prepareStatusToCopy());
        if(mainShipmentDataCheckBox.isSelected()) sb.append(prepareMainShipmentDataToCopy());
        if(detailsShipmentDataCheckBox.isSelected()) sb.append(prepareDetailsShipmentDataToCopy());

        if(sb.length() > 0) ClipboardUtil.get().put(sb.toString());
    }

    @FXML
    private void refreshShipmentStatus() {
        ProgramStart.getTracker().updateShipmentData(shipping);
        tableViewController.refreshTable();
    }

    @FXML
    private void deleteShipmentFromList() {
        Optional<ButtonType> response = DialogMessage.showConfirmDialog("Usunięcie przesyłki", "Czy na pewno chcesz usunąć przesyłkę o numerze " + shipping.getShippingNumber() + " z listy?");
        if (response.get() == ButtonType.OK) {
            ProgramStart.getManager().remove(shipping);
            tableViewController.refreshTable();
            ((MainShippingDataController) ControllerManager.get(MainShippingDataController.class)).setMainDataToDefault();
            ((DetailsShippingDataController) ControllerManager.get(DetailsShippingDataController.class)).clearTableData();
        }
    }

    @FXML
    private void selectAllItems() {
        selectAllCheckBoxes(true);
    }

    @FXML
    private void unselectAllItems() {
        selectAllCheckBoxes(false);
    }

    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }

    private Shipping getSelectedItem() {
        return tableViewController.getTableView().getSelectionModel().getSelectedItem();
    }

    private boolean isSelectedMoreThanOneItem() {
        int counter = 0;
        ObservableList<MenuItem> menuItems = ((Menu)contextMenu.getItems().get(0)).getItems();
        Node customMenuItem;
        for(MenuItem item : menuItems) {
            try {
                customMenuItem = ((CustomMenuItem) item).getContent();
                if (customMenuItem != null && customMenuItem.getTypeSelector().equals("CheckBox")) {
                    if(((CheckBox)customMenuItem).isSelected()) counter ++;
                }
            } catch(Exception ignore) {}
        }
        return counter > 1;
    }

    private void selectAllCheckBoxes(final boolean select) {
        ObservableList<MenuItem> menuItems = ((Menu)contextMenu.getItems().get(0)).getItems();
        menuItems.forEach(item -> {
            try {
                Node customMenuItem = ((CustomMenuItem) item).getContent();
                if (customMenuItem != null && customMenuItem.getTypeSelector().equals("CheckBox")) {
                    ((CheckBox)customMenuItem).setSelected(select);
                }
            } catch(Exception ignore) {}
        });
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
