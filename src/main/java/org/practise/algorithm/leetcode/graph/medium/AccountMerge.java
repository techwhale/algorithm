package org.practise.algorithm.leetcode.graph.medium;

import java.util.*;

/**
 * 721. Accounts Merge
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a list of accounts where each element accounts[i] is a list of strings, where the first element accounts[i][0] is a name, and the rest of the elements are emails representing emails of the account.
 *
 * Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there is some common email to both accounts. Note that even if two accounts have the same name, they may belong to different people as people could have the same name. A person can have any number of accounts initially, but all of their accounts definitely have the same name.
 *
 * After merging the accounts, return the accounts in the following format: the first element of each account is the name, and the rest of the elements are emails in sorted order. The accounts themselves can be returned in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: accounts = [["John","johnsmith@mail.com","john_newyork@mail.com"],["John","johnsmith@mail.com","john00@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
 * Output: [["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
 * Explanation:
 * The first and second John's are the same person as they have the common email "johnsmith@mail.com".
 * The third John and Mary are different people as none of their email addresses are used by other accounts.
 * We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'],
 * ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']] would still be accepted.
 * Example 2:
 *
 * Input: accounts = [["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]]
 * Output: [["Ethan","Ethan0@m.co","Ethan4@m.co","Ethan5@m.co"],["Gabe","Gabe0@m.co","Gabe1@m.co","Gabe3@m.co"],["Hanzo","Hanzo0@m.co","Hanzo1@m.co","Hanzo3@m.co"],["Kevin","Kevin0@m.co","Kevin3@m.co","Kevin5@m.co"],["Fern","Fern0@m.co","Fern1@m.co","Fern5@m.co"]]
 *
 *
 * Constraints:
 *
 * 1 <= accounts.length <= 1000
 * 2 <= accounts[i].length <= 10
 * 1 <= accounts[i][j].length <= 30
 * accounts[i][0] consists of English letters.
 * accounts[i][j] (for j > 0) is a valid email.
 */
public class AccountMerge {
    class Node {
        String name;
        Set<String> emails;
        public Node(String name, List<String> emails){
            this.name = name;
            this.emails = new HashSet<>();
            this.emails.addAll(emails);
        }
    }
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Node> map = new HashMap<>();
        for (List<String> account : accounts) {
            String name = account.get(0);
            Set<Node> relatedAccounts = new HashSet<>();
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                if (map.containsKey(email)) {
                    relatedAccounts.add(map.get(email));
                }
            }
            Node primaryNode;
            if (relatedAccounts.isEmpty()) {
                primaryNode = new Node(name, account.subList(1, account.size()));
            } else {
                primaryNode = relatedAccounts.iterator().next();
                // merge all other account
                for (Node node : relatedAccounts) {
                    if (node == primaryNode) {
                        continue;
                    }
                    for (String email : node.emails) {
                        primaryNode.emails.add(email);
                        map.put(email, primaryNode);
                    }
                }
            }
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                primaryNode.emails.add(email);
                map.put(email, primaryNode);
            }
        }
        List<List<String>> result = new ArrayList<>();
        for (Node node : new HashSet<>(map.values())) {
            List<String> currentAccount = new ArrayList<>();
            currentAccount.add(node.name);
            List<String> emails = new ArrayList<>(node.emails);
            Collections.sort(emails);
            currentAccount.addAll(emails);
            result.add(currentAccount);
        }
        return result;
    }
}
