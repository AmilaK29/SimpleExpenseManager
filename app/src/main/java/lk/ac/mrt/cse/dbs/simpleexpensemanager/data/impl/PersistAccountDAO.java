package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistAccountDAO implements AccountDAO {


    @Override
    public List<String> getAccountNumbersList() {
        List<Account> accounts = MainActivity.dbHandler.getAllAccounts();
        List<String> account_numbers = new ArrayList<String>();
        for(int i = 0;i < accounts.size();i++){
            account_numbers.add(accounts.get(i).getAccountNo());
        }
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = MainActivity.dbHandler.getAllAccounts();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account acc = MainActivity.dbHandler.getAccount(accountNo);

        return  acc;
    }

    @Override
    public void addAccount(Account account) {
        MainActivity.dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        MainActivity.dbHandler.deleteAccount(accountNo);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
