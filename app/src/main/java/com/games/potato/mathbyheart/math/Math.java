package com.games.potato.mathbyheart.math;


import android.util.Log;

/**
 * Created by Simon Tran on 2017-04-13.
 */

public class Math{
    public static boolean isNumber(Character character){
        return (character >= '0' && character <= '9');
    }
    
    public static boolean isOperator(Character character){
        switch(character){
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
        }
        return false;
    }
    
    public static boolean priorityHigherOrEqual(char operator1, char operator2){
        if(operator1 == '*' || operator1 == '/'){
            return true;
        }
        
        if((operator1 == '+' || operator1 == '-') &&
                (operator2 == '+' || operator2 == '-')){
            return true;
        }
        return false;
    }


    //TODO: Remove this.
    public static void print(String string){
        if(string != null) {
            Log.d("LOGGING_HOLA", string);
        }
        else{
            print("null");
        }
    }

    public static void error(String string){
        if(string != null) {
            Log.e("ERROR_HOLA", string);
        }
        else{
            print("null");
        }
    }
    
}
