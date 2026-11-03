package com.calcbuzz.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates simple arithmetic expressions such as "12+3.5×2−8÷4".
 * Supports + − × ÷ with correct precedence (× and ÷ before + and −)
 * and a leading minus sign for negative numbers.
 */
public class Evaluator {

    public static final char ADD = '+';
    public static final char SUB = '−';   // true minus sign (U+2212)
    public static final char MUL = '×';
    public static final char DIV = '÷';

    public static boolean isOperator(char c) {
        return c == ADD || c == SUB || c == MUL || c == DIV || c == '-';
    }

    /**
     * @throws ArithmeticException on division by zero or an invalid result
     * @throws NumberFormatException on a malformed expression
     */
    public static double evaluate(String expr) {
        List<Double> numbers = new ArrayList<>();
        List<Character> ops = new ArrayList<>();

        StringBuilder num = new StringBuilder();
        boolean negative = false;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                num.append(c);
            } else if (isOperator(c)) {
                boolean isMinus = (c == SUB || c == '-');
                if (num.length() == 0) {
                    // Operator with no number before it: only a leading minus is allowed
                    if (isMinus && numbers.isEmpty() && !negative) {
                        negative = true;
                        continue;
                    }
                    throw new NumberFormatException("Malformed expression");
                }
                numbers.add(parse(num.toString(), negative));
                num.setLength(0);
                negative = false;
                ops.add(c == '-' ? SUB : c);
            } else {
                throw new NumberFormatException("Unexpected character: " + c);
            }
        }

        if (num.length() == 0) {
            throw new NumberFormatException("Malformed expression");
        }
        numbers.add(parse(num.toString(), negative));

        // Pass 1: handle × and ÷ first
        List<Double> reducedNums = new ArrayList<>();
        List<Character> reducedOps = new ArrayList<>();
        reducedNums.add(numbers.get(0));
        for (int i = 0; i < ops.size(); i++) {
            char op = ops.get(i);
            double next = numbers.get(i + 1);
            if (op == MUL || op == DIV) {
                int last = reducedNums.size() - 1;
                double current = reducedNums.get(last);
                if (op == DIV && next == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                reducedNums.set(last, op == MUL ? current * next : current / next);
            } else {
                reducedOps.add(op);
                reducedNums.add(next);
            }
        }

        // Pass 2: handle + and − from left to right
        double result = reducedNums.get(0);
        for (int i = 0; i < reducedOps.size(); i++) {
            double next = reducedNums.get(i + 1);
            result = (reducedOps.get(i) == ADD) ? result + next : result - next;
        }

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException("Invalid result");
        }
        return result;
    }

    private static double parse(String s, boolean negative) {
        double value = Double.parseDouble(s);
        return negative ? -value : value;
    }
}
