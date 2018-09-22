package controller;

import controller.manager.ControllerManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.tracker.Tracker;
import model.tracker.TrackerState;
import view.ProgramStart;

public class AutomaticRefreshController {
    @FXML private Label refreshTimerLabel;
    @FXML private CheckBox automaticRefreshCheckBox;
    @FXML private CheckBox checkIfFinishedCheckBox;
    @FXML private Spinner<Integer> refreshTime;
    @FXML private Button refreshAllButton;

    private static int MAX_SPINNER_VALUE = 60;
    private static int MIN_SPINNER_VALUE = 5;
    private Tracker tracker;

    public AutomaticRefreshController() {
        ControllerManager.add(this);
        tracker = ProgramStart.getTracker();
    }

    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SPINNER_VALUE, MAX_SPINNER_VALUE, Math.floorDiv(tracker.getTimeBetweenRefreshes(), 60));
        refreshTime.setValueFactory(valueFactory);
        refreshTime.valueProperty().addListener(e -> spinnerValueChanged());

        checkIfFinishedCheckBox.setSelected(!tracker.isCheckIfFinished());
        automaticRefreshCheckBox.setSelected(tracker.getState() == TrackerState.DISABLED);
        onAutomaticRefreshStatusChange();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), (e) -> updateTimerText()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void onAutomaticRefreshStatusChange() {
        if(automaticRefreshCheckBox.isSelected()) {
            enableAutomaticRefresh();
        } else {
            disableAutomaticRefresh();
        }
    }

    @FXML
    private void refreshAll() {
        ProgramStart.getTracker().forceUpdate();
    }

    @FXML
    private void onCheckFinishedShipmentsStatusChange() {
        tracker.setCheckIfFinished(!checkIfFinishedCheckBox.isSelected());
    }

    private void disableAutomaticRefresh() {
        refreshTime.setDisable(true);
        refreshAllButton.setDisable(true);
        ProgramStart.getTracker().disableAutoTracking(true);
    }

    private void enableAutomaticRefresh() {
        refreshTime.setDisable(false);
        refreshAllButton.setDisable(false);
        ProgramStart.getTracker().disableAutoTracking(false);
    }

    private void spinnerValueChanged() {
        ProgramStart.getTracker().setTimeBetweenRefreshes(refreshTime.getValue() * 60);
    }

    private void updateTimerText() {
        switch(ProgramStart.getTracker().getState()) {
            case DISABLED:
                refreshTimerLabel.setText("Automatyczne odświeżanie wyłączone");
                break;
            case UPDATING:
                refreshAllButton.setDisable(true);
                refreshTimerLabel.setText(String.format("Trwa odświeżanie (%d / %d)", ProgramStart.getTracker().getCheckedSize(), ProgramStart.getManager().size()));
                ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class)).refreshTable();
                break;
            default: {
                int restTime = ProgramStart.getTracker().getRestTime();
                refreshAllButton.setDisable(false);
                refreshTimerLabel.setText(String.format("Odświeżenie za %d minut %d sekund", Math.floorDiv(restTime, 60), restTime % 60));
                break;
            }
        }
    }
}
