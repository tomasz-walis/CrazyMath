package com.math.crazymath;

import java.util.*;

/**
 * Created by Tom on 04/03/2017.
 */

public class Quiz {

    //variables
    private static final String DEFAULT_USER = "Unknown User";
    private List<QuestionEvaluation> questionEvaluations;
    private int current;
    private String user;
    private int level;
    private static Quiz instance;
    ActivityGame c = new ActivityGame();



    public static Quiz getInstance() {
        if(instance == null) {
            synchronized (Quiz.class) {
                instance = new Quiz(0);
            }
        }
        return instance;
    }


    private Quiz(final int level) {
        this.level = level;
        this.questionEvaluations = QuestionGenerator.generate(level);
        this.user = DEFAULT_USER;
        this.current = -1;
    }

    //generate next question
    public QuestionEvaluation getNextQuestion() {
        if(current <= questionEvaluations.size()) {
            return questionEvaluations.get(++current);
        }
        return null;
    }

    //validate the user answer if hints are off
    public boolean validate(final int userAnswer) {

        QuestionEvaluation questionEvaluation = this.questionEvaluations.get(this.current);
        int answer = questionEvaluation.answer();
        if(userAnswer == answer) {
            return true;
        }
        return false;
    }
    //validate the user answer if hints are on
    public boolean hintsValidate(final int userAnswer) {
        QuestionEvaluation questionEvaluation = this.questionEvaluations.get(this.current);
        int answer = questionEvaluation.answer();
        if (userAnswer == answer) {
            return true;
        } else if (userAnswer > answer) {
            c.setText("LESS!");
            return false;
        } else if (userAnswer < answer) {
            c.setText("GREATER!");
            return false;
        } else {
            return false;
        }
    }


    public int size() {
        return this.questionEvaluations.size();
    }

    public boolean isComplete() {
        return (this.current + 1) >= this.size() - 1;
    }

    //set user
    public void setUser(String user) {
        this.user = user;
    }

    //get user
    public String getUser() {
        return this.user;
    }

    public int getCurrent() {
        return current;
    }

    //start quiz
    public void start(int level) {
        synchronized (Quiz.class) {
            instance = new Quiz(level);
        }
    }



    public boolean isInProgress() {
        System.out.println("Current is " + current);
        return this.current >= 0;
    }

    //get level
    public int getLevel() {
        return level;
    }

    public void resume(int level, int current, int score) {
        start(level);
        this.current = current;
        //this.score = score;
    }
}
