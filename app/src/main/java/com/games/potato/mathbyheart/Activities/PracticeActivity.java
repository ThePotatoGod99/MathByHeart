package com.games.potato.mathbyheart.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.games.potato.mathbyheart.math.Math;
import com.games.potato.mathbyheart.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private MathView mathView;

    private Stack<FormulaList.Formula> knownFormulas;
    private Stack<FormulaList.Formula> unknownFormulas;

    private String dataFileName;

    private int questionNumber;

    Toolbar appToolbar;

    private ArrayList staredItems;


    private FormulaList formulaList;


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
        formulaList = FormulaList.read(
                new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + dataFileName)
        );
        if (formulaList == null) {
            Math.error("ERROR WHILE READING FILE");
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
        if (formulaList.isStarred(questionNumber)) {
            star.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            star.setIcon(R.drawable.ic_star_border_black_24dp);
        }

//        File file = new File(getFilesDir(),
//                getString(R.string.path_default_formulas) + "/" + "starredFormulas.xml");
//
//            /* Write changes to starredFormulas file */
//        FormulaList formula = FormulaList.read(file);
//        if (formula == null) {
//            /*If the file doesn't exist, create a new formula object to write in a new XML file */
//            formula = new FormulaList();
//        } else {
//            /* Delete old file if it exists (it will be replaced with an updated version */
//            file.delete();
//        }
//
//        try {
//            /* Create the file */
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
//            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<formulaList>\n" +
//                    "</formulaList>"
//            );//TODO: Change
//            bufferedWriter.flush();
//            bufferedWriter.close();
//        } catch (Exception e) {
//            System.err.println("Couldn't create file -> FormulaList.java toggleStarred()\n" + e.toString());
//        }
//
//        if (formulaList.isStarred(questionNumber)) {
//                formula.addFormula(formulaList.getFormula(questionNumber));
//        } else if (formula.getFormula(questionNumber).equals(formulaList.getFormula(questionNumber))) {
//            formula.removeFormulaNumber(questionNumber);
//        }
//        formula.write(file);
    }




    /* Buttons */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                File file = new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + "starredFormulas.xml");
                formulaList.toggleStarred(questionNumber, file);
                updateStar();
                return true;

            default:
                Math.error("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();

        if (tag.equals(getString(R.string.btn_known))) {
            knownFormulas.add(formulaList.getFormula(questionNumber));
        } else if (tag.equals(getString(R.string.btn_unknown))) {
            unknownFormulas.add(formulaList.getFormula(questionNumber));
        } else {
            Math.error("ERROR: Wrong button tag: " + tag + getString(R.string.btn_known));
        }

        questionNumber++;
        updateFormula();
        updateStar();


    }

    public void onMathViewPressed(View view) {
        setFormula(formulaList.getOtherSide(questionNumber));
    }

    /* Getters & Setters */

    public void setFormula(String formula) {
        mathView.setText(formula);
    }

    public boolean setFormulaWithID(int id) {
        try {
            mathView.setText(
                    formulaList.getFormulaString(id)
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
