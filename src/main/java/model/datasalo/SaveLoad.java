package model.datasalo;

import com.google.common.base.Preconditions;
import lombok.Getter;
import model.datasalo.loader.JSonLoader;
import model.datasalo.loader.Loader;
import model.datasalo.saver.JSonSaver;
import model.datasalo.saver.Saver;
import model.shipment.shp.ShippingManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad {
    private static final String FILE_NAME = "shippingdata.json";
    private Saver saver = new JSonSaver(FILE_NAME);
    private Loader loader = new JSonLoader(FILE_NAME);
    @Getter private List<Settings> observedSettings = new ArrayList<>();
    @Getter private List<SettingsList> observedSettingsLists = new ArrayList<>();

    public SaveLoad() throws IOException {

    }

    public void saveData(ShippingManager manager) throws IOException {
        saver.save(manager.getAll());
    }

    public void loadData(ShippingManager manager) {
        manager.clear();
        manager.addAll(loader.load());
    }

    public void addToObservedSettings(Settings element) {
        Preconditions.checkNotNull(element);
        if(!observedSettings.contains(element)) {
            observedSettings.add(element);
        }
    }

    public void addToObservedListSettings(SettingsList element) {
        Preconditions.checkNotNull(element);
        if(!observedSettingsLists.contains(element)) {
            observedSettingsLists.add(element);
        }
    }

}
