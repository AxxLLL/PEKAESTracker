package model.datasalo.syspath.systems;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Windows implements I_SystemPath {
    private final static String home = System.getProperty("user.home").replace("\\", "/");
    private final static String oldWindowsPath = home + "/Application Data";
    private final static String newWindowsPath = home + "/AppData";

    @Override
    public Path getPath() {
        if (new File(oldWindowsPath).exists()) return Paths.get(oldWindowsPath);
        else if (new File(newWindowsPath).exists()) return Paths.get(newWindowsPath, "Local");
        else return new UndefinedSystem().getPath();
    }
}
