package com.games.potato.mathbyheart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.potato.mathbyheart.Utilities.Math;
import com.games.potato.mathbyheart.Utilities.Stack;
import com.games.potato.mathbyheart.textBookStyle.Operation;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);







        TextView view = (TextView) findViewById(R.id.text);
        view.setText("∫x+1");


        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);


        String patate = "((6+1)*3)/(4+1)";

//        String patate2 = Math.convertExpressionToSuffix(patate);




       // Operation xd = evaluerSuffixe(patate2);


      //  view.setText(
        ArrayList<String> hola = Math.separateNumbers(patate);


        Math.print(hola + " jola");
        ArrayList<Object> xd = Math.test(hola, 0);

        Math.print(xd + " XD ");
        int i = 0;
        for(i = 0; i < xd.size(); i++){
            Math.print(xd.get(i) + "   :" + i);
        }




              //  System.out.println(Math.separateNumbers(patate).get(1));//Math.separateNumbers(patate).toString());

        //Operation aa;// = new Operation()
     //   Operation x = new Operation(34);
       // Operation y = new Operation()
        //view.setText(xd.toString());


    /*    int nb1 = 0;
        int nb2 = 0;

        int aaa = 0;

        for(int i = 0; i <patate.length(); i++){
            char character = patate.charAt(i);
            int number = Character.getNumericValue(character);

            switch (aaa){
                case 0:
                    nb1 = number;
                    break;
                case 1:
                    nb2 = number;
                    break;
                case 2:


                    aaa = -1;
                    break;
            }



            aaa++;

        }*/



    }



    public static Operation evaluerSuffixe(String suffixe){
        Character courant;
        Stack stack = new Stack<Operation>();

        int j = 0;
        Operation operation = new Operation();
        for(int i = 0; i < suffixe.length(); i++){


            courant = suffixe.charAt(i);
            if(!Math.isOperator(courant)){



                stack.stack(new Operation(courant.toString()));
                j++;

            }
            else{
                Operation y = (Operation) stack.unstack();
                Operation x = (Operation) stack.unstack();


                operation.setTerm1(y);
                operation.setTerm2(x);
                operation.setOperator(courant);
                stack.stack(operation);

            }
        }
        return (Operation) stack.look();

    }



}
