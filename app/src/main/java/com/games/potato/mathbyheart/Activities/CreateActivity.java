package com.games.potato.mathbyheart.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.games.potato.mathbyheart.Data.FormulaList;
import com.games.potato.mathbyheart.R;
import com.games.potato.mathbyheart.math.Xd;

import java.io.File;


public class CreateActivity extends AppCompatActivity {

    static final String EXTRA_QUESTION_NUMBER = "EXTRA_QUESTION_NUMBER";

    EditText editText;

    private FormulaList formulaList;

    private FormulaList.Formula currentFormula;
    private boolean isFront = true;

    private int questionNumber;

    private String dataFileName = "";
    //TODO: Fix editing when random


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);

        dataFileName = getIntent().getDataString();

        questionNumber = getIntent().getIntExtra(EXTRA_QUESTION_NUMBER, 0);
        if (dataFileName == null) {
            formulaList = new FormulaList();
            currentFormula = new FormulaList.Formula("", "");
        } else {
            formulaList = FormulaList.read(
                    new File(getFilesDir(),
                            getString(R.string.path_default_formulas) + "/" + dataFileName)
            );
            currentFormula = formulaList.get(questionNumber);


        }


        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        appToolbar.setTitle(getIntent().getDataString() + " (EDIT)");
        appToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appToolbar);


        editText = (EditText) findViewById(R.id.textBox);
        editText.setText(currentFormula.getFormula(isFront));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = editText.getText().toString();
                if (isFront) {
                    currentFormula.setQuestion(string);

                } else {
                    currentFormula.setAnswer(string);
                }
                updateFormula(0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateFormula(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        return true;
    }


    public void updateFormula(int animation) {
        PracticeActivity.Card card = new PracticeActivity.Card();
        card.setFormula(currentFormula);
        card.setFront(isFront);

        card.animateReplacement(this, animation, R.id.create_container);
    }

    public void updateFormulaList() {
        if (!currentFormula.isEmpty()) {
            if (formulaList.size() > questionNumber) {
                formulaList.set(questionNumber, currentFormula);
            } else {
                formulaList.add(questionNumber, currentFormula);
            }
        }
    }

    public void changeQuestion(boolean direction) {/* true: next, false:previous */
        if (!((questionNumber == 0 && !direction) || /* Do nothing if the user tries to access a negative question */
                (currentFormula.isEmpty() && direction))) { /* Do nothing if the user tries to save an empty formula */
            /* Save current formula */
            updateFormulaList();

            /* Update the card to display the correct formula */
            if (direction) {/* Next */
                questionNumber++;
                if (formulaList.size() > questionNumber) {
                    currentFormula = formulaList.get(questionNumber);
                } else {
                    currentFormula = new FormulaList.Formula("", "");
                }
                updateFormula(1);
            } else {/* Previous */
                questionNumber--;
                currentFormula = formulaList.get(questionNumber);
                updateFormula(2);
            }

            isFront = true;

            String text = currentFormula.getQuestion();
            editText.setText(text);
            editText.setHint(getString(R.string.hint_question_input));
        } else {
            updateFormula(3);
        }
    }

    public void save() {
        //TODO: If file exists overwrite dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save to file named:");
        final EditText input = new EditText(this);
        input.setText(dataFileName);

        builder.setView(input);

        builder.setPositiveButton(getString(R.string.action_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateFormulaList();
                formulaList.write(
                        new File(getFilesDir(),
                                getString(R.string.path_default_formulas) +
                                        "/" +
                                        input.getText().toString()));
                Toast.makeText(CreateActivity.this, "Saved", Toast.LENGTH_SHORT).show();//TODO: Translate
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    /* Buttons */

    public void onMathViewPressed(View view) {
        isFront = !isFront;

        String text = currentFormula.getFormula(isFront);
        Xd.print(text);
        editText.setText(text);
        if (isFront) {
            editText.setHint(getString(R.string.hint_question_input));
        } else {
            editText.setHint(getString(R.string.hint_answer_input));
        }

        getFragmentManager().executePendingTransactions();
        updateFormula(4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                changeQuestion(false);

                break;
            case R.id.action_delete:
                changeQuestion(true);

                break;
            case R.id.action_help:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.help_url)));
                startActivity(browserIntent);
                break;
            case R.id.action_save:
                save();
                break;
            default:
                Xd.error("ERROR: Action not recognised in onOptionsItemSelected(): " + item.toString() + " id: " + item.getItemId());
                return super.onOptionsItemSelected(item);
        }
        return true;

    }


    /* Getters & Setters */



    public static Intent getIntent(Context context, String dataFileName, int questionNumber) {
        Intent intent = new Intent(context, CreateActivity.class);
        intent.setData(Uri.parse(dataFileName));
        intent.putExtra(CreateActivity.EXTRA_QUESTION_NUMBER, questionNumber);
        return intent;
    }
}
