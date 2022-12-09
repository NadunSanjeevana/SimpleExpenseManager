package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DataBaseHelper extends SQLiteOpenHelper {

    String account_table = "ACCOUNT_TABLE";
    String Transaction_table = "LOG_TABLE";
    String account_no = "Account_No";
    String bank = "Bank";
    String acc_holder = "Acc_Holder";
    String initial_balance = "Initial_Balance";
    String log_id = "Log_ID";
    String type = "Type";
    String date = "Date";
    String amount = "Amount";


    public DataBaseHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String createAccountTableStatement = "CREATE TABLE " + account_table + "( " + account_no + " TEXT PRIMARY KEY, " + bank + " TEXT, " + acc_holder + " TEXT, " + initial_balance + " REAL);";
        sqLiteDatabase.execSQL(createAccountTableStatement);



        String createLogTableStatement = "CREATE TABLE " + Transaction_table + "( " + log_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + account_no + " TEXT , " + type + " TEXT, " + date + " TEXT, " + amount + " REAL);";
        sqLiteDatabase.execSQL(createLogTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(acc_holder,account.getAccountHolderName());
        cv.put(bank,account.getBankName());
        double x = account.getBalance();
        cv.put(initial_balance, Double.toString(x));
        cv.put(account_no,account.getAccountNo());
        db.insert(account_table, null, cv);
        db.close();


    }

    public boolean addTransation(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tv = new ContentValues();
        tv.put(account_no,transaction.getAccountNo());
        tv.put(type,transaction.getExpenseType());
        tv.put(amount,transaction.getAmount());
        tv.put(date,transaction.getDate().toString());

        long insert = db.insert(Transaction_table, null, tv);
        if(insert >= 0){
            return true;
        }
        else{
            return false;
        }

    }

    public List<Transaction> getEveryTransaction() throws ParseException {
        List<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + Transaction_table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(3));
                String accountNumber = cursor.getString(1);
                String type = cursor.getString(2);
                ExpenseType et;
                if(type == "INCOME"){
                    et = ExpenseType.INCOME;
                }
                else{
                    et = ExpenseType.EXPENSE;
                }

                Double amount =  cursor.getDouble(4);


                Transaction newCustomer = new Transaction(date1,accountNumber,et,amount);
                returnList.add(newCustomer);
            }while (cursor.moveToNext());

        }
        else{
            //empty list

        }
        cursor.close();;
        db.close();
        return returnList;
    }

    public List<Transaction> getEveryPagingTransaction(int limit) throws ParseException {
        List<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + Transaction_table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        int i = 0;
        if(cursor.moveToFirst()){
            do {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(3));
                String accountNumber = cursor.getString(1);
                String type = cursor.getString(2);
                ExpenseType et;
                if(type == "INCOME"){
                    et = ExpenseType.INCOME;
                }
                else{
                    et = ExpenseType.EXPENSE;
                }

                Double amount =  cursor.getDouble(4);


                Transaction newCustomer = new Transaction(date1,accountNumber,et,amount);
                returnList.add(newCustomer);
                i++;
            }while (i!=limit);

        }
        else{
            //empty list

        }
        cursor.close();;
        db.close();
        return returnList;
    }



    public List<Account> getEveryAccount() throws ParseException {
        List<Account> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + account_table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do {

                String accountNumber = cursor.getString(0);
                String bank = cursor.getString(1);
                String accountHolder = cursor.getString(2);
                Double balance = cursor.getDouble(3);


                Account newAccount = new Account(accountNumber,bank,accountHolder,balance);
                returnList.add(newAccount);
            }while (cursor.moveToNext());

        }
        else{
            //empty list

        }
        cursor.close();;
        db.close();
        return returnList;
    }

    public List<String> getEveryAccountnumber() throws ParseException {
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + account_table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do {

                String accountNumber = cursor.getString(0);

                returnList.add(accountNumber);
            }while (cursor.moveToNext());

        }
        else{
            //empty list

        }
        cursor.close();;
        db.close();
        return returnList;
    }

    public Account getAccount(String accName){
        Account returnAccount;
        String queryString = "SELECT * FROM " + account_table + " WHERE "+  account_no + " = " + accName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            String accountNumber = cursor.getString(0);
            String bank = cursor.getString(1);
            String accountHolder = cursor.getString(2);
            Double balance = cursor.getDouble(3);


            returnAccount = new Account(accountNumber,bank,accountHolder,balance);

        }

        else{
        //empty list
            returnAccount = new Account(null,null,null,0);
        }
        cursor.close();;
        db.close();
        return returnAccount;
    }

    public void updateAccount(Double amount, String acc_number){
        SQLiteDatabase db = this.getReadableDatabase();
        String samount = amount.toString();
        String updatequeryString  = "UPDATE "+ account_table +" SET "+ initial_balance + " = " + samount + " WHERE " + account_no + " = " + acc_number;
        Cursor cursor = db.rawQuery(updatequeryString,null);
        cursor.close();;
        db.close();
    }




}
