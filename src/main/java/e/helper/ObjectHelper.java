package e.helper;

import java.io.FileInputStream;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ObjectHelper {
    private static final Logger LOGGER =
                             LoggerFactory.getLogger(ObjectHelper.class);
    private static final String FILE = "in.fil";

    public static byte[] getContent() {
        try {
            return new FileInputStream(FILE).readAllBytes();
        } catch (IOException exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
            throw new RuntimeException(exception);
        }
    }
}
