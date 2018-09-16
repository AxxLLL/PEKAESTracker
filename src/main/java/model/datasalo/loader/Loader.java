package model.datasalo.loader;

import model.tracker.shipping.Shipping;

import java.nio.file.Path;
import java.util.List;

public interface Loader {
    List<Shipping> load();
    Path getPath();
}
