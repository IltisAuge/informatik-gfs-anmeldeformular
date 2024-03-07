<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registrieren</title>
        <link rel="stylesheet" href="../../static/css/style.css">
    </head>
    <body>
        <div class="box">
            <h1 id="box-headline">Registrieren</h1>
            <div>
                ${alert}
            </div>
            <form action="/handle-register" method="POST">
                <input type="text" name="email" placeholder="E-Mail Adresse"><br>
                <input type="password" name="password" placeholder="Passwort"><br>
                <input type="text" name="name" placeholder="Vor- und Nachname"><br>
                <input type="text" name="class" placeholder="Klasse"><br>
                <input type="tel" name="phone-number" placeholder="Telefonnummer"><br>
                <input type="date" name="date-of-birth" placeholder="Geburtsdatum"><br>
                <input type="submit" value="Registrieren">
            </form>
            <p>Stattdessen <a href="/login">Anmelden</a></p>
        </div>
    </body>
</html>