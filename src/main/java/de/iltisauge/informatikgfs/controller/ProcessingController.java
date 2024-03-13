package de.iltisauge.informatikgfs.controller;

import de.iltisauge.informatikgfs.AccountDatabase;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Daniel Ziegler
 */
@Controller
public class ProcessingController {

    @PostMapping("/handle-login")
    public RedirectView handleLogin(HttpServletRequest request,
                                RedirectAttributes redirAttr,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "password") String password) {
        // Passwort hashen
        final String passwordSHA256 = sha256(password);
        final Account account = AccountDatabase.getInstance().getAccount(email);
        // Überprüfen, ob Konto existiert und ob die Passwörter übereinstimmen
        if (account != null && account.getPasswordSHA256().equals(passwordSHA256)) {
            // User-Attribut im Session-Objekt setzen
            request.getSession().setAttribute("user", account);
            // Account-Objekt an nächsten View übergeben
            redirAttr.addFlashAttribute("account", account);
            return new RedirectView("/welcome");
        }
        // Text an Login-View übergeben
        redirAttr.addFlashAttribute("alert", "<p>Ungültige Anmeldedaten!</p>");
        return new RedirectView("/login");
    }

    @PostMapping("/handle-register")
    public RedirectView handleRegister(RedirectAttributes redirAttr,
                       @RequestParam(name = "email") String email,
                       @RequestParam(name = "password") String password,
                       @RequestParam(name = "name") String name,
                       @RequestParam(name = "class") String clazz,
                       @RequestParam(name = "phone-number") String phoneNumber,
                       @RequestParam(name = "date-of-birth") String dateOfBirth)
                    throws ParseException {
        // Konto aus Datenbank oder Cache laden
        final Account account = AccountDatabase.getInstance().getAccount(email);
        if (account != null) {
            /* Konto mit dieser E-Mail existiert bereits
                -> Text an Registrierungs-View übergeben
             */
            redirAttr.addFlashAttribute("alert",
                    "<p>Ein Konto mit dieser E-Mail-Adresse existiert bereits!</p>");
            return new RedirectView("/register");
        }
        // Passwort hashen
        final String passwordSHA256 = sha256(password);
        // In der Anfrage enthaltenes Datum hat das Format: 2024-03-13
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Datum-String in Java-Datum umwandeln
        final Date javaDate = dateFormat.parse(dateOfBirth);
        if (AccountDatabase.getInstance().
                createAccount(email, passwordSHA256,
                            name, clazz, phoneNumber, javaDate)) {
            // createAccount hat "true" zurückgegeben -> Kein Fehler
            // Text an Login-View übergeben
            redirAttr.addFlashAttribute("alert",
                    "<p>Konto wurde erstellt. Bitte anmelden!</p>");
            return new RedirectView("/login");
        }
        // createAccount hat "false" zurückgegeben -> Fehler!
        // Text an Registrierungs-View übergeben
        redirAttr.addFlashAttribute("alert",
                "<p>Fehler! Konto wurde nicht erstellt.</p>");
        return new RedirectView("/register");
    }

    @PostMapping("/delete-account")
    public RedirectView deleteAccount(HttpServletRequest request, RedirectAttributes redirAttr) {
        // Überprüfen, ob Client angemeldet ist
        if (!GetViewController.isLoggedIn(request)) {
            // Wenn nicht, auf die Login-Seite weiterleiten
            return new RedirectView("/login");
        }
        // Account-Objekt aus dem Session-Objekt laden
        final Account account = (Account) request.getSession().getAttribute("user");
        if (AccountDatabase.getInstance().deleteAccount(account.getEmail())) {
            // deleteAccount hat "true" zurückgegeben -> Kein Fehler
            // Text an Login-View übergeben
            redirAttr.addFlashAttribute("alert", "<p>Konto wurde gelöscht.</p>");
            return new RedirectView("/login");
        }
        // deleteAccount hat "false" zurückgegeben -> Fehler!
        // Text an Willkommen-View übergeben
        redirAttr.addFlashAttribute("alert", "<p>Fehler! Konto wurde nicht gelöscht.</p>");
        return new RedirectView("/welcome");
    }

    private String sha256(String input) {
        // Übergabewert "input" in SHA-256-Hash umwandelt
        return new DigestUtils("SHA3-256").digestAsHex(input);
    }
}
