package controller.utils;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardUtil {
    private static ClipboardUtil clipboardUtil = new ClipboardUtil();
    private static Clipboard clipboard;
    private static ClipboardContent content;

    public static ClipboardUtil get() {
        return clipboardUtil;
    }

    public void put(String text) {
        content.putString(text);
        clipboard.setContent(content);
    }

    private ClipboardUtil() {
        clipboard = Clipboard.getSystemClipboard();
        content = new ClipboardContent();
    }
}
