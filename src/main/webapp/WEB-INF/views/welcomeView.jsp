<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Willkommen</title>
        <link rel="stylesheet" href="../../static/css/style.css">
    </head>
    <body>
        <div>
            <h1>Willkommen ${user.name}</h1>
            <h4>E-Mail Adresse: ${user.email}</h4>
            <h4>Klasse: ${user.clazz}</h4>
            <h4>Telefonnummer: ${user.phoneNumber}</h4>
            <h4>
                Geburtsdatum:
                <span id="date-of-birth"></span>
            </h4>
            <h4>
                Ihr Alter beträgt
                <span id="age" style="color: green"></span>
                Jahre.
            </h4>
            <h4>
                Volljährig:
                <span id="full-age"></span>
            </h4>
            <form action="/delete-account" method="POST">
                <input id="delete-account-btn" type="submit"
                       value="Konto und alle Daten löschen">
            </form>
        </div>
        <script>
            const date_of_birth = new Date(${user.dateOfBirth.time});
            const current_date = new Date();

            // Geburtsdatum formatieren und in vorhergesehenes Feld einfügen
            const date_of_birth_element = document.getElementById('date-of-birth');
            date_of_birth_element.innerHTML = date_of_birth.toLocaleDateString("de-DE");

            // Alter berechnen und in vorhergesehenes Feld einfügen
            const age_element = document.getElementById('age');
            // Schaltjahre werden nicht berücksichtigt!
            const difference = current_date.getTime() - date_of_birth.getTime();
            const age = difference / (1000 * 60 * 60 * 24 * 365);
            age_element.innerHTML = Math.floor(age);

            // Ausgabe, ob der Benutzer volljährig ist
            const full_age_element = document.getElementById('full-age');
            if (age < 18) {
                full_age_element.innerHTML = 'Nein';
            } else {
                full_age_element.innerHTML = 'Ja';
            }
        </script>
    </body>
</html>