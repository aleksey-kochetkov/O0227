package e.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Component
public class ApplicationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHelper.class);
    private static final String NAME = isTest() ? "O0227test.properties" : "O0227.properties";
    private static final MultiRequestableOnceExecutor STORE_EXECUTOR =
          new MultiRequestableOnceExecutor(ApplicationHelper::storeFile);
    private static Integer tmp;
    private static Integer storeExecutorSleep;

    static {
        init();
    }

    public static void init() {
        STORE_EXECUTOR.waitIfBusy();
        String value = System.getProperty("app.store-executor-sleep");
        if (value != null) {
            setStoreExecutorSleep(value);
        }
        Properties file = new Properties();
        try (FileInputStream in = new FileInputStream(NAME)) {
            file.load(in);
            tmp = Integer.parseInt(file.getProperty("app.current-id"));
        } catch (Throwable exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
            try (FileInputStream in = new FileInputStream(
                                     System.getProperty("java.io.tmpdir")
                                          + File.separatorChar + NAME)) {
                file.load(in);
                tmp = Integer.parseInt(file.getProperty("app.current-id"));
            } catch (Throwable exc) {
                LOGGER.error(StringHelper.getStackTrace(exc));
            }
        }
    }

    public static void init(ApplicationContext ctx) {
        init();
        setApplicationContext_internal(ctx);
    }

    private static void setApplicationContext_internal(ApplicationContext ctx) {
        if (tmp == null) {
            try {
                tmp = Integer.parseInt(ctx.getEnvironment()
                                    .getProperty("app.current-id"));
            } catch (NumberFormatException ignored) {
                LOGGER.error(StringHelper.getStackTrace(ignored));
            }
        }
        AppGenerator.init(tmp);
        tmp = null;
        if (storeExecutorSleep == null) {
            String value = ctx.getEnvironment()
                                .getProperty("app.store-executor-sleep");
            if (value != null) {
                setStoreExecutorSleep(value);
            }
        }
    }

    private static void setStoreExecutorSleep(String value) {
        STORE_EXECUTOR.setSleep(storeExecutorSleep = Integer.parseInt(value));
    }

    private static boolean isTest() {
        return false;
    }

    private static void storeFile() {
        Properties file = new Properties();
        file.setProperty("app.current-id", Objects.toString(getPropertyAppCurrentId()));
        try (FileOutputStream out = new FileOutputStream(NAME)) {
            file.store(out, null);
        } catch (Throwable exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
            try (FileOutputStream out = new FileOutputStream(
                                     System.getProperty("java.io.tmpdir")
                                          + File.separatorChar + NAME)) {
                file.store(out, null);
            } catch (Throwable exc) {
                LOGGER.error(StringHelper.getStackTrace(exc));
            }
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            LOGGER.error(StringHelper.getStackTrace(exception));
        }
    }

    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
        setApplicationContext_internal(ctx);
    }

    public static int getPropertyAppCurrentId() {
        return AppGenerator.get();
    }

    public static int getPropertyAppNextId() {
        int result = AppGenerator.generate();
        STORE_EXECUTOR.request();
        return result;
    }
}
