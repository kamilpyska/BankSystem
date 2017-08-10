/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *
 * @author Kamil Pyska
 */
public class BankingService extends UnicastRemoteObject
        implements IBankingService {

    private ArrayList<Account> accountList;
    private Map<String, Account> hashMap;
    private Random rand;
    private String aToZ;

    public BankingService(ArrayList accList) throws RemoteException {
        this.accountList = accList;
        this.hashMap = new HashMap<String, Account>();
        this.rand = new Random();
        this.aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    }

    private String generateToken(String aToZ) {
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            rs.append(aToZ.charAt(randIndex));
        }
        return rs.toString();
    }

    public String authorize(int accountNumber, int pin) throws RemoteException {
        String stringToReturn;
        Account usedAccount = null;
        for (Account acc : this.accountList) {
            if (acc.getNumber() == accountNumber) {
                if (acc.getPin() == pin) {
                    usedAccount = acc;
                }
            }
        }

        if (usedAccount != null) {
            stringToReturn = generateToken(aToZ);
            this.hashMap.put(stringToReturn, usedAccount);
        } else {
            stringToReturn = null;
        }
        return stringToReturn;
    }

    public double getBalance(String token) throws RemoteException {
        Account account = this.hashMap.get(token);
        return account.getBalance();
    }

    public void deposit(String token, double value) throws RemoteException {
        Account account = this.hashMap.get(token);
        account.deposit(value);
    }

    public boolean withdraw(String token, double value) throws RemoteException {
        Account account = this.hashMap.get(token);
        return account.withdraw(value);
    }

    public boolean withdraw(String token, int accountToTransferNumber, double value) throws RemoteException {
        Account accToWithdraw = this.hashMap.get(token);
        Boolean withdrawFinalized = false;

        for (Account acc : this.accountList) {
            if (acc.getNumber() == accountToTransferNumber) {
                withdrawFinalized = accToWithdraw.withdraw(value);
                if (withdrawFinalized) {
                    acc.deposit(value);
                }
            }
        }
        return withdrawFinalized;
    }

    public void bye(String token) throws RemoteException {
        hashMap.remove(token);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList accountsList = new ArrayList();
        accountsList.add(new Account(111, 123, 100));
        accountsList.add(new Account(222, 123, 100));
        accountsList.add(new Account(333, 123, 100));

        try {
            BankingService bankingService = new BankingService(accountsList);
            UnicastRemoteObject.unexportObject(bankingService, true);
            System.out.println("Exporting...");
            IBankingService bsStub = (IBankingService) UnicastRemoteObject.exportObject(bankingService, 0);

            Registry registry;
            if (true) {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("New registry created on localhost");
            } else {
                System.out.println("Locating registry...");
                registry = LocateRegistry.getRegistry("localhost");
                System.out.println("Registry located");
            }

            registry.rebind("MyBankingService", bsStub);
            System.out.println("IBankingService bound, ready");
        } catch (Exception e) {
            System.err.println("IBankingService exception:");
            e.printStackTrace();
        }
    }
}
