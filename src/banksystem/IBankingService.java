package banksystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.*;

/**
 *
 * @author Kamil Pyska
 */
public interface IBankingService extends Remote {

    public String authorize(int account, int pin) throws RemoteException;

    public double getBalance(String token) throws RemoteException;

    public void deposit(String token, double value) throws RemoteException;

    public boolean withdraw(String token, double value) throws RemoteException;

    public boolean withdraw(String token, int account, double value) throws RemoteException;

    public void bye(String token) throws RemoteException;
}
