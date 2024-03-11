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
