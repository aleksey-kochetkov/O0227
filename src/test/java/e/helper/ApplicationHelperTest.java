package e.helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationHelperTest {
    @Test
    void whenGetPropertyAppNextId() {
        int expected = ApplicationHelper.getPropertyAppCurrentId();
        Assertions.assertHigher(expected, ApplicationHelper.getPropertyAppNextId());
    }
}
