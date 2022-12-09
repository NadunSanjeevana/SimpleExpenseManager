package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentAccountDAO implements AccountDAO {
    private final DataBaseHelper dbh;


    public PersistentAccountDAO(DataBaseHelper dbh){this.dbh = dbh;}
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
        return  returnAccount;
    }

    @Override
    public void addAccount(Account account) {
        dbh.addAccount(account);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(expenseType.equals("EXPENSE")){
            amount  = -1 * amount;
        }
        dbh.updateAccount(amount, accountNo);

    }
}
