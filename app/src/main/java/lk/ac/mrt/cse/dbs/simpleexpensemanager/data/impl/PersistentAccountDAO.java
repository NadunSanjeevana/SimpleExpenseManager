package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final DataBaseHelper dbh;


    public PersistentAccountDAO(DataBaseHelper dbh) {
        this.dbh = dbh;
    }

    @Override
    public List<String> getAccountNumbersList() throws ParseException {

        List<String> accountNumList;
        accountNumList = dbh.getEveryAccountnumber();
        return accountNumList;
    }

    @Override
    public List<Account> getAccountsList() throws ParseException {
        List<Account> accountList;
        accountList = dbh.getEveryAccount();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account returnAccount;
        returnAccount = dbh.getAccount(accountNo);
        return returnAccount;
    }

    @Override
    public void addAccount(Account account) {
        dbh.addAccount(account);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbh.deleteAccount(accountNo);
    }


    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account newacc = dbh.getAccount(accountNo);
        Double balance = newacc.getBalance();
        if (expenseType == ExpenseType.EXPENSE) {
            if(balance >= amount) {
                balance = balance-amount;
            }
            else{
                dbh.deleteLastTransaction();
                throw new InvalidAccountException("Insufficient Account Balance");
            }

        }
        else{balance = amount + balance; }
        dbh.updateAccount(balance, accountNo);

    }
}

