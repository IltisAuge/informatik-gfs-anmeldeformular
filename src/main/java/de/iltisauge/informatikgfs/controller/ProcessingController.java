package de.iltisauge.informatikgfs.controller;

import de.iltisauge.informatikgfs.AccountDatabase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ProcessingController {

    @PostMapping("/handle-login")
    public RedirectView handleLogin(HttpServletRequest request,
                                RedirectAttributes redirAttr,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "password") String password)
            throws NoSuchAlgorithmException {
        // Hash password in SHA256
        final String passwordSHA256 = sha256(password);
        final Account account = AccountDatabase.getInstance().getAccount(email);
        if (account != null && account.getPasswordSHA256().equals(passwordSHA256)) {
            request.getSession().setAttribute("user", account);
            redirAttr.addFlashAttribute("account", account);
            return new RedirectView("/welcome");
        }
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
            throws NoSuchAlgorithmException, ParseException {
        final Account account = AccountDatabase.getInstance().getAccount(email);
        if (account != null) {
            redirAttr.addFlashAttribute("alert",
                    "<p>Ein Konto mit dieser E-Mail Adresse existiert bereits!</p>");
            return new RedirectView("/register");
        }
        // Hash password in SHA256
        final String passwordSHA256 = sha256(password);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date javaDate = dateFormat.parse(dateOfBirth);
        if (AccountDatabase.getInstance().
                createAccount(email, passwordSHA256,
                            name, clazz, phoneNumber, javaDate)) {
            redirAttr.addFlashAttribute("alert",
                    "<p>Konto wurde erstellt. Bitte anmelden!</p>");
            return new RedirectView("/login");
        }
        redirAttr.addFlashAttribute("alert",
                "<p>Fehler! Konto wurde nicht erstellt.</p>");
        return new RedirectView("/register");
    }

    @PostMapping("/delete-account")
    public RedirectView deleteAccount(HttpServletRequest request, RedirectAttributes redirAttr) {
        if (!GetViewController.isLoggedIn(request)) {
            return new RedirectView("/login");
        }
        final Account account = (Account) request.getSession().getAttribute("user");
        if (AccountDatabase.getInstance().deleteAccount(account.getEmail())) {
            redirAttr.addFlashAttribute("alert", "<p>Konto wurde gelöscht.</p>");
            return new RedirectView("/login");
        }
        redirAttr.addFlashAttribute("alert", "<p>Fehler! Konto wurde nicht gelöscht.</p>");
        return new RedirectView("/welcome");
    }

    private String sha256(String input) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        // Decoding
        final BigInteger bigInt = new BigInteger(1, hashBytes);
        String hash = bigInt.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }
}
