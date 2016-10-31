package by.bsu.dektiarev.controller;

import by.bsu.dektiarev.action.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by USER on 31.10.2016.
 */
public class AudioThread extends Thread {

    private String audioUrl;
    private AtomicBoolean stopRequest = new AtomicBoolean();

    public AudioThread(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public void run() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("/" + audioUrl));
            clip.open(inputStream);
            clip.start();
            while(!stopRequest.get()) {

            }
            clip.stop();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setStopRequest(boolean stop) {
        stopRequest.compareAndSet(!stop, stop);
    }
}
