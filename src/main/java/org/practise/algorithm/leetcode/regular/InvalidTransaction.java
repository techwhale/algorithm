package org.practise.algorithm.leetcode.regular;

import java.util.*;

/**
 * 1169. Invalid Transactions
 * Medium
 * 444
 * 2K
 * company
 * Bloomberg
 * company
 * Amazon
 * company
 * Wix
 * A transaction is possibly invalid if:
 *
 * the amount exceeds $1000, or;
 * if it occurs within (and including) 60 minutes of another transaction with the same name in a different city.
 * You are given an array of strings transaction where transactions[i] consists of comma-separated values representing the name, time (in minutes), amount, and city of the transaction.
 *
 * Return a list of transactions that are possibly invalid. You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: transactions = ["alice,20,800,mtv","alice,50,100,beijing"]
 * Output: ["alice,20,800,mtv","alice,50,100,beijing"]
 * Explanation: The first transaction is invalid because the second transaction occurs within a difference of 60 minutes, have the same name and is in a different city. Similarly the second one is invalid too.
 * Example 2:
 *
 * Input: transactions = ["alice,20,800,mtv","alice,50,1200,mtv"]
 * Output: ["alice,50,1200,mtv"]
 * Example 3:
 *
 * Input: transactions = ["alice,20,800,mtv","bob,50,1200,mtv"]
 * Output: ["bob,50,1200,mtv"]
 *
 *
 * Constraints:
 *
 * transactions.length <= 1000
 * Each transactions[i] takes the form "{name},{time},{amount},{city}"
 * Each {name} and {city} consist of lowercase English letters, and have lengths between 1 and 10.
 * Each {time} consist of digits, and represent an integer between 0 and 1000.
 * Each {amount} consist of digits, and represent an integer between 0 and 2000.
 * */
public class InvalidTransaction {
    class Transaction {
        String name;
        int time;
        int amount;
        String city;
        boolean invalid;
        public Transaction(String name, int time, int amount, String city, boolean invalid) {
            this.name = name;
            this.time = time;
            this.amount = amount;
            this.city = city;
            this.invalid = invalid;
        }
    }

    public List<String> invalidTransactions(String[] transactions) {
        List<Transaction> list = new ArrayList<>();
        for (String t : transactions) {
            String[] txn = t.split(",");
            list.add(new Transaction(txn[0], Integer.parseInt(txn[1]), Integer.parseInt(txn[2]), txn[3], false));
        }

        for (int i = 0; i < list.size(); i++) {
            Transaction current = list.get(i);
            if (current.amount > 1000) {
                current.invalid = true;
            }
            for (int j = i + 1; j < list.size(); j++) {
                Transaction next = list.get(j);
                if (isInvalid(current, next)) {
                    current.invalid = true;
                    next.invalid = true;
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);
            if (t.invalid) {
                result.add(transactions[i]);
            }
        }
        return result;
    }


    private boolean isInvalid(Transaction t1, Transaction t2) {
        if (t2.time < t1.time) {
            return isInvalid(t2, t1);
        }

        if (!t1.name.equals(t2.name)) {
            return false;
        }
        return !t1.city.equals(t2.city) && t1.time + 60 >= t2.time;
    }
}
