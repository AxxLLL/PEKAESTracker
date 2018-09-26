package model.datasalo;

import java.util.List;
import java.util.Map;

public interface Settings {
    List<String> getKeys();
    Map<String, String> getSettingsValues();
    void setSettingsValues(Map<String, String> settings);
}
