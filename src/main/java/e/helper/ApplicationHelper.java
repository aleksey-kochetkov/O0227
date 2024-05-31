package e.helper;

import java.io.File;
import java.io.FileInputStream;
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
    private static Integer tmp;
    
    static {
        init();
    }
    
    public static void init() {
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
    }
    
    private static boolean isTest() {
        return false;
    }
    
    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
        setApplicationContext_internal(ctx);
    }
    
    public static int getPropertyAppCurrentId() {
        return AppGenerator.get();
    }
    
    public static int getPropertyAppNextId() {
        return AppGenerator.generate();
    }
}
