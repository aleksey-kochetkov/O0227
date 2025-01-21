package e;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import e.helper.ObjectHelper;
import e.helper.ApplicationHelper;
import e.logic.Signer;

@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private Signer signer;
    
    @RequestMapping("sample")
    public String pageSample(Model model) {
        int id = ApplicationHelper.getPropertyAppNextId();
        model.addAttribute("id", id);
        LOGGER.info(String.format("app.current-id:%d", id));
        return "sample";
    }

    @RequestMapping("sign")
    public String pageSign() {
        signer.sign(ObjectHelper.getContent());
        return "sign";
    }
}
