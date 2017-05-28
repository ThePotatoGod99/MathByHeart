package com.games.potato.mathbyheart.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.games.potato.mathbyheart.math.Math;
import com.games.potato.mathbyheart.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private MathView mathView;

    private Stack<Formulas.Formula> knownFormulas;
    private Stack<Formulas.Formula> unknownFormulas;

    private String dataFileName;

    private int questionNumber;

    Toolbar appToolbar;

    private ArrayList staredItems;


    private Formulas formulas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Toast.makeText(PracticeActivity.this, getIntent().getDataString(), Toast.LENGTH_SHORT).show();

        appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString());
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);


        mathView = (MathView) findViewById(R.id.math_view);
        mathView.setEngine(MathView.Engine.KATEX);

        knownFormulas = new Stack();
        unknownFormulas = new Stack();

        dataFileName = getIntent().getDataString();

        updateData();

        questionNumber = 0;


    }

    @Override
    protected void onStart() {
        super.onStart();

        setFormulaWithID(questionNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        updateStar();
        return true;
    }


    public void updateData() {
        formulas = Formulas.read(
                new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + dataFileName)
        );
        if (formulas == null) {
            Math.print("ERROR WHILE READING FILE");
            Toast.makeText(PracticeActivity.this, "ERROR WHILE READING FILE: " + dataFileName, Toast.LENGTH_SHORT).show();
            this.onBackPressed();
        }



        staredItems = new ArrayList<>();
    }


    public boolean updateFormula() {
        return setFormulaWithID(questionNumber);
    }

    public void updateStar() {
        MenuItem star = appToolbar.getMenu().findItem(R.id.action_favorite);
        if (formulas.isStarred(questionNumber)) {
            star.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            star.setIcon(R.drawable.ic_star_border_black_24dp);
        }
    }


    /* Buttons */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                File file = new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + "starredFormulas.xml");
                formulas.toggleStarred(questionNumber, file);
                updateStar();
                return true;

            default:
                Math.print("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();

        if (tag.equals(getString(R.string.btn_known))) {
            knownFormulas.add(formulas.getFormula(questionNumber));
        } else if (tag.equals(getString(R.string.btn_unknown))) {
            unknownFormulas.add(formulas.getFormula(questionNumber));
        } else {
            Math.print("ERROR: Wrong button tag: " + tag + getString(R.string.btn_known));
        }

        questionNumber++;
        updateFormula();
        updateStar();


    }

    public void onMathViewPressed(View view) {
        setFormula(formulas.getOtherSide(questionNumber));
    }

    /* Getters & Setters */

    public void setFormula(String formula) {
        mathView.setText(formula);
    }

    public boolean setFormulaWithID(int id) {
        try {
            mathView.setText(
                    formulas.getFormulaString(id)
            );

        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(PracticeActivity.this, "Going back to start", Toast.LENGTH_SHORT).show();
            questionNumber = 0; //TODO: change
            setFormulaWithID(questionNumber);
            return false;
        }
        return true;
    }
}
