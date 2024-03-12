package de.iltisauge.informatikgfs;

import de.iltisauge.informatikgfs.controller.Account;
import de.iltisauge.databaseapi.Credential;
import de.iltisauge.databaseapi.databases.MySQLDatabase;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Ziegler
 */
public class AccountDatabase extends MySQLDatabase {

    // Lokaler Zwischenspeicher für Account-Objekte
    private final Map<String, Account> accountCache = new HashMap<>();
    private static AccountDatabase INSTANCE;

    public AccountDatabase() {
        // Adresse und Anmeldedaten für die Datenbank an Unterklasse übergeben
        super(new Credential("127.0.0.1", 3306, "informatik-gfs", "root", ""));
    }

    public static AccountDatabase getInstance() {
        if (INSTANCE == null) {
            // MySQL-Driver Klasse laden
            try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException exception) {
				exception.printStackTrace();
			}
            // Neue Instanz der AccountDatabase-Klasse zuweisen
            INSTANCE = new AccountDatabase();
        }
        // Zur Datenbank verbinden, falls noch keine Verbindung besteht
        INSTANCE.tryToConnect();
        return INSTANCE;
    }

    /**
     *
     * @param email E-Mail-Adresse
     * @return Wenn ein Konto gefunden wurde, das {@link Account}-Objekt
     * ansonsten <code>null</code>
     */
    public Account getAccount(String email) {
        Account account = null;
        /*
        Überprüfen, ob ein Account-Objekt mit dieser
        E-Mail-Adresse im Cache vorhanden ist.
         */
        if ((account = accountCache.get(email)) != null) {
            return account;
        }
        /*
        Nach Datensatz mit dieser E-Mail-Adresse in der Datenbank suchen
         */
        final ResultSet result = getResultAsync("SELECT * FROM `accounts`" +
                                                " WHERE `email` = ?", email);
        try {
            // Wenn kein Fehler aufgetreten ist und ein Datensatz gefunden wurde..
            if (result != null && result.next()) {
                // Einzelne Attribute aus Datensatz laden
                final String passwordSHA256 = result.getString("passwordSHA256");
                final String name = result.getString("name");
                final String clazz = result.getString("class");
                final String phoneNumber = result.getString("phoneNumber");
                final Date dateOfBirth = result.getDate("dateOfBirth");
                // Neues Account-Objekt mit geladenen Daten erstellen
                account = new Account(email, passwordSHA256,
                        name, clazz, phoneNumber,
                        new java.util.Date(dateOfBirth.getTime()));
                // Objekt in Zwischenspeicher legen
                accountCache.put(email, account);
                return account;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public boolean createAccount(String email, String passwordSHA256,
                                 String name, String clazz,
                                 String phoneNumber, java.util.Date dateOfBirth) {
        // Datensatz in Datenbank einfügen
        final boolean success = executeAsync("INSERT INTO `accounts`" +
                        " (`email`, `passwordSHA256`, `name`," +
                        " `class`, `phoneNumber`, `dateOfBirth`)" +
                        " VALUES (?, ?, ?, ?, ?, ?);",
                email, passwordSHA256, name, clazz,
                phoneNumber, new Date(dateOfBirth.getTime()));
        if (success) {
            // Wenn Einfügen erfolgreich, neues Account-Objekt in Cache speichern
            accountCache.put(email, new Account(email, passwordSHA256,
                    name, clazz, phoneNumber, dateOfBirth));
        }
        return success;
    }

    public boolean deleteAccount(String email) {
        // Datensatz mit gegebener E-Mail-Adresse aus Datenbank löschen
        final boolean success = executeAsync("DELETE FROM `accounts` WHERE `email` = ?;",
                                email);
        if (success) {
            // Wenn Löschen erfolgreich, Account-Objekt aus Cache entfernen
            accountCache.remove(email);
        }
        return success;
    }
}
