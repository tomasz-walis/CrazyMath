package com.math.crazymath;

/**
 * Created by Tom on 04/03/2017.
 */

public enum Operator {

    //operators
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    //variables
    private String displayValue;

    Operator(String displayValue) {
        this.displayValue = displayValue;
    }

    //display operator as string
    public String toString() {
        return displayValue;
    }
}