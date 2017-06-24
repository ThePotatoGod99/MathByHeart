package com.games.potato.mathbyheart.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
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

import io.github.kexanie.library.MathView;


public class PracticeActivity extends AppCompatActivity {
    private String dataFileName;

    private int questionNumber;

    private FormulaList formulaList;
    private FormulaList starredFormulaList;

    private Card currentCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        currentCard = new Card();

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
            updateFormula(4);
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
            if (dataFileName.equals("starredList")) {//TODO: Change
                Toast.makeText(PracticeActivity.this, "No starred formulas. Press the star at a difficult question to star it", Toast.LENGTH_LONG).show();//TODO: Translate
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
        int number;
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
        updateFormula(3);
        updateStar();
        Toast.makeText(PracticeActivity.this, "Randomized", Toast.LENGTH_SHORT).show();
    }

    private void flipCard() {
        currentCard.setFront(!currentCard.isFront());
        updateFormula(4);
    }

    public boolean updateFormula(int animation) {
        boolean result = setFormulaWithID(questionNumber);
        Card card = new Card();
        card.setFormula(currentCard.getFormula());
        card.setFront(currentCard.isFront());


        card.animateReplacement(this, animation, R.id.container);


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
                currentCard.setFront(true);
                randomize();
                return true;


            default:
                Xd.error("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }

    }

    public void onButtonPressed(View view) {
        String tag = view.getTag().toString();

        int direction = 0;
        if (tag.equals(getString(R.string.btn_2_label))) {
            direction = 1;
            questionNumber++;
        } else if (tag.equals(getString(R.string.btn_1_label))) {
            direction = 2;
            questionNumber--;
            if (questionNumber < 0) {
                questionNumber = formulaList.size() - 1;
            }
        }
        currentCard.setFront(true);

        updateFormula(direction);
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
            currentCard.setFormula(formula);
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(PracticeActivity.this, "Going back to start", Toast.LENGTH_SHORT).show();
            questionNumber = 0;
            setFormulaWithID(questionNumber);
            return false;
        }
        return true;
    }


    /* Classes */
    public static class Card extends Fragment {
        private boolean isFront = true;


        private FormulaList.Formula formula;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            updateText();
        }

        public String getText() {
            return isFront ? formula.getQuestion() : formula.getAnswer();
        }


        private void updateText() {
            ((MathView) getView().findViewById(R.id.math_view)).setText(getText());
            if (!isFront) {
                ((TextView) getView().findViewById(R.id.textView))
                        .setText("Press to show the question");
            }
        }

        public FragmentTransaction animateReplacement(Activity context, int animation, int containerToReplace) {
        /* 0: None, 1: Left, 2:Right, 3:shake 4: Flip*/
            FragmentTransaction fragmentTrans = context.getFragmentManager()
                    .beginTransaction();


            Xd.print(animation + " : ");
            switch (animation) {
                case 1:
                    fragmentTrans.setCustomAnimations(
                            R.animator.card_swipe_left_in,
                            R.animator.card_swipe_left_out);
                    break;
                case 2:
                    fragmentTrans.setCustomAnimations(
                            R.animator.card_swipe_right_in,
                            R.animator.card_swipe_right_out);
                    break;
                case 3:
                    ObjectAnimator
                            .ofFloat(context.findViewById(containerToReplace), "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                            .setDuration(1000)
                            .start();
                    break;
                case 4:
                    fragmentTrans.setCustomAnimations(
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out);
                    break;
                default:
                    break;
            }

            fragmentTrans.replace(containerToReplace, this)
                    .commit();
            return fragmentTrans;
        }


        public boolean isFront() {
            return isFront;
        }

        public void setFront(boolean front) {
            isFront = front;
            if (!(getView() == null)) {
//                updateText();
            }
        }

        public FormulaList.Formula getFormula() {
            return formula;
        }

        public void setFormula(FormulaList.Formula formula) {
            this.formula = formula;
        }

        public void setQuestion(String question) {
            formula.setQuestion(question);
        }

        public void setAnswer(String answer) {
            formula.setAnswer(answer);
        }
    }

}
