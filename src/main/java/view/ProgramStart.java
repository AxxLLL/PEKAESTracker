package view;

import controller.AutomaticRefreshController;
import controller.InputFormController;
import controller.ShippingTableViewController;
import controller.manager.ControllerManager;
import javafx.application.Application;
import javafx.stage.Stage;
import model.datasalo.SaveLoad;
import model.shipment.shp.ShippingManager;
import model.tracker.Tracker;

import java.io.IOException;

public class ProgramStart extends Application {

    private static ShippingManager shippingManager = new ShippingManager();
    private static Tracker tracker;

    @Override
    public void start(Stage primaryStage) throws IOException {
        tracker = new Tracker(shippingManager);
        SaveLoad loader = new SaveLoad();
        loader.loadData(shippingManager);
        StartFX.start(primaryStage);

        ((ShippingTableViewController) ControllerManager.get(ShippingTableViewController.class)).refreshTable();
    }

    @Override
    public void stop() throws IOException {
        SaveLoad saver = new SaveLoad();
        saver.saveData(shippingManager);
        ((InputFormController)ControllerManager.get(InputFormController.class)).hideMessage();
        tracker.stop();
    }

    public static ShippingManager getManager() {
        return shippingManager;
    }

    public static Tracker getTracker() {
        return tracker;
    }
}
