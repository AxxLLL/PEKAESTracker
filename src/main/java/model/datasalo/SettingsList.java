package model.datasalo;

import java.util.List;
import java.util.Map;

public interface SettingsList {
    String getMainKey();
    List<String> getKeys();
    List<Map<String, String>> getSettingsListValues();
    void setSettingsValues(List<Map<String, String>> settings);
}
