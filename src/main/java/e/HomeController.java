package e;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import e.helper.ApplicationHelper;

@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    
    @GetMapping
    public String get() {
        LOGGER.info(String.format("app.current-id:%d",
                              ApplicationHelper.getPropertyAppNextId()));
        return "sample";
    }
}
