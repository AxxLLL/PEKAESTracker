package model.datasalo.syspath;

import model.datasalo.syspath.systems.I_SystemPath;
import model.datasalo.syspath.systems.UndefinedSystem;
import model.datasalo.syspath.systems.Windows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SystemPath {
    private static final SystemType userSystemType;
    private static final Map<SystemType, List<String>> systemPredefinedValues = new HashMap<>();

    static {
        systemPredefinedValues.put(SystemType.WINDOWS, new ArrayList<>(Arrays.asList("Windows XP", "Windows 7", "Windows 8", "Windows 8.1", "Windows 10")));
        userSystemType = getUserSystemType();
    }

    public static Path getPath() {
        I_SystemPath sysPath;
        switch(userSystemType) {
            case WINDOWS: sysPath = new Windows(); break;
            default: sysPath = new UndefinedSystem(); break;
        }
        return Paths.get(sysPath.getPath().toString(), "PEKAESTracker");
    }

    private static SystemType getUserSystemType() {
        String systemName = System.getProperty("os.name");
        for(Map.Entry<SystemType, List<String>> entry : systemPredefinedValues.entrySet()) {
            for(String sysName : entry.getValue()) {
                if(sysName.equals(systemName)) return entry.getKey();
            }
        }
        return SystemType.UNDEFINED;
    }
}