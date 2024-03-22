package e.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ApplicationHelper {
    private static Integer tmp;
    
    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
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
    
    public static int getPropertyAppCurrentId() {
        return AppGenerator.get();
    }
}
