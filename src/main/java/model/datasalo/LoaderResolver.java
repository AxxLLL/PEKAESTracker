package model.datasalo;

import java.util.List;

public interface LoaderResolver {
    void loadSettings(List<Settings> settings);
    void loadSettingsLists(List<SettingsList> settings);
}
