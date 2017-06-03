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

import com.games.potato.mathbyheart.Data.FormulaList;
import com.games.potato.mathbyheart.math.Xd;
import com.games.potato.mathbyheart.R;

import java.io.File;
import java.util.Random;
import java.util.Stack;

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private MathView mathView;

    private Stack<FormulaList.Formula> knownFormulas;
    private Stack<FormulaList.Formula> unknownFormulas;

    private String dataFileName;

    private int questionNumber;

    Toolbar appToolbar;


    private FormulaList formulaList;
    private FormulaList starredFormulaList;


    public void init() {
        setContentView(R.layout.activity_practice);

        appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString());
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);

        mathView = (MathView) findViewById(R.id.math_view);
        mathView.setEngine(MathView.Engine.KATEX);

        knownFormulas = new Stack();
        unknownFormulas = new Stack();


        questionNumber = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formulaList = new FormulaList();
        starredFormulaList = new FormulaList();

        dataFileName = getIntent().getDataString();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (readData()) {
            setFormulaWithID(questionNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        updateStar();
        return true;
    }


    public boolean readData() {
        formulaList = FormulaList.read(
                new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + dataFileName)
        );

        if (formulaList == null || formulaList.isEmpty()) {
            Xd.error("ERROR WHILE READING FILE");
            Toast.makeText(PracticeActivity.this, "No formulas in this section", Toast.LENGTH_SHORT).show();
            this.onBackPressed();
            return false;
        }

        starredFormulaList = FormulaList.read(
                new File(getFilesDir(),
                        getString(R.string.path_starred_list))
        );
        if (starredFormulaList == null) {
            starredFormulaList = new FormulaList();
        }
        return true;


    }

    public void randomize() {
        FormulaList temp = new FormulaList();
        Random random = new Random();
        int number = 0;
        while (!formulaList.isEmpty()) {

            if (formulaList.size() == 1) {
                number = 0;
            } else {
                number = (int) (Math.random() * formulaList.size());

            }
            temp.add(formulaList.remove(number));
        }

        formulaList = temp;
        questionNumber = 0;
        updateFormula();
        updateStar();

    }


    public boolean updateFormula() {
        return setFormulaWithID(questionNumber);
    }

    public void updateStar() {
        Xd.print(formulaList.get(0).toString());

        updateStar(starredFormulaList.contains(
                formulaList.get(questionNumber
                )));
    }

    public void updateStar(boolean toggledOn) {
        MenuItem star = appToolbar.getMenu().findItem(R.id.action_favorite);
        if (toggledOn) {
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


                boolean toggledOn = false;
                if (!starredFormulaList.remove(formulaList.get(questionNumber))) {
                    /* If starredFormulaList contains the current question, remove it. Else, add it */
                    starredFormulaList.add(formulaList.get(questionNumber));
                    toggledOn = true;
                }


                updateStar(toggledOn);
                setFormulaWithID(questionNumber);

                starredFormulaList.write(starredFormulaList.getSourceFile());
                return true;

            case R.id.action_randomize:
                randomize();
                return true;


            default:
                Xd.error("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();

        if (tag.equals(getString(R.string.btn_2_label))) {
            questionNumber++;
        } else if (tag.equals(getString(R.string.btn_1_label))) {
            questionNumber--;
            if (questionNumber < 0) {
                questionNumber = formulaList.size() - 1;
            }
        }

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

    public void setFormulaList(FormulaList formulaList) {
        this.formulaList = formulaList;
    }

    public FormulaList getFormulaList() {
        return formulaList;
    }
}
