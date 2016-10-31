package by.bsu.dektiarev.controller;

import by.bsu.dektiarev.entity.Conditioner;
import by.bsu.dektiarev.entity.ConditionerState;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by USER on 27.10.2016.
 */
public class ConditionerThread extends Thread {

    public static final int TIME_OUT = 1000;
    private Conditioner conditioner;
    private AtomicBoolean stopRequest = new AtomicBoolean();

    public ConditionerThread(Conditioner conditioner) {
        this.conditioner = conditioner;
    }

    @Override
    public void run() {
        while(true) {
            if(getStopRequest()) {
                break;
            }
            if(conditioner.getCurrentTemperature() <= conditioner.getFatalTemperature() ||
                    conditioner.getCurrentTemperature() >= 40) {
                conditioner.setState(ConditionerState.DISASTER);
                break;
            }
            switch (conditioner.getRegime()) {
                case MAN_OFF:
                    conditioner.setState(ConditionerState.OFF);
                    break;
                case MAN_ON:
                    conditioner.setState(ConditionerState.ON);
                    adjustTemperature();
                    break;
                case AUTOMATIC:
                    if(conditioner.getCurrentTemperature() == conditioner.getNeededTemperature()) {
                        conditioner.setState(ConditionerState.OFF);
                    } else {
                        conditioner.setState(ConditionerState.ON);
                        adjustTemperature();
                    }
                    break;
            }
            try {
                Thread.sleep(TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void adjustTemperature() {
        if(conditioner.getCurrentTemperature() < conditioner.getNeededTemperature()) {
            conditioner.setCurrentTemperature(conditioner.getCurrentTemperature() +
                    conditioner.getDegreesPerSecond());
        } else if(conditioner.getCurrentTemperature() > conditioner.getNeededTemperature()) {
            conditioner.setCurrentTemperature(conditioner.getCurrentTemperature() -
                    conditioner.getDegreesPerSecond());
        }
        //System.out.println("New: " + conditioner.getCurrentTemperature());
    }

    public Conditioner getConditioner() {
        return conditioner;
    }

    public boolean getStopRequest() {
        return stopRequest.get();
    }

    public void setStopRequest(boolean stop) {
        stopRequest.compareAndSet(!stop, stop);
    }
}
