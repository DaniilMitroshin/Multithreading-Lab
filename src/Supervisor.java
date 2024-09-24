import java.io.Closeable;

public class Supervisor extends Thread implements Closeable {
    private final AbstractProgram program;

    public Supervisor(AbstractProgram program) {
        this.program = program;
    }

    private void restartProgram() {
        program.updateStatus(Status.RUNNING);
        System.out.println("Supervisor: Program restarted.");
    }

    @Override
    public void run() {
        while (program.isRunning()) {
            synchronized (program) {
                try {
                    program.wait(); //ждем изменения состояния
                } catch (final InterruptedException interruptedException) {
                    currentThread().interrupt();
                }

                Status currentState = program.getStatus(); // сообщаем о текущем статусе
                switch (currentState) {
                    case STOPPING:
                        System.out.println("Supervisor: Detected STOPPING state. Restarting...");
                        restartProgram();
                        break;
                    case FATAL_ERROR:
                        System.out.println("Supervisor: Detected FATAL_ERROR state. Terminating program.");
                        program.terminate();
                        return;
                    default:
                        System.out.println(currentState);
                        break;
                }
            }
        }
    }

    @Override
    public void close() {
        program.terminate();
        System.out.println("Supervisor: Program has been terminated.");
    }
}
