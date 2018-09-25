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
        initializeAutoRefreshTimeSpinner(Math.floorDiv(tracker.getTimeBetweenRefreshes(), 60));
        initializeCheckFinishedShipmentsCheckBox(!tracker.isCheckFinishedShipments());
        initializeAutoRefreshCheckBox(tracker.isAutoTrackingEnabled());
        startAutoRefreshTimer();
    }

    @FXML
    private void onAutomaticRefreshStatusChange() {
        enableAutoRefresh(automaticRefreshCheckBox.isSelected());
    }

    @FXML
    private void refreshAll() {
        tracker.changeTrackerState(TrackerState.UPDATING);
    }

    @FXML
    private void onCheckFinishedShipmentsStatusChange() {
        tracker.setCheckFinishedShipments(!checkIfFinishedCheckBox.isSelected());
    }

    private void initializeAutoRefreshTimeSpinner(int initial) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SPINNER_VALUE, MAX_SPINNER_VALUE, initial);
        refreshTime.setValueFactory(valueFactory);
        refreshTime.valueProperty().addListener(e -> spinnerValueChanged());
    }

    private void initializeCheckFinishedShipmentsCheckBox(boolean select) {
        checkIfFinishedCheckBox.setSelected(select);
    }

    private void initializeAutoRefreshCheckBox(boolean select) {
        automaticRefreshCheckBox.setSelected(select);
        onAutomaticRefreshStatusChange();
    }

    /*
    * This is only interface update timer. Real timer is in Tracker class.
    * */
    private void startAutoRefreshTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), (e) -> updateTimerText()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void enableAutoRefresh(boolean enable) {
        refreshTime.setDisable(!enable);
        refreshAllButton.setDisable(!enable);
        tracker.enableAutoTracking(enable);
    }

    private void spinnerValueChanged() {
        tracker.setTimeBetweenRefreshes(refreshTime.getValue());
    }

    private void updateTimerText() {
        switch(tracker.getTrackerState()) {
            case DISABLED:
                refreshTimerLabel.setText("Automatyczne odświeżanie wyłączone");
                break;
            case UPDATING:
                refreshAllButton.setDisable(true);
                refreshTimerLabel.setText(String.format("Trwa odświeżanie (%d / %d)", tracker.getCheckedListSize(), ProgramStart.getManager().size()));
                ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class)).refreshTable();
                break;
            default: {
                int restTime = tracker.getTimeToNextUpdate();
                refreshAllButton.setDisable(false);
                refreshTimerLabel.setText(String.format("Odświeżenie za %d minut %d sekund", Math.floorDiv(restTime, 60), restTime % 60));
                break;
            }
        }
    }
}
