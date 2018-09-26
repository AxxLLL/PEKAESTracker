package model.datasalo;

import java.util.List;

public interface SaverResolver {
    void saveSettings(List<Settings> settings, List<SettingsList> settingsLists);
}
