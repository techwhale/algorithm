package org.practise.algorithm.interestingideas;

import java.util.*;

/**
 * 399. Evaluate Division
 * Medium
 * You are given an array of variable pairs equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i]. Each Ai or Bi is a string that represents a single variable.
 * You are also given some queries, where queries[j] = [Cj, Dj] represents the jth query where you must find the answer for Cj / Dj = ?.
 * Return the answers to all queries. If a single answer cannot be determined, return -1.0.
 * Note: The input is always valid. You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.
 *
 * Example 1:
 * Input: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * Explanation:
 * Given: a / b = 2.0, b / c = 3.0
 * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
 *
 * Example 2:
 * Input: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * Output: [3.75000,0.40000,5.00000,0.20000]
 *
 * Example 3:
 * Input: equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * Output: [0.50000,2.00000,-1.00000,-1.00000]
 *
 * Constraints:
 * 1 <= equations.length <= 20
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * values.length == equations.length
 * 0.0 < values[i] <= 20.0
 * 1 <= queries.length <= 20
 * queries[i].length == 2
 * 1 <= Cj.length, Dj.length <= 5
 * Ai, Bi, Cj, Dj consist of lower case English letters and digits.
 */
public class EvaluateDivision {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        buildGraph(equations, values, graph);

        double[] result = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            List<String> query = queries.get(i);
            result[i] = findValue(query.get(0), query.get(1), new HashSet<>(), graph);
        }
        return result;
    }


    private double findValue(String dividend, String divisor, Set<String> visited, Map<String, Map<String, Double>> graph) {
        if (! (graph.containsKey(dividend) && graph.containsKey(divisor))) {
            return -1;
        } else if (dividend.equals(divisor)) {
            return 1;
        } else {
            return backtrack(dividend, divisor, new HashSet<>(), graph);
        }
    }


    private double backtrack(String source, String target, Set<String> visited, Map<String, Map<String, Double>> graph) {
        if (source.equals(target)) {
            return 1;
        }
        visited.add(source);

        if (graph.get(source).containsKey(target)) {
            return graph.get(source).get(target);
        } else {
            for (String neighbor : graph.get(source).keySet()) {
                if (visited.contains(neighbor)) {
                    continue;
                }
                double value = backtrack(neighbor, target, visited, graph);
                if (value != -1) {
                    return value * graph.get(source).get(neighbor);
                }
            }
        }

        visited.remove(source);
        return -1;
    }

    private void buildGraph(List<List<String>> equations, double[] values, Map<String, Map<String, Double>> graph) {
        for (int i = 0; i < equations.size(); i++) {
            List<String> equation = equations.get(i);
            String dividend = equation.get(0), divisor = equation.get(1);
            double quotient  = values[i];

            if (! graph.containsKey(dividend)) {
                graph.put(dividend, new HashMap<>());
            }

            if (! graph.containsKey(divisor)) {
                graph.put(divisor, new HashMap<>());
            }

            graph.get(dividend).put(divisor, quotient);
            graph.get(divisor).put(dividend, 1 / quotient);
        }
    }
}
