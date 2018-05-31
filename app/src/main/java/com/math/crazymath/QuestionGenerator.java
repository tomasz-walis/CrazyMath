package com.math.crazymath;

import java.util.*;


/**
 * Created by Tom on 04/03/2017.
 */

public class QuestionGenerator {

    //variables
    private static final int NUMBER_OF_QUESTIONS = 10;
    private static final Operator[] OPERATORS = {Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY, Operator.DIVIDE};


    // generate random question
    public static List<QuestionEvaluation> generate(final int level) {
        List<QuestionEvaluation> questionEvaluations = new ArrayList<>();
        for(int i=0; i<=NUMBER_OF_QUESTIONS; i++) {
            questionEvaluations.add(generateQuestion(level));
        }
        return questionEvaluations;
    }

    //genarate question based on chosen level and operators needed for choosen level
    private static QuestionEvaluation generateQuestion(final int level) {

        int operandsNeeded;
        //2 operands for novice level
        if(level == Level.NOVICE) {
            operandsNeeded = 2;
        }
        //2 to 3 operands for easy level
        else if(level == Level.EASY) {
            operandsNeeded = randomInt(2, 3);
        }
        //2 to 4 operands for medium level
        else if(level == Level.MEDIUM) {
            operandsNeeded = randomInt(2, 4);
        }
        //4 - 6 operands for guru level
        else {
            operandsNeeded = randomInt(4, 6);
        }

        List<Object> list = new ArrayList<>();

        list.add(randomInt(1, 9));
        list.add(randomOperator());
        list.add(randomInt(1, 9));
        int operands = 2;

        while(operands < operandsNeeded) {
            list.add(randomOperator());
            list.add(randomInt(1, 9));
            operands++;
        }

        return new QuestionEvaluation(list);
    }

    //generate random operator
    private static Operator randomOperator() {
        int operatorIndex = randomInt(0, OPERATORS.length-1);
        return OPERATORS[operatorIndex];
    }

    //generate random number
    private static int randomInt(final int lowerBound, final int upperBound) {
        return lowerBound + (int)(Math.random() * ((upperBound - lowerBound) + 1));
    }
}
