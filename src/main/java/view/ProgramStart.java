/**
 * <p>PEKAES Tracker v1.0.0 by Rafał (AxL) Żółtański</p>
 *
 * <a href="https://ibb.co/ikY6P9"><img src="https://thumb.ibb.co/ikY6P9/PEKAESTracker_details_data_view.jpg" alt="PEKAESTracker_details_data_view" border="0"></a>
 * <a href="https://ibb.co/ga92xU"><img src="https://thumb.ibb.co/ga92xU/PEKAESTracker_main_data_view.jpg" alt="PEKAESTracker_main_data_view" border="0"></a>
 * */


package view;

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
