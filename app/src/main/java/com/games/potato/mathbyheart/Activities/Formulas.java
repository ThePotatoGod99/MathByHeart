package com.games.potato.mathbyheart.Activities;

import android.app.Activity;
import android.renderscript.ScriptGroup;

import com.games.potato.mathbyheart.R;
import com.games.potato.mathbyheart.math.Math;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2017-04-16.
 */

@Root(name = "formulas")
public class Formulas {
    /* Elements */
    @ElementList(required = false, inline = true)
    private ArrayList<Formula> list;

    @Element(name = "table", required = false)
    private Table table;


    private boolean questionsFirst = true; /* Should show questions first */
    private boolean showingQuestion = true; /* Last formula returned was a question */


    public Formulas() {
        this.list = new ArrayList<>();
        this.table = new Table();
    }

    public static Formulas read(InputStream inputStream) {
        try {
            Serializer ser = new Persister();
            Formulas formulas = ser.read(Formulas.class, inputStream);
            return formulas;
        } catch (Exception e) {
            Math.print(e.toString());
            return null;
        }

    }


    /* Getters & Setters */
    public String getFormulaString(int id) {
        return getFormulaString(id, questionsFirst);
    }

    public String getFormulaString(int id, boolean showQuestion) {
        showingQuestion = showQuestion;
        if (showQuestion) {
            return getFormula(id).getQuestion();
        } else {
            return getFormula(id).getAnswer();
        }
    }

    public Formula getFormula(int id) {
        if (table != null) {
            Math.print("table is not null + " + list);
            if (list.isEmpty()) {
                for (int x = table.getStartNumber(); x <= table.getEndNumber(); x++) {
                    for (int y = table.getStartNumber(); y <= table.getEndNumber(); y++) {
                        list.add(new Formula(
                                "$$" + x + table.getOperation() + y + "$$",
                                "$$" + table.getAnswer(x, y).toString() + "$$",
                                false
                        ));
                        //TODO: make table entries starrable
                    }
                }
            }

            return list.get(id);
        } else {
            return list.get(id);
        }
    }


    public String getOtherSide(int id) {
        return getFormulaString(id, !showingQuestion);
    }

    public void setQuestionsFirst(boolean questionsFirst) {
        this.questionsFirst = questionsFirst;
    }

    public void toggleStarred(int id) {
        setStarred(id, !isStarred(id));
    }

    public void setStarred(int id, boolean starred) {
        list.get(id).setStarred(starred);
    }

    public boolean isStarred(int id) {
        return list.get(id).isStarred();
    }


    public static class Formula {
        @Element(name = "question")
        private String question;
        @Element(name = "answer")
        private String answer;
        @Element(name = "starred")
        private boolean starred;


        public Formula(String question, String answer, boolean starred) {
            this.question = question;
            this.answer = answer;
            this.starred = starred;
        }

        private Formula() {
        }

        /* Getters & Setters */

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean isStarred() {
            return starred;
        }

        public void setStarred(boolean starred) {
            this.starred = starred;
        }
    }

    public static class Table {
        @Element(name = "operation")
        private String operation;
        @Element(name = "startNumber")
        private int startNumber;
        @Element(name = "endNumber")
        private int endNumber;

        public Table(String operation, int startNumber, int endNumber) {
            Math.print("TABLE");
            this.operation = operation;
            this.startNumber = startNumber;
            this.endNumber = endNumber;
        }

        private Table() {
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public Double getAnswer(double number1, double number2) {
            if (operation.equals("\\times")) {
                return number1 * number2;
            }
            return new Double(0);//TODO
        }

        public int getStartNumber() {
            return startNumber;
        }

        public void setStartNumber(int startNumber) {
            this.startNumber = startNumber;
        }

        public int getEndNumber() {
            return endNumber;
        }

        public void setEndNumber(int endNumber) {
            this.endNumber = endNumber;
        }
    }

}
