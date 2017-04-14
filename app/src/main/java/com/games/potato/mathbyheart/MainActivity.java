package com.games.potato.mathbyheart;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.potato.mathbyheart.Utilities.Stack;
import com.games.potato.mathbyheart.math.Expression;
import com.games.potato.mathbyheart.math.Math;
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


     /*   TextView tv = new TextView(this);
        tv.setText("Allo");
        tv.setId(5);
        tv.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(tv);

        TextView tv2 = new TextView(this);
        tv2.setText("xd");
        tv2.setId(5);
        tv2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(tv2);*/

        String patate = "((6+1)*3)/(4+1)";

//        String patate2 = com.games.potato.mathbyheart.math.Math.convertExpressionToSuffix(patate);




       // Operation xd = evaluerSuffixe(patate2);


      //  view.setText(
        Expression hola = Expression.separateNumbers(patate);


        Expression xd = Expression.separateTerms(hola, 0);
        Math.print(xd.toString(false) + " jola");

        Math.print(xd + " XD ");

        Expression a = (Expression) xd.get(0);
        Expression b = (Expression) a.get(0);
        Math.print("hehe " + b.operation + " : " + a.operation + " : " + xd.operation);
        int i = 0;

        String string = "";


        //layout.addView(xd.toLinearLayout(this));

      /*  for(i = 0; i < xd.size(); i++){
            Object obj = xd.get(i);
            if(obj.getClass() == Expression.class){
                Expression expression = (Expression) obj;

            }



            if(xd.get(i).equals("/")){
                TextView tv = new TextView(this);
                tv.setText(string);
                tv.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.addView(tv);

                View divide = new View(this);
                divide.setBackgroundColor(Color.BLACK);
                divide.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                ));

                layout.addView(divide);

               // string = (String) xd.get(i);
            }
            else{
                string += xd.get(i);
            }
            Math.print(xd.get(i) + "   :" + i);
        }
        TextView tv = new TextView(this);
        tv.setText(string);
        tv.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(tv);

*/






              //  System.out.println(com.games.potato.mathbyheart.math.Math.separateNumbers(patate).get(1));//com.games.potato.mathbyheart.math.Math.separateNumbers(patate).toString());

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



