
public class AbstractProgram extends Thread {
    private volatile Status status;
    private volatile boolean isRunning;
    private final long delayInterval;

    public AbstractProgram(long delayInterval) {
        this.status = Status.UNKNOWN;
        this.isRunning = true;
        this.delayInterval = Math.max(delayInterval, 100);
    }

    @Override
    public synchronized void run() { //переопределяем для демон-потока
        Runnable stateChanger = () -> {
            while (isRunning) {
                try {
                    Thread.sleep(this.delayInterval);
                    updateStatus(Status.getRandomStatus());//меняем состояние программы
                } catch (final InterruptedException interruptedException) {
                    currentThread().interrupt();
                }
            }
        };

        Thread daemonThread = new Thread(stateChanger);
        daemonThread.setDaemon(true);//создаем демон-поток
        daemonThread.start();
    }

    public synchronized Status getStatus() {
        return status;
    }

    public synchronized void updateStatus(Status newStatus) {
        this.status = newStatus;
        notify(); //сообщаем об изменении состояния (передаем монитор супервизору)
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void terminate() {
        isRunning = false;
    }
}
