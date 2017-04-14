package com.games.potato.mathbyheart.Utilities;

import android.graphics.Path;
import android.util.Log;

import com.games.potato.mathbyheart.textBookStyle.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Simon on 2017-04-13.
 */

public class Math {


    public static ArrayList<Object> separateTerms(List<String> input, int start) {
        ArrayList<Object> result = new ArrayList<>();
        while(!input.isEmpty()){
            String object = input.get(0);
            input.remove(0);
            if (object.equals(")")) {
                return result;
            }
            if (object.equals("(")) {
                result.add(separateTerms(input, 1));
            } else {
                result.add(object);
            }
        }
        return result;
    }

    public static ArrayList<String> separateNumbers(String expression) {
        ArrayList<String> list = new ArrayList<>();
        String number = "";
        Character character;

        for (int i = 0; i < expression.length(); i++) {
            character = expression.charAt(i);
            if (isNumber(character)) {
                number += character;
            } else {
                if (number != "") {
                    list.add(number);
                    number = "";
                }
                list.add(character.toString());
            }
        }
        return list;
    }

    public static String convertExpressionToSuffix(String expression) {
        ArrayList<String> list = separateNumbers(expression);
        String object;
        Character firstChar;


        String suffixe = "";
        Stack stack = new Stack<Character>();
        stack.stack('(');
        char courant;

        list.add(")");
        expression += ")";


//        Operation operation;

        //      Operation operation1 = new Operation(); //;= new Operation("");

        Operation operation = new Operation();
        Operation operation1 = new Operation();
        String number[] = new String[2];
        int j = 0;
        for (int i = 0; !stack.empty(); i++) {
            object = list.get(i);
            firstChar = object.charAt(0);

            print("char = " + object + " number " + number[0] + "  |number2| " + number[1]);

            courant = firstChar;//= expression.charAt(i);
            if (isNumber(courant)) {
                //  number1 += courant;
                number[j] = object;
                //  suffixe += courant;
            } else if (courant == '(') {
                stack.stack(courant);
            } else if (isOperator(courant)) {
                while (priorityHigherOrEqual((Character) stack.look(), courant)) {


                    suffixe += number[j];
                    suffixe += stack.unstack();
                    number[0] = "";
                    number[1] = "";
                }
                if (j == 1) {
                    j = 0;
                } else {
                    j = 1;
                }
                stack.stack(courant);
            } else if (courant == ')') {
                while ((Character) stack.look() != '(') {


                    operation1.setTerm1(operation1);
                    /*if (number[0] == "") {
                        operation.setOperator((char)stack.look());
                        operation.setTerm1(operation1);
                        operation.setTerm2(new Operation(number[1]));
                    } else {
                        operation1.setOperator((char)stack.look());
                        operation1.setTerm1(new Operation(number[0]));
                        operation1.setTerm2(new Operation(number[1]));
                    }*/

                    suffixe += number[0] + number[1];
                    suffixe += stack.unstack();
                    number[0] = "";
                    number[1] = "";
                    if (j == 1) {
                        j = 0;
                    } else {
                        j = 1;
                    }
                }
                stack.unstack();
            }
            print("SUFFIXE = " + suffixe);
        }
        print("SUfxd = " + suffixe);

        print(operation.toString());
        print("HOOOLLLAAA " + operation.getTerm2().toString());
        return suffixe;
    }

    public static boolean isNumber(Character character) {
        return (character >= '0' && character <= '9');
    }

    public static boolean isOperator(Character character) {
        switch (character) {
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
        }
        return false;
    }

    public static boolean priorityHigherOrEqual(char operator1, char operator2) {
        if (operator1 == '*' || operator1 == '/') {
            return true;
        }

        if ((operator1 == '+' || operator1 == '-') &&
                (operator2 == '+' || operator2 == '-')) {
            return true;
        }
        return false;
    }

    //TODO:DELETE THIS
    public static void print(String string) {
        Log.d("LOGGING_HOLA", string);
    }
}
