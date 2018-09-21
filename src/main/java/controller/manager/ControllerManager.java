package controller.manager;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private static Map<Class, Object> controllersMap = new HashMap<>();

    public static void add(Object reference) {
        Preconditions.checkNotNull(reference);
        controllersMap.put(reference.getClass(), reference);
    }

    public static Object get(Class rclass) {
        Preconditions.checkNotNull(rclass);
        return controllersMap.get(rclass);
    }
}
