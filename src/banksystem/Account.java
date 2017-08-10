/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem;

/**
 *
 * @author Kamil Pyska
 */
public class Account {

    private final int number;
    private final int pin;
    private double balance;

    public Account(int _number, int _pin, double _balance) {
        this.number = _number;
        this.pin = _pin;
        this.balance = _balance;
    }

    public int getNumber() {
        return this.number;
    }

    public int getPin() {
        return this.pin;
    }

    public double getBalance() {
        return this.balance;
    }

    public void deposit(double value) {
        this.balance += value;
    }

    public Boolean withdraw(double value) {
        if (value > this.balance || value <= 0) {
            return false;
        } else {
            this.balance -= value;
            return true;
        }
    }
}
