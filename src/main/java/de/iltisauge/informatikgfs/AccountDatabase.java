package de.iltisauge.informatikgfs;

import de.iltisauge.informatikgfs.controller.Account;
import de.iltisauge.databaseapi.Credential;
import de.iltisauge.databaseapi.databases.MySQLDatabase;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountDatabase extends MySQLDatabase {

    private final Map<String, Account> accountCache = new HashMap<>();
    private static AccountDatabase INSTANCE;

    public static void main(String[] args) {
        // Testing
        Account a = getInstance().getAccount("danielziegler.1@gmx.de");
        System.out.println(a == null ? "null" : a.toString());
    }

    public AccountDatabase() {
        super(new Credential("127.0.0.1", 3306, "informatik-gfs", "root", ""));
    }

    public static AccountDatabase getInstance() {
        if (INSTANCE == null) {
            // Load MySQL Driver class
            try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException exception) {
				exception.printStackTrace();
			}
            INSTANCE = new AccountDatabase();
        }
        INSTANCE.tryToConnect();
        return INSTANCE;
    }

    /**
     *
     * @param email
     * @return If an account is found,
     * a new {@link Account} object, otherwise null
     */
    public Account getAccount(String email) {
        Account account = null;
        if ((account = accountCache.get(email)) != null) {
            return account;
        }
        final ResultSet result = getResultAsync("SELECT * FROM `accounts`" +
                                                " WHERE `email` = ?", email);
        try {
            if (result.next()) {
                final String passwordSHA256 = result.getString("passwordSHA256");
                final String name = result.getString("name");
                final String clazz = result.getString("class");
                final String phoneNumber = result.getString("phoneNumber");
                final Date dateOfBirth = result.getDate("dateOfBirth");
                account = new Account(email, passwordSHA256,
                        name, clazz, phoneNumber,
                        new java.util.Date(dateOfBirth.getTime()));
                accountCache.put(email, account);
                return account;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param email
     * @param passwordSHA256
     * @return If email and password are valid,
     * a new {@link Account} object, otherwise null
     */
    /*public Account getAccount(String email, String passwordSHA256) {
        Account account = null;
        if ((account = accountCache.get(email)) != null
                && account.passwordSHA256().equals(passwordSHA256)) {
            return account;
        }
        System.out.println("Fetch account from DB");
        final ResultSet result = getResultAsync("SELECT * FROM `accounts`" +
                        " WHERE `email` = ?" +
                        " AND `passwordSHA256` = ?;",
                        email, passwordSHA256);
        try {
            if (result.next()) {
                final String name = result.getString("name");
                final String clazz = result.getString("class");
                final String phoneNumber = result.getString("phoneNumber");
                final Date dateOfBirth = result.getDate("dateOfBirth");
                account = new Account(email, passwordSHA256,
                        name, clazz, phoneNumber,
                        new java.util.Date(dateOfBirth.getTime()));
                accountCache.put(email, account);
                return account;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }*/

    public boolean createAccount(String email, String passwordSHA256,
                                 String name, String clazz,
                                 String phoneNumber, java.util.Date dateOfBirth) {
        accountCache.put(email, new Account(email, passwordSHA256,
                                    name, clazz, phoneNumber,
                                    new java.util.Date(dateOfBirth.getTime())));
        return executeAsync("INSERT INTO `accounts`" +
                        " (`email`, `passwordSHA256`, `name`," +
                        " `class`, `phoneNumber`, `dateOfBirth`)" +
                        " VALUES (?, ?, ?, ?, ?, ?);",
                email, passwordSHA256, name, clazz,
                phoneNumber, new Date(dateOfBirth.getTime()));
    }

    public boolean deleteAccount(String email) {
        accountCache.remove(email);
        return executeAsync("DELETE FROM `accounts` WHERE `email` = ?;", email);
    }
}
