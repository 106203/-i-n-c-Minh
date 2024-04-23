/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai7Lab5;

import java.util.Random;

/**
 *
 * @author Admin
 */
public class BankAll {
    double[] accounts;

    public BankAll(int n, double initBalance) {
        accounts = new double[n];
        for (int i = 0; i < n; i++) {
            accounts[i] = initBalance;
        }
    }

    public int size() {
        return accounts.length;
    }

    public synchronized double getTotalBalance() {
        double sum = 0;
        for (double a : accounts)
            sum += a;
        return sum;
    }

    public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
        while (accounts[from] < amount)
            wait();
        System.out.print(Thread.currentThread());
        accounts[from] -= amount;
        System.out.printf(" Chuyển %10.2f từ %d sang %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Tổng giá trị: %10.2f%n", getTotalBalance());
        notifyAll();
    }
}
class TransferMoney implements Runnable{
    Bank bank;
    int fromAcc;
    double maxAmount;
    int delay = 1000;

    public TransferMoney(Bank bank, int fromAcc, double maxAmount) {
        this.bank = bank;
        this.fromAcc = fromAcc;
        this.maxAmount = maxAmount;
    }

    @Override
    public void run() {
        Random rd = new Random();
        int toAcc = 0;
        double amount = 0;
        try {
            while (true) {
                do{
                    toAcc = rd.nextInt(bank.size());
                }while (toAcc == fromAcc);
                amount = rd.nextInt((int)maxAmount);
                bank.transfer(fromAcc, toAcc, amount);
                Thread.sleep(rd.nextInt(delay));
                }
            }
        catch (InterruptedException ex) {
            InterruptedException("Giao dịch chuyển tiền từ account " + fromAcc + "sang account" + toAcc + "bị gián đoạn");
        }
    }
}
class SynchBank
{
    public static void main(String[] args) {
        int n = 100;
        double initBalance = 1000;
        Bank bank = new Bank(n, initBalance);
        for (int i = 0; i < n; i++) {
            TransferMoney r = new TransferMoney(bank, i, initBalance);
            Thread t = new Thread(r);
            t.start();
        }
    }
}