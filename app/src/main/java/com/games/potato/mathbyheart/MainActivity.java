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
import com.games.potato.mathbyheart.math.Math;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String testString = "$$\\int{\\frac{1+x*3}{(x+1)*3+2}}dx$$";
        MathView mathView = (MathView) findViewById(R.id.formula_one);
        mathView.setText(testString);
    }

}



