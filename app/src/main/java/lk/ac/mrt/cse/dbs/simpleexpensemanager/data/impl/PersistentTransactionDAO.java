package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DataBaseHelper db;

    public PersistentTransactionDAO(DataBaseHelper db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction newTransaction = new Transaction(date,accountNo,expenseType,amount);
        db.addTransation(newTransaction);


    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList;
        transactionList = db.readTransactions(0);
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionpagingList;
        //transactionpagingList = db.readTransactions(limit);
        transactionpagingList = db.lastval(limit);
        return transactionpagingList;
    }
}
