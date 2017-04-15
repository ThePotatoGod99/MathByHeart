package com.games.potato.mathbyheart;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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


        ListView listView = (ListView) findViewById(R.id.list_view);
        String hola[] = {"asdf", "asdf"};

        String items[] = {"Integrals",
                            "Derivatives",
                            "Other"};
        String formulas[] = {"$$\\int{\\frac{1}{1+x^2}}dx$$",
                            "$$\\frac{d}{du}(\\frac{u}{v})$$",
                            "$$\\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$"};
        CustomList customList = new CustomList(this, items, formulas);
        listView.setAdapter(customList);
        /*MathView mathView = new MathView(this, ); //(MathView) findViewById(R.id.formula_one);
        mathView.setText(testString);


        ListView listView = (ListView) findViewById(R.id.list_view);

        ArrayList listViewItems = new ArrayList<WebView>();

        listViewItems.add(0, mathView);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listViewItems);
        listView.setAdapter(adapter);*/
    }

}



