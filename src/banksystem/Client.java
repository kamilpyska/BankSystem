/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

/**
 *
 * @author Kamil Pyska
 */
public class Client {
    //przed wybraniem u klienta sprawdzenie czy wieksze od zera
    //przed wpłatą też sprawdzić ^

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String token = null;
        String chosenKey = null;
        double value;
        boolean logOut;
        int accNumber = 0;
        int accPin = 0;

        try {
            System.out.println("List of registered REMOTE objects:");
            for (String s : Naming.list("//localhost")) {
                System.out.println(s);
            }

            System.out.println("Getting registry");
            Registry reg = LocateRegistry.getRegistry("localhost");
            System.out.println("Getting interface");
            IBankingService iBankingService = (IBankingService) reg.lookup("MyBankingService");
            System.out.println("Got interface");

            while (true) {
                System.out.println("\nPodaj numer swojego konta: ");
                accNumber = scanner.nextInt();
                System.out.println("Podaj numer Pin: ");
                accPin = scanner.nextInt();

                token = iBankingService.authorize(accNumber, accPin);
                if (token != null) {
                    System.out.println("Zalogowano.");
                    logOut=true;
                    while (logOut) {
                        System.out.println("\nWybierz działanie naciskając odpowiadający mu klawisz: "
                                + "\n1 - sprawdź saldo."
                                + "\n2 - wpłać pieniądze."
                                + "\n3 - wypłać pieniądze."
                                + "\n4 - wykonaj przelew."
                                + "\n5 - wyloguj.");
                        chosenKey = scanner.next();

                        switch (chosenKey) {
                            case "1":
                                System.out.println("Saldo Twojego konta: " + iBankingService.getBalance(token) + " zł.");
                                break;
                            case "2":
                                System.out.println("Jaką kwotę chcesz wpłacić?");
                                value = scanner.nextDouble();
                                iBankingService.deposit(token, value);
                                System.out.println("Zdeponowano: " + value + "zł.");
                                break;
                            case "3":
                                System.out.println("Jaką kwotę chcesz wypłacić?");
                                value = scanner.nextDouble();
                                if (iBankingService.withdraw(token, value)) {
                                    System.out.println("Wypłacono " + value + "zł.");
                                } else {
                                    System.out.println("Za mało środków lub podano niewłaściwą kwotę.");
                                }
                                break;
                            case "4":
                                System.out.println("Podaj numer konta do przelewu:");
                                int nr = scanner.nextInt();
                                if (nr != accNumber) {
                                    System.out.println("Podaj kwotę do przelewu:");
                                    value = scanner.nextDouble();
                                    if (iBankingService.withdraw(token, nr, value)) {
                                        System.out.println("Przelano " + value + "zł na konto nr " + nr + ".");
                                    } else {
                                        System.out.println("Nieprawidłowy numer konta lub wartość przelewu.");
                                    }
                                } else {
                                    System.out.println("Zły numer konta. \nDo przelewu na własne konto służy opcja 'wpłać pieniądze'.");
                                }
                                break;
                            case "5":
                                iBankingService.bye(token);
                                logOut = false;
                                System.out.println("Wylogowano.");
                                break;
                            default:
                                System.out.println("Nieprawidłowy klawisz. Spróbuj jeszcze raz.");
                                break;
                        }
                    }
                } else {
                    System.out.println("\nDane niepoprawne.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
