package model.datasalo;

import model.datasalo.loader.JSonLoader;
import model.datasalo.loader.Loader;
import model.datasalo.saver.JSonSaver;
import model.datasalo.saver.Saver;
import model.tracker.Shipping;

import java.io.IOException;
import java.util.List;

public class SaveLoad {
    private static final String FILE_NAME = "shippingdata.json";
    private Saver saver = new JSonSaver(FILE_NAME);
    private Loader loader = new JSonLoader(FILE_NAME);

    public SaveLoad() throws IOException {
    }

    public void saveData(List<Shipping> listOfShipping) throws IOException {
        saver.save(listOfShipping);
    }

    public List<Shipping> loadData() {
        return loader.load();
    }
}
