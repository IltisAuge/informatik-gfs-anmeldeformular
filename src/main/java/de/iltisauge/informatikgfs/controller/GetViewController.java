package de.iltisauge.informatikgfs.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Daniel Ziegler
 */
@Controller
public class GetViewController {

    @GetMapping("/")
    public RedirectView getDefaultPage(HttpServletRequest request) {
        /*
        Wenn Client bereits angemeldet, Willkommensseite aufrufen,
        ansonsten die Login-Seite
         */
        return new RedirectView(isLoggedIn(request) ? "welcome" : "login");
    }

    @GetMapping("/register")
    public String getRegisterView() {
        // Datei registerView.jsp wird als View zurückgegeben
        return "registerView";
    }

    @GetMapping("/login")
    public String getLoginView() {
        return "loginView";
    }

    @GetMapping("/welcome")
    public Object getWelcomeView(Model model, HttpServletRequest request) {
        // Überprüfen, ob Client angemeldet ist
        if (!isLoggedIn(request)) {
            // Wenn nicht, auf Login-Seite weiterleiten
            return new RedirectView("/login");
        }
        // Ansonsten auf Willkommensseite weiterleiten
        // User attribute will be available via the session object
        return "welcomeView";
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        // Überprüfen, ob das User-Attribut im Session-Objekt gesetzt wurde
        return request.getSession().getAttribute("user") != null;
    }
}
