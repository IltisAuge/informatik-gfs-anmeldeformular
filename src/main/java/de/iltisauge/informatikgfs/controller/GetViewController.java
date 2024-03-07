package de.iltisauge.informatikgfs.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GetViewController {

    @GetMapping("/")
    public RedirectView getDefaultPage(HttpServletRequest request) {
        return new RedirectView(isLoggedIn(request) ? "welcome" : "login");
    }

    @GetMapping("/register")
    public String getRegisterView() {
        return "registerView";
    }

    @GetMapping("/login")
    public String getLoginView() {
        return "loginView";
    }

    @GetMapping("/welcome")
    public Object getWelcomeView(Model model, HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            return new RedirectView("/login");
        }
        // User attribute will be available via the session object
        return "welcomeView";
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }
}
