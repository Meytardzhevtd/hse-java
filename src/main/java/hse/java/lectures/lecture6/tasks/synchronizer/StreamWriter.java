package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

import java.io.PrintStream;

public class StreamWriter implements Runnable {

    private final String message;
    @Getter
    private final int id;
    private final PrintStream output;
    private final Runnable onTick;
    private volatile StreamingMonitor monitor;
    private int ticksPrinted = 0;

    public StreamWriter(int id, String message, PrintStream output, Runnable onTick) {
        this.message = message;
        this.id = id;
        this.output = output;
        this.onTick = onTick;
    }

    public void attachMonitor(StreamingMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (ticksPrinted > Synchronizer.DEFAULT_TICKS_PER_WRITER) {
            try {
                monitor.waitForTurn(id);
                output.print(message);
                ticksPrinted++;
                onTick.run();
                monitor.notifyTurnDone();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
