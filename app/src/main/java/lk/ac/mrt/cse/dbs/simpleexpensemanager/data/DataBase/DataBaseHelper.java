package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + account_table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Transaction_table);
        onCreate(sqLiteDatabase);

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




    public List<Account> getEveryAccount() {
        List<Account> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + account_table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do {

                String accountNumber = cursor.getString(0);
                String bank = cursor.getString(1);
                String accountHolder = cursor.getString(2);
                double balance = cursor.getDouble(3);


                Account newAccount = new Account(accountNumber,bank,accountHolder,balance);
                returnList.add(newAccount);
            }while (cursor.moveToNext());

        }
        //empty list

        cursor.close();
        db.close();
        return returnList;
    }

    public List<String> getEveryAccountnumber() {
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
        //empty list

        cursor.close();
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
            double balance = cursor.getDouble(3);


            returnAccount = new Account(accountNumber,bank,accountHolder,balance);

        }

        else{
        //empty list
            returnAccount = new Account(null,null,null,0);
        }
        cursor.close();
        db.close();
        return returnAccount;
    }

    public void updateAccount(Double amount, String acc_number){
        SQLiteDatabase db = this.getReadableDatabase();
        String samount = amount.toString();
        String updatequeryString  = "UPDATE "+ account_table +" SET "+ initial_balance + " = " + samount + " WHERE " + account_no + " = " + acc_number;
        db.execSQL(updatequeryString);
        db.close();
    }

    public void deleteAccount(String accountnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + account_table + " WHERE " + account_no + " = " + accountnumber;
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.close();
        db.close();
    }

    public void addTransation(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tv = new ContentValues();
        tv.put(account_no,transaction.getAccountNo());
        tv.put(type,transaction.getExpenseType());
        tv.put(amount,transaction.getAmount());
        tv.put(date,transaction.getDate().toString());

        db.insert(Transaction_table, null, tv);
    }


    public ArrayList<Transaction> readTransactions(int limit) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTransactions;
        if (limit == 0){
            cursorTransactions = db.rawQuery("SELECT * FROM " + Transaction_table, null);
        }
        else {

            cursorTransactions = db.rawQuery("SELECT * FROM " + Transaction_table + " LIMIT " + limit, null);
        }

        ArrayList<Transaction> transactionsArrayList = new ArrayList<>();


        if (cursorTransactions.moveToFirst()) {
            do {

                ExpenseType type;
                if(cursorTransactions.getString(2).equals("EXPENSE")){
                    type = ExpenseType.EXPENSE;
                }
                else{
                    type = ExpenseType.INCOME;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = new Date();
                try{
                    date = dateFormat.parse(cursorTransactions.getString(3));
                }catch(Exception e){
                    System.out.println(e);
                }

                transactionsArrayList.add(new Transaction(date,
                        cursorTransactions.getString(1),
                        type,
                        cursorTransactions.getDouble(4)));
            } while (cursorTransactions.moveToNext());

        }

        cursorTransactions.close();
        return transactionsArrayList;
    }

    public void deleteLastTransaction(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlquery = "DELETE FROM " + Transaction_table + " WHERE " + log_id  + " = (SELECT MAX(" + log_id + " ) FROM " + Transaction_table + ");";
        db.execSQL(sqlquery);
    }




}
