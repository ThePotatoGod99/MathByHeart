package com.games.potato.mathbyheart.Activities;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
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


    private PracticeActivity.Card currentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        currentCard = new PracticeActivity.Card();


        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString());
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);

        questionNumber = 0;

        EditText editText = (EditText) findViewById(R.id.textBox);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentCard.setFormula(new FormulaList.Formula(((EditText) findViewById(R.id.textBox)).getText().toString(), ""));
                updateFormula(0);
                Xd.print("HOLA");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                currentCard.setFormula(new FormulaList.Formula(((EditText) findViewById(R.id.textBox)).getText().toString(), ""));
                updateFormula(0);
                Xd.print("HOLA");
                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

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


    private void flipCard() {
        currentCard.setFront(!currentCard.isFront());
        updateFormula(0);
    }

    public void updateFormula(int animation) {
       /* 0: None, 1: Left, 2:Right, 3:shake 4: Flip*/
        PracticeActivity.Card card = new PracticeActivity.Card();

        FormulaList.Formula formula = (currentCard.getFormula() == null) ?
                new FormulaList.Formula("", "") :
                currentCard.getFormula();

        card.setFormula(formula);
        card.setFront(currentCard.isFront());

        FragmentTransaction fragmentTrans = getFragmentManager()
                .beginTransaction();
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
                        .ofFloat(findViewById(R.id.container), "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
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

        fragmentTrans.replace(R.id.container, card)
                .commit();


    }


    /* Buttons */



    public void onMathViewPressed(View view) {
        flipCard();
    }

    /* Getters & Setters */


}
