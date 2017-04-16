package com.games.potato.mathbyheart.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import com.games.potato.mathbyheart.math.Math;
import com.games.potato.mathbyheart.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private MathView mathView;

    private ArrayList<String> questions;
    private ArrayList<String> answers;

    private Stack<String> knownFormulas;
    private Stack<String> unknownFormulas;

    private String dataFileName;

    private int questionNumber;
    private boolean questionsFirst = true;
    private boolean showingQuestion = true;

    Toolbar appToolbar;

    private ArrayList<Boolean> staredItems;

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

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        dataFileName = getIntent().getDataString();

        updateData();

        questionNumber = 0;


    }

    @Override
    protected void onStart() {
        super.onStart();

        setFormulaWithID(questionNumber, questionsFirst);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(!(staredItems.size() > questionNumber)){
                    staredItems.add(questionNumber, true);
                    item.setIcon(R.drawable.ic_star_black_24dp);
                }
                else if (staredItems.get(questionNumber)) {
                    item.setIcon(R.drawable.ic_star_border_black_24dp);
                    staredItems.set(questionNumber, false);
                }
                else{
                    item.setIcon(R.drawable.ic_star_black_24dp);
                    staredItems.set(questionNumber, true);
                }
                return true;

            default:
                Math.print("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    public void updateData() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            getAssets().open(dataFileName)
                    ));

            String line;
            while ((line = br.readLine()) != null) {
                questions.add(line);
                answers.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            Math.print("ERROR WHILE READING FILE");
            Toast.makeText(PracticeActivity.this, "ERROR WHILE READING FILE: " + dataFileName, Toast.LENGTH_SHORT).show();
            this.onBackPressed();
        }
        staredItems = new ArrayList<>();
    }


    public boolean updateFormula() {
        return setFormulaWithID(questionNumber, questionsFirst);
    }


    /* Buttons */

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();


        showingQuestion = questionsFirst;

        if (tag.equals(getString(R.string.btn_known))) {
            knownFormulas.add(questions.get(questionNumber));
        } else if (tag.equals(getString(R.string.btn_unknown))) {
            unknownFormulas.add(questions.get(questionNumber));
        } else {
            Math.print("ERROR: Wrong button tag: " + tag + getString(R.string.btn_known));
        }
        questionNumber++;
        updateFormula();
    }

    public void onMathViewPressed(View view) {
        Math.print("PRESSED " + questionNumber + " : " + showingQuestion);
        if (showingQuestion) {
            showingQuestion = false;
        } else {
            showingQuestion = true;
        }
        setFormulaWithID(questionNumber, showingQuestion);
    }

    /* Getters & Setters */

    public MathView getMathView() {
        return mathView;
    }

    public void setMathView(MathView mathView) {
        this.mathView = mathView;
    }


    public void setFormula(String formula) {
        mathView.setText(formula);
    }

    public boolean setFormulaWithID(int id, boolean useQuestions) {
        try {
            String text;
            if (useQuestions) {
                text = questions.get(id);
            } else {
                text = answers.get(id);
            }
            mathView.setText(text);

        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(PracticeActivity.this, "Going back to start", Toast.LENGTH_SHORT).show();


            questionNumber = 0; //TODO: change
            setFormulaWithID(questionNumber, useQuestions);

            return false;
        }
        return true;
    }
}
