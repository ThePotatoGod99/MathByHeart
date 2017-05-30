package com.games.potato.mathbyheart.Activities;

import android.support.annotation.NonNull;

import com.games.potato.mathbyheart.math.Math;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Simon on 2017-04-16.
 */

@Root(name = "formulaList")
public class FormulaList implements List<FormulaList.Formula> {
    /* Elements */
    @ElementList(required = false, inline = true)
    private ArrayList<Formula> list;

    @Element(name = "table", required = false)
    private Table table;


    @ElementList(name = "starredList", required = false)
    private ArrayList<String> starredList;


    private boolean questionsFirst = true; /* Should show questions first */
    private boolean showingQuestion = true; /* Last formula returned was a question */

    private File file;

    public FormulaList() {
        this.list = new ArrayList<>();
    }


    public static FormulaList read(File file) {
        try {
            Serializer ser = new Persister();
            FormulaList formulaList = ser.read(FormulaList.class, file);
            formulaList.file = file;
            return formulaList;
        } catch (Exception e) {
            Math.error("ERROR in FormulaList.java read(): " + e.toString());
            return null;
        }
    }

    public void write(File file) {
        try {
            Serializer serializer = new Persister();
            serializer.write(this, file);
        } catch (Exception e) {

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
            if (this.isEmpty()) {
                String operation = "";
                String answer = "";
                for (int x = table.getStartNumber(); x <= table.getEndNumber(); x++) {
                    for (int y = table.getStartNumber(); y <= table.getEndNumber(); y++) {
                        operation = "$$" + x + table.getOperation() + y + "$$";
                        answer = "$$" + table.getAnswer(x, y).toString() + "$$";
                        this.add(new Formula(
                                operation,
                                answer,
                                starredList.contains(operation)
                        ));
                        //TODO: make table entries starrable
                    }
                }
            }
        }
        return this.get(id);
    }

    public boolean addFormula(Formula formula) {
        if (this.contains(formula)) {
            return false;
        } else {
            this.add(formula);
            return true;
        }
    }

    public Formula removeFormulaNumber(int id) {
        return this.remove(id);
    }


    public String getOtherSide(int id) {
        return getFormulaString(id, !showingQuestion);
    }

    public void setQuestionsFirst(boolean questionsFirst) {
        this.questionsFirst = questionsFirst;
    }


    public void toggleStarred(int id, File starredFile) {
        setStarred(id, !isStarred(id));
        if (table != null) {
                /* If table exists, we must add the starred formulas at the end of the table XML file */
            if (!isStarred(id)) {
                starredList.remove(getFormula(id).getQuestion());
            } else {
                starredList.add(getFormula(id).getQuestion());
            }
            this.write(file);
        } else {
            this.write(file);
        }
    }


    public void setStarred(int id, boolean starred) {
        this.get(id).setStarred(starred);
    }

    public boolean isStarred(int id) {
        return this.get(id).isStarred();
    }


    public ArrayList<String> getStarredList() {
        return starredList;
    }

    public void setStarredList(ArrayList<String> starredList) {
        this.starredList = starredList;
    }


    /* Override methods */
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();

    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return list.iterator();
    }


    @Override
    public boolean add(Formula formula) {
        return list.add(formula);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection c) {
        return list.addAll(index, c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Formula get(int index) {
        return list.get(index);
    }

    @Override
    public Formula set(int index, Formula element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, Formula element) {
        list.add(index, element);
    }


    @Override
    public Formula remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator listIterator(int index) {
        return list.listIterator(index);
    }

    @NonNull
    @Override
    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(@NonNull Collection c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(@NonNull Collection c) {
        return list.removeAll(c);
    }

    @Override
    public boolean containsAll(@NonNull Collection c) {
        return list.containsAll(c);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public Object[] toArray(@NonNull Object[] a) {
        return new Object[0];
    }



    /* Formula Class */
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

        @Override
        public String toString() {
            return "Formula{" +
                    "question='" + question + '\'' +
                    ", answer='" + answer + '\'' +
                    ", starred=" + starred +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Formula formula = (Formula) o;

            if (starred != formula.starred) return false;
            if (question != null ? !question.equals(formula.question) : formula.question != null)
                return false;
            return answer != null ? answer.equals(formula.answer) : formula.answer == null;

        }

    }


    /* Table Class */
    public static class Table {
        @Element(name = "operation")
        private String operation;
        @Element(name = "startNumber")
        private int startNumber;
        @Element(name = "endNumber")
        private int endNumber;

        public Table(String operation, int startNumber, int endNumber) {
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
            switch (operation) {
                case "\\times":
                    return number1 * number2;
                case "\\div":
                    return number1 / number2;
                case "+":
                    return number1 + number2;
                case "-":
                    return number1 - number2;
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
