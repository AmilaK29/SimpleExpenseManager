package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentTransactionDAO implements TransactionDAO {
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        MainActivity.dbHandler.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactions = MainActivity.dbHandler.getAllTransactions();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit){

        List<Transaction> Alltransactions = getAllTransactionLogs();
        List<Transaction> limitedTransactions = new ArrayList<Transaction>();

        if(limit > getAllTransactionLogs().size()){
            return Alltransactions;
        }
        else{

            for (int i = 0 ; i < limit ; i++){
                limitedTransactions.add(Alltransactions.get(i));
            }
            return limitedTransactions;
        }


    }
}
