package model.datasalo.syspath.systems;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UndefinedSystem implements I_SystemPath {
    private final static String home = System.getProperty("user.home").replace("\\", "/");

    @Override
    public Path getPath() {
        return Paths.get(home);
    }
}
