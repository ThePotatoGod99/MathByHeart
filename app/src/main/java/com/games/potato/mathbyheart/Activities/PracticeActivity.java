package com.games.potato.mathbyheart.Activities;

import android.app.Fragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.games.potato.mathbyheart.Data.FormulaList;
import com.games.potato.mathbyheart.math.Xd;
import com.games.potato.mathbyheart.R;

import java.io.File;
import java.util.Random;
import java.util.Stack;

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private String dataFileName;

    private int questionNumber;

    private FormulaList formulaList;
    private FormulaList starredFormulaList;

    private Card frontCard;
    private Card backCard;
    private Card currentCard;/* Reference to the current visible card (Pointer to frontCard or backCard) */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        frontCard = new Card();
        backCard = new Card();
        backCard.setFront(false);
        currentCard = frontCard;

        formulaList = new FormulaList();
        starredFormulaList = new FormulaList();

        dataFileName = getIntent().getDataString();

        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString());
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);

        questionNumber = 0;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (readData()) {
            updateFormula();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        if (!isFinishing()) {
            updateStar();
        }
        return true;
    }


    public boolean readData() {
        formulaList = FormulaList.read(
                new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + dataFileName)
        );
        if (formulaList == null || formulaList.isEmpty()) {
            if (dataFileName.equals("starredList.xml")) {//TODO: Change

                Toast.makeText(PracticeActivity.this, "No starred formulas. Press the star at a difficult question to star it", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(PracticeActivity.this, "No formulas in this section", Toast.LENGTH_SHORT).show();
            }
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
        Toast.makeText(PracticeActivity.this, "Randomized", Toast.LENGTH_SHORT).show();
    }

    private void flipCard() {
        currentCard = currentCard.isFront() ? backCard : frontCard;
        updateFormula();
    }

    public boolean updateFormula() {
        boolean result = setFormulaWithID(questionNumber);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.container, currentCard)
                .commit();
        return result;
    }

    public void updateStar() {
        updateStar(starredFormulaList.contains(
                formulaList.get(questionNumber
                )));
    }

    public void updateStar(boolean toggledOn) {
        MenuItem star = ((Toolbar) findViewById(R.id.app_toolbar)).getMenu().findItem(R.id.action_favorite);
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
                currentCard = frontCard;
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
        currentCard = frontCard;

        updateFormula();
        updateStar();
    }

    public void onMathViewPressed(View view) {
        flipCard();
    }

    /* Getters & Setters */

    public void setFormulaList(FormulaList formulaList) {
        this.formulaList = formulaList;
    }

    public FormulaList getFormulaList() {
        return formulaList;
    }

    public boolean setFormulaWithID(int id) {
        try {
            FormulaList.Formula formula = formulaList.getFormula(id);
            /* This also changes the text for currentCard because it's always pointing to the frontCard or the backCard */
            frontCard.setText(
                    formula.getQuestion()
            );
            backCard.setText(
                    formula.getAnswer()
            );

        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(PracticeActivity.this, "Going back to start", Toast.LENGTH_SHORT).show();
            questionNumber = 0; //TODO: change
            setFormulaWithID(questionNumber);
            return false;
        }
        return true;
    }


    /* Classes */
    public static class Card extends Fragment {
        private String text;
        private boolean isFront = true;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();
            updateText();
        }

        public void setText(String text) {
            this.text = text;
            if (!(getView() == null)) {
                updateText();
            }

        }

        public String getText() {
            return text;
        }

        private void updateText() {
            ((MathView) getView().findViewById(R.id.math_view)).setText(text);
            if(!isFront){
                ((TextView) getView().findViewById(R.id.textView)).setText("Press to show the question");
            }
        }


        public boolean isFront() {
            return isFront;
        }

        public void setFront(boolean front) {
            isFront = front;
        }
    }

}
