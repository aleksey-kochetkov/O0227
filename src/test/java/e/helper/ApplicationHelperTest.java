package e.helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static e.helper.Assertions.assertHigher;

@SpringBootTest
public class ApplicationHelperTest {
    @Autowired
    private ApplicationContext ctx;
    
    @Test
    void whenGetPropertyAppNextId() {
        System.setProperty("app.store-executor-sleep", "0");
        ApplicationHelper.init(this.ctx);
        int expected = ApplicationHelper.getPropertyAppCurrentId();
        int actual = ApplicationHelper.getPropertyAppNextId();
        assertHigher(expected, actual);
        expected = actual;
        ApplicationHelper.init(this.ctx);
        assertEquals(expected, ApplicationHelper.getPropertyAppCurrentId());
    }
}
