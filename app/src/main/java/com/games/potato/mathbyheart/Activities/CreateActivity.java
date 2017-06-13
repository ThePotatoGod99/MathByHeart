package com.games.potato.mathbyheart.Activities;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.games.potato.mathbyheart.Data.FormulaList;
import com.games.potato.mathbyheart.R;
import com.games.potato.mathbyheart.math.Xd;

import java.io.File;

import io.github.kexanie.library.MathView;


public class CreateActivity extends AppCompatActivity {

    private int questionNumber;


    private MathView mathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);

        mathView = (MathView) findViewById(R.id.math_view);


        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString());
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);

        questionNumber = 0;

        setupTextBoxes();

    }


    @Override
    protected void onStart() {
        super.onStart();
        ((MathView) findViewById(R.id.math_view_question)).setText("$$\\frac{3}{4}$$");
        ((MathView) findViewById(R.id.math_view_answer)).setText("$$\\frac{43}{46}$$");

//        if (readData()) {
        updateFormula(0);
//        } else {
//            finish();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        return true;
    }


    private void setupTextBoxes() {

        EditText editText_question = (EditText) findViewById(R.id.textBox_question);
        EditText editText_answer = (EditText) findViewById(R.id.textBox_answer);

        editText_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((MathView) findViewById(R.id.math_view_question)).setText(
                        ((EditText) findViewById(R.id.textBox_question)).getText().toString());
                updateFormula(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((MathView) findViewById(R.id.math_view_answer)).setText(
                        ((EditText) findViewById(R.id.textBox_answer)).getText().toString());
                updateFormula(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void updateFormula(int animation) {
       /* 0: None, 1: Left, 2:Right, 3:shake 4: Flip*/
//        PracticeActivity.Card card = new PracticeActivity.Card();
//
//        FormulaList.Formula formula = (currentCard.getFormula() == null) ?
//                new FormulaList.Formula("", "") :
//                currentCard.getFormula();
//
//        card.setFormula(formula);
//        card.setFront(currentCard.isFront());
//
//        FragmentTransaction fragmentTrans = getFragmentManager()
//                .beginTransaction();
//        switch (animation) {
//            case 1:
//                fragmentTrans.setCustomAnimations(
//                        R.animator.card_swipe_left_in,
//                        R.animator.card_swipe_left_out);
//                break;
//            case 2:
//                fragmentTrans.setCustomAnimations(
//                        R.animator.card_swipe_right_in,
//                        R.animator.card_swipe_right_out);
//                break;
//            case 3:
//                ObjectAnimator
//                        .ofFloat(findViewById(R.id.container), "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
//                        .setDuration(1000)
//                        .start();
//                break;
//            case 4:
//                fragmentTrans.setCustomAnimations(
//                        R.animator.card_flip_right_in,
//                        R.animator.card_flip_right_out);
//                break;
//            default:
//                break;
//        }
//
//        fragmentTrans.replace(R.id.container, card)
//                .commit();


    }


    /* Buttons */


//    public void onMathViewPressed(View view) {
//        flipCard();
//    }

    /* Getters & Setters */


    public static class CreateCard extends Fragment {
        private boolean isFront = true;


        private FormulaList.Formula formula;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_card, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();
            updateText();
        }

        public String getText() {
            return isFront ? formula.getQuestion() : formula.getAnswer();
        }

        private void updateText() {
            ((MathView) getView().findViewById(R.id.math_view)).setText(getText());
        }


        public boolean isFront() {
            return isFront;
        }

        public void setFront(boolean front) {
            isFront = front;
            if (!(getView() == null)) {
                updateText();
            }
        }

        public FormulaList.Formula getFormula() {
            return formula;
        }

        public void setFormula(FormulaList.Formula formula) {
            this.formula = formula;
        }
    }


}
