package model.datasalo;

import model.datasalo.loader.JSonLoader;
import model.datasalo.loader.Loader;
import model.datasalo.saver.JSonSaver;
import model.datasalo.saver.Saver;
import model.tracker.shipping.ShippingManager;

import java.io.IOException;

public class SaveLoad {
    private static final String FILE_NAME = "shippingdata.json";
    private Saver saver = new JSonSaver(FILE_NAME);
    private Loader loader = new JSonLoader(FILE_NAME);

    public SaveLoad() throws IOException {
    }

    public void saveData(ShippingManager manager) throws IOException {
        saver.save(manager.getAll());
    }

    public void loadData(ShippingManager manager) {
        manager.clear();
        manager.addAll(loader.load());
    }
}
