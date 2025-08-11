# BaigiamasisDarbas

Užsakymų valdymo sistema

Ši aplikacija yra vidinė darbo užsakymų valdymo sistema, sukurta naudojant Java 21, Spring Boot, Thymeleaf ir PostgreSQL duomenų bazę.

## Funkcionalumas

- Saugus prisijungimas (Spring Security: ADMIN, USER)
- Užsakymų kūrimas ir peržiūra
- Darbų pradžios/pabaigos registravimas (Work Log)
- Brokų žymėjimas ir taisymas
- Trikalbystė (i18n) – LT/EN/UA
- Patogi web sąsaja (Thymeleaf + Bootstrap)
Aplikacijos paleidimas:

Paleisti pgAdmin 4 serveri.

Kad prisijungti prie aplikacijos dabar yra laikinai sukurti prisijungimai.

Username: admin
Password: admin

Username: worker
Password: worker

Username: client
Password: client


## Testavimui ne lokaliai.
Paleisti springboot aplikacija, tada i terminalą ivesti šia komanda:
C:\ngrok\ngrok.exe http 8080 norint prisijungti ne lokaliai.
ISPĖJIMAS: Naudojamas http, tai bandant atidaryti per naršykle rodis kad tai nėra saugus tinklapis
apsilankimui.

Kai užsikraus terminale ngrok, nukopijuok linka kurį gali įvesti, į crome naršyke
ar kita web brouse aplikacija.