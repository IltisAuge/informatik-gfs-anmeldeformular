<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Anmelden</title>
        <link rel="stylesheet" href="../../static/css/style.css">
    </head>
    <body>
        <div class="box">
            <h1 class="box-headline">Anmelden</h1>
            <div>
                ${alert}
            </div>
            <form action="/handle-login" method="POST">
                <input type="text" name="email" placeholder="E-Mail Adresse"><br>
                <input type="password" name="password" placeholder="Passwort"><br>
                <input type="submit" value="Anmelden">
            </form>
            <p>Stattdessen <a href="/register">Registrieren</a></p>
        </div>
    </body>
</html>