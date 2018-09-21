package view;

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
        SaveLoad loader = new SaveLoad();
        loader.loadData(shippingManager);
        //tracker = new Tracker(shippingManager);
        StartFX.start(primaryStage);

        ((ShippingTableViewController) ControllerManager.get(ShippingTableViewController.class)).refreshTable();
    }

    @Override
    public void stop() throws IOException {
        SaveLoad saver = new SaveLoad();
        saver.saveData(shippingManager);
        //tracker.stop();
    }

    public static ShippingManager getManager() {
        return shippingManager;
    }
}
