package e.helper;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ObjectHelper {
    private static final Logger LOGGER =
                             LoggerFactory.getLogger(ObjectHelper.class);
    private static final String FILE = "in.fil";

    public static InputStream getContent() {
        try {
            return new BufferedInputStream(new FileInputStream(FILE));
        } catch (FileNotFoundException exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
            throw new RuntimeException(exception);
        }
    }
}
