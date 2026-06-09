Main - Odpowiada za punkt wejścia aplikacji oraz pętlę menu konsolowego zbierającego dane od użytkownika.

Student - Klasa reprezentująca dane studenta (ID, imię, grupa) wraz z licznikiem punktów lojalnościowych.

Equipment - Abstrakcyjna klasa bazowa definiująca podstawowe cechy sprzętu (cenę bazową, status dostępności).

LaptopSet i CameraKit - Klasy dziedziczące po Equipment. Specyficzne algorytmy obliczania ceny dziennej ze względu na dodatkow wyposażenie.

Reservation - Klasa wiążąca studenta ze sprzętem na określony czas, przechowująca stan rezerwacji oraz ich finalny koszt.

ReservationService - Serwis biznesowy zarządzający kolekcjami obiektów.

LoyaltyDiscountPolicy - Klasa implementująca interfejs polityki rabatowej, naliczająca zniżkę na podstawie punktów studenta.
