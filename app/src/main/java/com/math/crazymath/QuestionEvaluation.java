package com.math.crazymath;

import java.util.*;


/**
 * Created by Tom on 06/03/2017.
 */

public class QuestionEvaluation {

    private List<Object> list;


    public QuestionEvaluation(List<Object> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + " ";
        }
        return str;
    }


    public int answer() {
        int theAnswer = (int) calculate(this.toString());
        return theAnswer;
    }

    /*calculate if the user input is correct answer,
    do not consider any parenthesis in the question*/
    public double calculate(String userInput) {
        String[] expression = userInput.split(" ");
        int i = 0;
        double result = Integer.valueOf(expression[i++]);
        while (i < expression.length) {
            String operator = expression[i++];
            double rightOperand = Double.valueOf(expression[i++]);
            switch (operator) {
                case "*":
                    result = result * rightOperand;
                    break;
                case "/":
                    result = result / rightOperand;
                    break;
                case "+":
                case "-":
                    while (i < expression.length) {
                        String operator2 = expression[i++];
                        if (operator2.equals("+") || operator2.equals("-")) {
                            i--;
                            break;
                        }
                        if (operator2.equals("*")) {
                            rightOperand = rightOperand * Double.valueOf(expression[i++]);
                        }
                        if (operator2.equals("/")) {
                            rightOperand = rightOperand / Double.valueOf(expression[i++]);
                        }
                    }
                    if (operator.equals("+")) {
                        result = result + rightOperand;
                    } else {
                        result = result - rightOperand;
                    }
                    break;
            }

        }

        //round the answer
        int value = (int) Math.rint(result);
        return value ;
    }
}