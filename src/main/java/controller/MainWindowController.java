package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import view.ProgramData;


public class MainWindowController {
    @FXML private Accordion mainAccordion;
    @FXML private Label programInfoLabel;

    @FXML
    void initialize() {
        MainShippingDataController mainShippingDataController = (MainShippingDataController)  ControllerManager.get(MainShippingDataController.class);
        mainAccordion.setExpandedPane(mainShippingDataController.getPane());
        programInfoLabel.setText(String.format("%s v%s (%s) by %s", ProgramData.PROGRAM_NAME, ProgramData.VERSION, ProgramData.LAST_UPDATE, ProgramData.AUTHOR));
    }
}
