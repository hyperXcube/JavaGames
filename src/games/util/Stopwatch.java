package games.util;

import static java.lang.System.currentTimeMillis;

public class Stopwatch {
    private long startTime; // Time when timer was last started
    private long timeBeforeResume = 0; // Total time before timer was last resumed
    private boolean paused = true;

    public void start() {
        startTime = currentTimeMillis();
        paused = false;
    }

    public void pause() {
        timeBeforeResume += currentTimeMillis() - startTime;
        paused = true;
    }

    public void reset() {
        timeBeforeResume = 0;
        paused = true;
    }

    public String getTime() {
        if (paused) {
            return timeString(timeBeforeResume);
        } else {
            return timeString(timeBeforeResume + currentTimeMillis() - startTime);
        }
    }

    // Turns time as millis into readable format
    private String timeString(long millis) {
        long minutes = (millis/1000) / 60;
        long seconds = (millis/1000) % 60;
        String s;
        if (seconds < 10) {
            s = minutes + ":0" + seconds;
        } else {
            s = minutes + ":" + seconds;
        }
        return s;
        // TODO: Add hours
    }

    public boolean isPaused() { return paused; }
}
