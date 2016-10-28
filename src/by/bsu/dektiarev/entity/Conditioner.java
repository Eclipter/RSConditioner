package by.bsu.dektiarev.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by USER on 27.10.2016.
 */
public class Conditioner {

    private double neededTemperature;
    private double degreesPerSecond;
    private double currentTemperature;
    private Lock temperatureLock = new ReentrantLock();
    private double fatalTemperature;
    private ConditionerRegime regime;
    private ConditionerState state;

    public Conditioner(double neededTemperature, double degreesPerSecond, double fatalTemperature) {
        this.neededTemperature = neededTemperature;
        this.currentTemperature = neededTemperature;
        this.fatalTemperature = fatalTemperature;
        this.degreesPerSecond = degreesPerSecond;
        this.regime = ConditionerRegime.MAN_OFF;
        this.state = ConditionerState.OFF;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        //temperatureLock.lock();
        this.currentTemperature = currentTemperature;
        //temperatureLock.unlock();
    }

    public double getNeededTemperature() {
        return neededTemperature;
    }

    public void setNeededTemperature(double neededTemperature) {
        this.neededTemperature = neededTemperature;
    }

    public double getDegreesPerSecond() {
        return degreesPerSecond;
    }

    public void setDegreesPerSecond(double degreesPerSecond) {
        this.degreesPerSecond = degreesPerSecond;
    }

    public ConditionerRegime getRegime() {
        return regime;
    }

    public void setRegime(ConditionerRegime regime) {
        this.regime = regime;
    }

    public double getFatalTemperature() {
        return fatalTemperature;
    }

    public void setFatalTemperature(double fatalTemperature) {
        this.fatalTemperature = fatalTemperature;
    }

    public ConditionerState getState() {
        return state;
    }

    public void setState(ConditionerState state) {
        this.state = state;
    }

    public Lock getTemperatureLock() {
        return temperatureLock;
    }
}
