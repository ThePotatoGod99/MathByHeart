package com.games.potato.mathbyheart.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.games.potato.mathbyheart.math.Math;
import com.games.potato.mathbyheart.R;

import io.github.kexanie.library.MathView;

public class PracticeActivity extends AppCompatActivity {


    private MathView mathView;


    private String formula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Toast.makeText(PracticeActivity.this, getIntent().getDataString(), Toast.LENGTH_SHORT).show();

        mathView = (MathView) findViewById(R.id.math_view);
        mathView.setEngine(MathView.Engine.KATEX);

        setFormula("$$\\int{\\frac{1+x*3}{(x+1)*3+2}}dx$$");


    }


    //Getters & Setters

    public MathView getMathView() {
        return mathView;
    }

    public void setMathView(MathView mathView) {
        this.mathView = mathView;
    }


    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
        mathView.setText(formula);
    }
}
