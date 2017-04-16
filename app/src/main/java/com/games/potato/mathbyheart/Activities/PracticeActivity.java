package com.games.potato.mathbyheart.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Stack;

import io.github.kexanie.library.MathView;



public class PracticeActivity extends AppCompatActivity {
    private MathView mathView;

    private ArrayList<String> questions;
    private ArrayList<String> answers;

    private Stack knownFormulas;
    private Stack unknownFormulas;

    private String dataFileName;

    private int questionNumber;
    private boolean questionsFirst = true;
    private boolean showingQuestion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Toast.makeText(PracticeActivity.this, getIntent().getDataString(), Toast.LENGTH_SHORT).show();


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
    }



    /* Buttons */

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();

        questionNumber++;
        updateFormula();

        showingQuestion = questionsFirst;

        if (tag.equals(getString(R.string.btn_known))) {
            knownFormulas.add(questions.get(questionNumber));
        } else if (tag.equals(getString(R.string.btn_unknown))) {
            unknownFormulas.add(questions.get(questionNumber));
        } else {
            Math.print("ERROR: Wrong button tag: " + tag + getString(R.string.btn_known));
        }
    }

    public void onMathViewPressed(View view) {
        Math.print("PRESSED "+questionNumber + " : " + showingQuestion );
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

    public boolean updateFormula() {
        return setFormulaWithID(questionNumber, questionsFirst);
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
