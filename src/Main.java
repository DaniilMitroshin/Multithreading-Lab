import static java.lang.Thread.currentThread;

public class Main {
    public static void main(String[] args) {
        AbstractProgram program = new AbstractProgram(200);

        try (Supervisor supervisor = new Supervisor(program)) {
            supervisor.start();
            program.start();

            program.join();//приостанавливаем главный поток
            supervisor.join();
        } catch (final InterruptedException interruptedException) {
            currentThread().interrupt();
        }

        System.out.println("The program has completed its work.");
    }
}