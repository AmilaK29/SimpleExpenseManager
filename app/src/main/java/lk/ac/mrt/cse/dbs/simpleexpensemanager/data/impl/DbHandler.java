package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "200196G";
    private static final String TABLE_NAME_ACCOUNTS = "accounts";
    private static final String TABLE_NAME_TRANSACTION = "transactions";

    //Table Columns for accounts table
    private static final String ACCOUNT_NO = "account_no";
    private static final String BANK = "bank";
    private static final String ACC_HOLDER = "account_holder";
    private static final String INIT_BALANCE = "initial_balance";

    //Table Columns for transactions table
    private static final String TRANSACTION_ID = "id";
    private static final String EXPENSE_TYPE = "expense_type";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";

    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Creating Table to store Accounts
        String AccountTableQuery = "CREATE TABLE " + TABLE_NAME_ACCOUNTS + " "
                + "(" + ACCOUNT_NO + " TEXT PRIMARY KEY,"
                      + BANK + " TEXT,"
                      + ACC_HOLDER + " TEXT,"
                      + INIT_BALANCE + " TEXT" +
                    ");";
        //Creating Table to store transactions
        String TransactionTableQuery = "CREATE TABLE " + TABLE_NAME_TRANSACTION + " "
                + "("
                      + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                      + ACCOUNT_NO + " TEXT,"
                      + EXPENSE_TYPE + " TEXT,"
                      + DATE + " TEXT,"
                      + AMOUNT + " TEXT" +
                      ");" ;
        sqLiteDatabase.execSQL(TransactionTableQuery);
        sqLiteDatabase.execSQL(AccountTableQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {



        String DropAccountTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCOUNTS;
        String DropTransactionTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME_TRANSACTION;
        sqLiteDatabase.execSQL(DropAccountTableQuery);
        sqLiteDatabase.execSQL(DropTransactionTableQuery);
        onCreate(sqLiteDatabase);

    }

    public void addAccount(Account account){
        /***
         Thsi will add new Account to the account table
         An Account object is passed as the parameter
         ***/
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Structering data to be inserted into the database
        contentValues.put(ACCOUNT_NO,account.getAccountNo());
        contentValues.put(BANK,account.getBankName());
        contentValues.put(ACC_HOLDER,account.getAccountHolderName());
        contentValues.put(INIT_BALANCE,account.getBalance());

        //Inserting values to the database
        sqLiteDatabase.insert(TABLE_NAME_ACCOUNTS,null,contentValues);
        sqLiteDatabase.close();

    }

    public Account getAccount(String acc_no){
        /***
         Get an Account given the account number and return the Account object
          ***/
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_ACCOUNTS,new String[]{ACCOUNT_NO,BANK,ACC_HOLDER,INIT_BALANCE},ACCOUNT_NO + " = ?",new String[]{acc_no},null,null,null);

        Account account = null;
        if(cursor != null){

            String account_no = cursor.getString(0);
            String bankName = cursor.getString(1);
            String holderName = cursor.getString(2);
            double balance = cursor.getDouble(3);
            account = new Account(account_no,bankName,holderName,balance);
            return account;

        }
        return account;

    }

    public List<Account> getAllAccounts(){
        /***
         Generate an Array list of all the accounts
         ***/
        //Array to store accounts
        List<Account> accounts = new ArrayList<Account>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String get_all_acc_state = "SELECT * FROM " + TABLE_NAME_ACCOUNTS;

        Cursor cursor = sqLiteDatabase.rawQuery(get_all_acc_state,null);

        if(cursor.moveToFirst()){
            do{
                String account_no = cursor.getString(0);
                String bankName = cursor.getString(1);
                String holderName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account account = new Account(account_no,bankName,holderName,balance);
                accounts.add(account);
            }
            while (cursor.moveToNext());
        }

        return accounts;

    }

    public void deleteAccount(String accno){
        /***
         Delete an Account given the account number
         */
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME_ACCOUNTS,ACCOUNT_NO + " =?",new String[]{accno});
        sqLiteDatabase.close();

    }

    public void addTransaction(Transaction transaction){
        /***
         Adding transaction to the given table
         Transaction object is passed as the parameter
         */
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
        String date = simpleDateFormat.format(transaction.getDate());


        contentValues.put(ACCOUNT_NO,transaction.getAccountNo());
        contentValues.put(DATE,date);
        contentValues.put(AMOUNT,Double.toString(transaction.getAmount()));
        contentValues.put(EXPENSE_TYPE, transaction.getExpenseType().name());

        sqLiteDatabase.insert(TABLE_NAME_TRANSACTION,null,contentValues);
        sqLiteDatabase.close();
    }

    public List<Transaction> getAllTransactions(){
        /***
         Generate a list of all transactions
         */

        //Array list to store all the transactions
        List<Transaction> transactions = new ArrayList<Transaction>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String get_all_transactions = "SELECT * FROM " + TABLE_NAME_TRANSACTION;
        Cursor cursor = sqLiteDatabase.rawQuery(get_all_transactions,null);

        if(cursor.moveToFirst()){
            do {
                String account_no = cursor.getString(1);
                String expense_type_str = cursor.getString(2);
                String date_str = cursor.getString(3);
                double amount = cursor.getDouble(4);

                ExpenseType expense_type = ExpenseType.valueOf(expense_type_str);

                Date date;
                try {
                    //Converting string of the date to the Date object
                    DateFormat df = new SimpleDateFormat("yyyy/mm/dd");
                    date = df.parse(date_str);
                    Transaction transaction = new Transaction(date,account_no,expense_type,amount);
                    transactions.add(transaction);
                }catch (java.text.ParseException e){
                    e.printStackTrace();
                }


            }
            while (cursor.moveToNext());
        }

        return transactions;

    }
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount){
        /***
         This will update the balance given the account number,expense type and the amount
         */
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Account account = getAccount(accountNo);
        double existing_amount = account.getBalance();

        if(expenseType.name() == "EXPENSE"){
            existing_amount = existing_amount - amount;
        }
        else if(expenseType.name() == "INCOME"){
            existing_amount = existing_amount + amount;
        }

        contentValues.put(INIT_BALANCE,Double.toString(existing_amount));

        int status = sqLiteDatabase.update(TABLE_NAME_ACCOUNTS,contentValues,ACCOUNT_NO + " =?",new String[]{accountNo});

        sqLiteDatabase.close();
    }


}
