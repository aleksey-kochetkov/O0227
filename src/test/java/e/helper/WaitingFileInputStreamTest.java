package e.helper;

import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WaitingFileInputStreamTest {

    @Test
    void whenRead() throws IOException {
        Path delmiFil = Paths.get("name.test");
        Thread main = Thread.currentThread();
        Thread thread = new Thread(rethrow(() -> {
                  while (main.getState().equals(Thread.State.RUNNABLE)) {}
                  createFile(delmiFil, "a");}));
        InputStream in = new WaitingFileInputStream(delmiFil);
        thread.start();
        int actual = in.read();
        in.close();
        assertEquals('a', actual);
    }

    @Test
    void whenWrongEvent() throws IOException {
        Path delmiFil = Paths.get("name.test");
        Thread main = Thread.currentThread();
        Thread thread = new Thread(rethrow(() -> {
                  while (main.getState().equals(Thread.State.RUNNABLE)) {}
                  createFile(Paths.get("wrong.test"), null);
                  main.interrupt();}));
        thread.start();
        try (InputStream in = new WaitingFileInputStream(delmiFil)) {
            assertThrows(IOException.class, ()
            -> in.read());
        }
    }

    private void createFile(Path file, String message) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(file.toFile()))) {
            if (message != null) {
                out.write(message.getBytes());
            }
        }
    }

    interface RunnableWithException {
        void run() throws Exception;
    }

    private Runnable rethrow(RunnableWithException runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }};
    }
}