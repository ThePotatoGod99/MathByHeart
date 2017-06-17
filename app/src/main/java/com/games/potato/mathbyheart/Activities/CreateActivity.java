package com.games.potato.mathbyheart.Activities;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.text.Normalizer;

import io.github.kexanie.library.MathView;


public class CreateActivity extends AppCompatActivity {

    private int questionNumber;


    private MathView mathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);
//        setContentView(R.layout.activity_practice);

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


        updateFormula(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.practice_menu, menu);
        return true;
    }


    private void setupTextBoxes() {


//        EditText editText_question = (EditText) findViewById(R.id.textBox_question);
//        EditText editText_answer = (EditText) findViewById(R.id.textBox_answer);
//
//        editText_question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                ((MathView) findViewById(R.id.math_view_answer)).setVisibility(
////                        hasFocus ?
////                                View.INVISIBLE :
////                                View.VISIBLE
////                );
//
//
//            }
//        });
//
//        editText_question.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ((MathView) findViewById(R.id.math_view_question)).setText(
//                        ((EditText) findViewById(R.id.textBox_question)).getText().toString());
//                updateFormula(0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
////        (MathView) findViewById(R.id.math_view_question).setto
//        editText_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                ((MathView) findViewById(R.id.math_view_question)).setVisibility(
////                        hasFocus ?
////                                View.INVISIBLE :
////                                View.VISIBLE
////                );
//
//
//            }
//        });
//
//
//        editText_answer.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ((MathView) findViewById(R.id.math_view_answer)).setText(
//                        ((EditText) findViewById(R.id.textBox_answer)).getText().toString());
//                updateFormula(0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    public void updateFormula(int animation) {


        CreateCard card = new CreateCard();
        FormulaList.Formula formula = new FormulaList.Formula("$$\\frac{3}{4}$$", "$$\\text(Answer here)$$");
        card.setFormula(formula);

        Xd.print("ASDFASDF");

        Xd.print(card.getFormula().toString());


        card.setFront(true);

        findViewById(R.id.create_container).setBackgroundColor(Color.YELLOW);

        FragmentTransaction fragmentTrans = getFragmentManager()
                .beginTransaction();





        fragmentTrans.setCustomAnimations(
                R.animator.card_flip_right_in,
                R.animator.card_flip_right_out);


        fragmentTrans.replace(R.id.create_container, card)
                .commit();






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



    /* Getters & Setters */


    public static class CreateCard extends Fragment {
        private boolean isFront = true;


        private FormulaList.Formula formula;
        private Context context;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Xd.print("On create view");
            return inflater.inflate(R.layout.fragment_card, container, false);
        }

        @Nullable
        @Override
        public View getView() {
            View view = super.getView();
            if (view != null) {
                view.setBackgroundColor(Color.RED);
            }
            Xd.print("GET VIEW");


            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Xd.print("on view created create view");
            getView().setBackgroundColor(Color.GREEN);
            updateText();
        }

        public String getText() {
            Xd.print(getFormula().toString() + " ASDF ");


            return isFront ? formula.getQuestion() : formula.getAnswer();
        }


        private void updateText() {
            ((MathView) getView().findViewById(R.id.math_view)).setText(getText());
            if (!isFront) {
                ((TextView) getView().findViewById(R.id.textView)).setText("Press to show the question");
            }
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
