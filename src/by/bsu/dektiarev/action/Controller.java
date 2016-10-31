package by.bsu.dektiarev.action;

import by.bsu.dektiarev.controller.ConditionerThread;
import by.bsu.dektiarev.entity.Conditioner;
import by.bsu.dektiarev.entity.ConditionerRegime;
import by.bsu.dektiarev.entity.ConditionerState;
import by.bsu.dektiarev.util.OptionsManager;
import by.bsu.dektiarev.util.OptionsParameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {

    private static final Media CON_TURNING_ON_AUDIO =
            new Media(Controller.class.getResource("/6023_01.wav").toString());
    private static final Media CON_TURNING_OFF_AUDIO =
            new Media(Controller.class.getResource("/6021_01.wav").toString());
    private static final Media CON_WORKING_AUDIO =
            new Media(Controller.class.getResource("/6022_01.wav").toString());

    private List<ConditionerThread> threadList = new ArrayList<>();
    private MonitorThread monitorThread = new MonitorThread();
    private Lock restartLock = new ReentrantLock();

    @FXML
    private ImageView con1ImageView;

    @FXML
    private Slider con1Slider;

    @FXML
    private Spinner<Double> con1TempSpinner;

    @FXML
    private Circle con1WorkIndicator;

    @FXML
    private Circle con1AlertIndicator;

    @FXML
    private Button con1RestartButton;

    @FXML
    private ImageView con2ImageView;

    @FXML
    private Slider con2Slider;

    @FXML
    private Spinner<Double> con2TempSpinner;

    @FXML
    private Circle con2WorkIndicator;

    @FXML
    private Circle con2AlertIndicator;

    @FXML
    private Button con2RestartButton;

    @FXML
    private ImageView con3ImageView;

    @FXML
    private Slider con3Slider;

    @FXML
    private Spinner<Double> con3TempSpinner;

    @FXML
    private Circle con3WorkIndicator;

    @FXML
    private Circle con3AlertIndicator;

    @FXML
    private Button con3RestartButton;

    @FXML
    private MenuItem exitButton;

    @FXML
    void exit(ActionEvent event) {
        threadList.forEach(thread -> thread.setStopRequest(true));
        monitorThread.setStopRequest(true);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @FXML
    void restartConditioner(ActionEvent event) {
        Button source = (Button) event.getSource();
        restartLock.lock();
        switch (source.getId()) {
            case "con1RestartButton":
                restartConditioner1();
                break;
            case "con2RestartButton":
                restartConditioner2();
                break;
            case "con3RestartButton":
                restartConditioner3();
                break;
            default:
                System.out.println("Not working" + event.getSource());
                break;
        }
        restartLock.unlock();
    }

    private void changeRegime(int conditionerId, double value) {
        ConditionerRegime regime;
        if (value == 0) {
            regime = ConditionerRegime.MAN_ON;
        } else if (value == 1) {
            regime = ConditionerRegime.MAN_OFF;
        } else if (value == 2) {
            regime = ConditionerRegime.AUTOMATIC;
        } else {
            return;
        }
        threadList.get(conditionerId).getConditioner().setRegime(regime);
    }

    private void changeTemperature(int conditionerId, double value) {
        threadList.get(conditionerId).getConditioner().setCurrentTemperature(value);
    }

    private void restartConditioner1() {
        con1ImageView.setImage(new Image(OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
        con1TempSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-50, 50,
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW))));
        con1WorkIndicator.setFill(Color.WHITE);
        con1AlertIndicator.setFill(Color.WHITE);
        con1Slider.adjustValue(1);

        Conditioner conditioner = new Conditioner(
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.FATAL_TEMPERATURE)));
        if (threadList.size() > 0) {
            threadList.get(0).setStopRequest(true);
            threadList.set(0, new ConditionerThread(conditioner));
        } else {
            threadList.add(new ConditionerThread(conditioner));
        }
        threadList.get(0).start();
    }

    private void restartConditioner2() {
        con2ImageView.setImage(new Image(OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
        con2TempSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-50, 50,
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW))));
        con2WorkIndicator.setFill(Color.WHITE);
        con2AlertIndicator.setFill(Color.WHITE);
        con2Slider.adjustValue(1);

        Conditioner conditioner = new Conditioner(
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.FATAL_TEMPERATURE)));
        if (threadList.size() > 1) {
            threadList.get(1).setStopRequest(true);
            threadList.set(1, new ConditionerThread(conditioner));
        } else {
            threadList.add(new ConditionerThread(conditioner));
        }
        threadList.get(1).start();
    }

    private void restartConditioner3() {
        con3ImageView.setImage(new Image(OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
        con3TempSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-50, 50,
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW))));
        con3WorkIndicator.setFill(Color.WHITE);
        con3AlertIndicator.setFill(Color.WHITE);
        con3Slider.adjustValue(1);

        Conditioner conditioner = new Conditioner(
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.NEEDED_TEMPERATURE)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.DEGREES_PER_ROW)),
                Double.parseDouble(OptionsManager.getProperty(OptionsParameter.FATAL_TEMPERATURE)));
        if (threadList.size() > 2) {
            threadList.get(2).setStopRequest(true);
            threadList.set(2, new ConditionerThread(conditioner));
        } else {
            threadList.add(new ConditionerThread(conditioner));
        }
        threadList.get(2).start();
    }

    private void initConditioners() {
        restartConditioner1();
        restartConditioner2();
        restartConditioner3();
    }

    @FXML
    void initialize() {
        con1Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeRegime(0, (Double) newValue);
        });
        con2Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeRegime(1, (Double) newValue);
        });
        con3Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeRegime(2, (Double) newValue);
        });

        initConditioners();
        con1TempSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            changeTemperature(0, newValue);
        }));
        con2TempSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            changeTemperature(1, newValue);
        }));
        con3TempSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            changeTemperature(2, newValue);
        }));
        monitorThread.start();
    }

    @FXML
    void showInfoDialog(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Система кондиционирования помещений");
        alert.setContentText(OptionsManager.getProperty(OptionsParameter.ABOUT_TEXT));
        alert.showAndWait();
    }

    private class MonitorThread extends Thread {

        private AtomicBoolean stopRequest = new AtomicBoolean();
        private List<ConditionerState> stateList = Arrays.asList(
                ConditionerState.OFF, ConditionerState.OFF, ConditionerState.OFF);
        private List<MediaPlayer> playerList = Arrays.asList(
                new MediaPlayer(CON_TURNING_ON_AUDIO),
                new MediaPlayer(CON_TURNING_ON_AUDIO),
                new MediaPlayer(CON_TURNING_ON_AUDIO));

        private void addListenerToPlayer(MediaPlayer player, int index) {
            player.setOnEndOfMedia(() -> {
                player.dispose();
                MediaPlayer player1 = new MediaPlayer(CON_WORKING_AUDIO);
                player1.setOnEndOfMedia(this);
                playerList.set(index, player1);
                player1.play();
            });
        }

        @Override
        public void run() {

            while (true) {
                if (stopRequest.get()) {
                    return;
                }
                restartLock.lock();
                Conditioner con1 = threadList.get(0).getConditioner();
                ConditionerState state = con1.getState();
                if (state != stateList.get(0)) {
                    stateList.set(0, state);
                    switch (state) {
                        case ON:
                            con1ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.MOVING_IMAGE)));
                            con1WorkIndicator.setFill(Color.GREEN);
                            MediaPlayer player = playerList.get(0);
                            player.stop();
                            player.dispose();
                            MediaPlayer player1 = new MediaPlayer(CON_TURNING_ON_AUDIO);
                            addListenerToPlayer(player1, 0);
                            playerList.set(0, player1);
                            playerList.get(0).play();
                            break;
                        case OFF:
                            con1ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con1WorkIndicator.setFill(Color.WHITE);
                            player = playerList.get(0);
                            player.stop();
                            player.dispose();
                            player1 = new MediaPlayer(CON_TURNING_OFF_AUDIO);
                            playerList.set(0, player1);
                            playerList.get(0).play();
                            break;
                        case DISASTER:
                            con1ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con1WorkIndicator.setFill(Color.WHITE);
                            con1AlertIndicator.setFill(Color.RED);
                            playerList.get(0).stop();
                            playerList.get(0).dispose();
                            break;
                    }
                }
                if (state == ConditionerState.ON) {
                    con1TempSpinner.getValueFactory().setValue(con1.getCurrentTemperature());
                }

                Conditioner con2 = threadList.get(1).getConditioner();
                state = con2.getState();
                if (state != stateList.get(1)) {
                    stateList.set(1, state);
                    switch (state) {
                        case ON:
                            con2ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.MOVING_IMAGE)));
                            con2WorkIndicator.setFill(Color.GREEN);
                            MediaPlayer player = playerList.get(1);
                            player.stop();
                            player.dispose();
                            MediaPlayer player1 = new MediaPlayer(CON_TURNING_ON_AUDIO);
                            addListenerToPlayer(player1, 1);
                            playerList.set(1, player1);
                            playerList.get(1).play();
                            break;
                        case OFF:
                            con2ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con2WorkIndicator.setFill(Color.WHITE);
                            player = playerList.get(1);
                            player.stop();
                            player.dispose();
                            player1 = new MediaPlayer(CON_TURNING_OFF_AUDIO);
                            playerList.set(1, player1);
                            playerList.get(1).play();
                            break;
                        case DISASTER:
                            con2ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con2WorkIndicator.setFill(Color.WHITE);
                            con2AlertIndicator.setFill(Color.RED);
                            playerList.get(1).stop();
                            playerList.get(1).dispose();
                            break;
                    }
                }
                if (state == ConditionerState.ON) {
                    con2TempSpinner.getValueFactory().setValue(con2.getCurrentTemperature());
                }

                Conditioner con3 = threadList.get(2).getConditioner();
                state = con3.getState();
                if (state != stateList.get(2)) {
                    stateList.set(2, state);
                    switch (state) {
                        case ON:
                            con3ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.MOVING_IMAGE)));
                            con3WorkIndicator.setFill(Color.GREEN);
                            MediaPlayer player = playerList.get(2);
                            player.stop();
                            player.dispose();
                            MediaPlayer player1 = new MediaPlayer(CON_TURNING_ON_AUDIO);
                            addListenerToPlayer(player1, 2);
                            playerList.set(2, player1);
                            playerList.get(2).play();
                            break;
                        case OFF:
                            con3ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con3WorkIndicator.setFill(Color.WHITE);
                            player = playerList.get(2);
                            player.stop();
                            player.dispose();
                            player1 = new MediaPlayer(CON_TURNING_OFF_AUDIO);
                            playerList.set(2, player1);
                            playerList.get(2).play();
                            break;
                        case DISASTER:
                            con3ImageView.setImage(new Image(
                                    OptionsManager.getProperty(OptionsParameter.STATIC_IMAGE)));
                            con3WorkIndicator.setFill(Color.WHITE);
                            con3AlertIndicator.setFill(Color.RED);
                            playerList.get(2).stop();
                            playerList.get(2).dispose();
                            break;
                    }
                }
                if (state == ConditionerState.ON) {
                    con3TempSpinner.getValueFactory().setValue(con3.getCurrentTemperature());
                }

                restartLock.unlock();

                try {
                    Thread.sleep(ConditionerThread.TIME_OUT / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void setStopRequest(boolean stop) {
            stopRequest.compareAndSet(!stop, stop);
        }
    }
}
