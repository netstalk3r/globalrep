package spring.cloud.gateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// TODO should be available in next release
// TODO https://github.com/spring-projects/spring-framework/issues/21922
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = { "/index", "/" })
    public String index() {
        return "index";
    }

}
