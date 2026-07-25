package e.helper;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class WaitingFileInputStream extends InputStream {
    private Path file;
    private WatchService watchService;
    private FileInputStream wrapped;

    public WaitingFileInputStream(Path file) throws IOException {
        this.file = file;
        watchService = FileSystems.getDefault().newWatchService();
        this.file.toAbsolutePath().getParent().register(watchService,
                           ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
    }

    @Override
    public void close() throws IOException {
        watchService.close();
    }

    @Override
    public int read() throws IOException {
        if (wrapped == null) {
            take();
        }
        return wrapped.read();
    }

    private void take() throws IOException {
TAKE:   while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException exc) {
                throw new IOException(exc);
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                if (file.equals((Path)event.context())) {
                    break TAKE;
                }
            }
        }
        wrapped = new FileInputStream(file.toFile());
    }
}
