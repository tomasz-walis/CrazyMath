package com.math.crazymath;

/**
 * Created by Tom on 04/03/2017.
 */

public class Answer {

    //variables
    public static final String DEFAULT_VALUE = "?";
    private String buffer;
    private boolean negate;

    public Answer() {
        this.buffer = DEFAULT_VALUE;
    }

    //append the input
    public void append(final String key) {
        if(this.buffer.equals(DEFAULT_VALUE)) {
            this.buffer = "";
        }
        this.buffer += key;
    }

    //set the value of text field
    public int value() {
        int value = 0;
        try {
            value = Integer.parseInt(this.buffer);
        }
        catch (Exception e) {}
        if(this.negate) {
            return value * -1;
        }
        return value;
    }

    //reset answer text firld to deafult value
    public void reset() {
        this.buffer = DEFAULT_VALUE;
        this.negate = false;
    }

    //when delete button pressed question mark dose not appear
    public void delete() {
        this.buffer = "";
        this.negate = false;
    }


    public void negate() {
        this.negate = !this.negate;
    }

    public String toString() {
        if(this.negate && !this.equals(DEFAULT_VALUE)) {
            return String.format("-%s", this.buffer);
        }
        return this.buffer;
    }
}