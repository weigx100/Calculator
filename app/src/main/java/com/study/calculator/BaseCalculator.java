package com.study.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BaseCalculator {
    private final char[] operSet = {'+', '-', '*', '/', '(', ')', '#'};

    private final Map<Character, Integer> operMap = new HashMap<Character, Integer>() {{
        put('+', 0);
        put('-', 1);
        put('*', 2);
        put('/', 3);
        put('(', 4);
        put(')', 5);
        put('#', 6);
    }};

    private final char[][] operPrior = {
            /* (o1,o2)  +    -    *    /    (    )    # */
            /*  +  */ {'>', '>', '<', '<', '<', '>', '>'},
            /*  -  */ {'>', '>', '<', '<', '<', '>', '>'},
            /*  *  */ {'>', '>', '>', '>', '<', '>', '>'},
            /*  /  */ {'>', '>', '>', '>', '<', '>', '>'},
            /*  (  */ {'<', '<', '<', '<', '<', '=', ' '},
            /*  )  */ {'>', '>', '>', '>', ' ', '>', '>'},
            /*  #  */ {'<', '<', '<', '<', '<', ' ', '='},
    };


    private char getPrior(char oper1, char oper2) {
        return operPrior[operMap.get(oper1)][operMap.get(oper2)];
    }

    private double operate(double a, char oper, double b) {
        switch (oper) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    return Double.MAX_VALUE;
                }
                return a / b;
            default:
                return 0;
        }
    }

    private double calSubmath(String math) {
        try {
            if (math.length() == 0) {
                return Double.MAX_VALUE;
            } else {
                if (!hasOper(math.substring(1, math.length())) || math.contains("E-")) {
                    return Double.parseDouble(math);
                }
                int flag = 0;
                if (math.charAt(0) == '-') {
                    flag = 1;
                    math = math.substring(1, math.length());
                }

                Stack<Character> operStack = new Stack<>();
                Stack<Double> numStack = new Stack<>();

                operStack.push('#');
                math += "#";

                String tempNum = "";
                for (int i = 0; i < math.length(); i++) {

                    char charOfMath = math.charAt(i); //遍历math中的char


                    if (!isOper(charOfMath)
                            || charOfMath == '-' && math.charAt(i - 1) == '(') {
                        tempNum += charOfMath;
                        i++;
                        charOfMath = math.charAt(i);

                        if (isOper(charOfMath)) {
                            double num = Double.parseDouble(tempNum);
                            if (flag == 1) {
                                num = -num;
                                flag = 0;
                            }
                            numStack.push(num);
                            tempNum = "";
                        }
                        i--;
                    }

                    else {

                        switch (getPrior(operStack.peek(), charOfMath)) {
                            case '<':
                                operStack.push(charOfMath);
                                break;
                            case '=':
                                operStack.pop();
                                break;
                            case '>':
                                char oper = operStack.pop();
                                double b = numStack.pop();
                                double a = numStack.pop();
                                if (operate(a, oper, b) == Double.MAX_VALUE)
                                    return Double.MAX_VALUE;
                                numStack.push(operate(a, oper, b));
                                i--;
                                break;
                        }
                    }
                }
                return numStack.peek();
            }
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }

    double cal(String math) {
        try {
            if (math.length() == 0) {
                return Double.MAX_VALUE;
            } else {
                if (!hasOper(math.substring(1, math.length())) || math.contains("E-")) {
                    return Double.parseDouble(math);
                }
                else {
                    return calSubmath(math);
                }
            }
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }

    private boolean hasOper(String s) {
        return s.contains("+") || s.contains("-") || s.contains("*") || s.contains("/");
    }

    boolean isOper(char c) {
        int i;
        for (i = 0; i < operSet.length; i++) {
            if (c == operSet[i]) {
                break;
            }
        }
        return i != operSet.length;
    }
}
