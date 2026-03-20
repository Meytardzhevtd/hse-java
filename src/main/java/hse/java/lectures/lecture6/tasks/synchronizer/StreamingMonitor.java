package hse.java.lectures.lecture6.tasks.synchronizer;

public class StreamingMonitor {
    private int currentId = 1;
    private int totalCount = 0;
    private int limit;
    private boolean isFinished = false;

    public StreamingMonitor(int totalTicks) {
        this.limit = totalTicks;
    }

    public synchronized void waitForTurn(int id) throws InterruptedException {
        while (currentId != id || isFinished) {
            this.wait();
        }
    }

    public synchronized void notifyTurnDone() {
        totalCount++;
        if (totalCount == limit) {
            isFinished = true;
        }
        currentId = (currentId % 3) + 1;
        this.notifyAll();
    }
}
