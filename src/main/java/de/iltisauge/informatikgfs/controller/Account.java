package de.iltisauge.informatikgfs.controller;

import java.util.Date;

/**
 * @author Daniel Ziegler
 */
public record Account(String email,
                      String passwordSHA256,
                      String name,
                      String clazz,
                      String phoneNumber,
                      Date dateOfBirth) {

    /*
    get-Methoden für die einzelnen Attribute
    Diese Methoden müssen erstellt werden, damit über JSP zugegriffen werden kann
     */

    public String getEmail() {
        return email;
    }

    public String getPasswordSHA256() {
        return passwordSHA256;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
