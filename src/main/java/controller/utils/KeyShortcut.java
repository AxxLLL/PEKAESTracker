package controller.utils;

import com.google.common.base.Preconditions;
import controller.ShippingTableViewController;
import controller.manager.ControllerManager;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import model.shipment.shp.Shipping;

public class KeyShortcut {

    private static final KeyCombination copyShippingNumber = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    private static KeyShortcut keyShortcut;
    private Scene mainScene;

    public static void create(Scene scene) {
        keyShortcut = new KeyShortcut(scene);
    }

    public static KeyShortcut get() {
        return keyShortcut;
    }

    private void addCopyListener() {
        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(copyShippingNumber.match(event) && event.getTarget().toString().contains("trackingTable")) {
                TableView<Shipping> tableView = ((ShippingTableViewController) ControllerManager.get(ShippingTableViewController.class)).getTableView();
                if(!tableView.getSelectionModel().isEmpty()) {
                    Shipping shp = tableView.getSelectionModel().getSelectedItem();
                    ClipboardUtil.get().put(shp.getShippingNumber());
                }
            }
        });
    }

    private KeyShortcut(Scene scene) {
        Preconditions.checkNotNull(scene);
        mainScene = scene;
        addCopyListener();
    }
}
