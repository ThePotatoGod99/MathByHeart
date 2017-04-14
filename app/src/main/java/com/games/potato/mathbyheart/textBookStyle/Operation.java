package com.games.potato.mathbyheart.textBookStyle;

/**
 * Created by Simon on 2017-04-13.
 */

public class Operation {
    private Operation term1, term2;


    private String number1, number2;/*To use only when operator = null*/


    private char operator = 0;

    private boolean isLastTerm = false;

    private boolean usesParenthesis = false;

    private boolean isSingleNumber = false;
    private String singleNumber = "";


    public Operation(Operation term1, Operation term2, char operator) {
        this.term1 = term1;
        this.term2 = term2;
        this.operator = operator;
    }
    public Operation(String number1, String number2, char operator){
        this.number1 = number1;
        this.number2 = number2;
        this.operator = operator;
        isLastTerm = true;
    }
    public Operation(){

    }

    public Operation(String number){
        isSingleNumber = true;
        singleNumber = number;
    }

    //Getters & Setters

    public Operation getTerm1() {
        return term1;
    }

    public void setTerm1(Operation term1) {
        this.term1 = term1;
    }

    public Operation getTerm2() {
        return term2;
    }

    public void setTerm2(Operation term2) {
        this.term2 = term2;
    }



    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }





    public boolean isUsesParenthesis() {
        return usesParenthesis;
    }

    public void setUsesParenthesis(boolean usesParenthesis) {
        this.usesParenthesis = usesParenthesis;
    }

    @Override
    public String toString() {
        if(isSingleNumber){
            return singleNumber;
        }
        return term1.toString() + operator + term2.toString();
    }
}
