package com.games.potato.mathbyheart.Data;

import android.support.annotation.NonNull;

import com.games.potato.mathbyheart.math.Xd;

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

    private boolean questionsFirst = true; /* Should show questions first */
    private boolean showingQuestion = true; /* Last formula returned was a question */


    private File sourceFile;

    public FormulaList() {
        this.list = new ArrayList<>();
    }


    public static FormulaList read(File file) {
        try {
            Serializer ser = new Persister();
            FormulaList formulaList = ser.read(FormulaList.class, file);

            formulaList.sourceFile = file;

            if (formulaList.table != null) {
                if (formulaList.isEmpty()) {
                    String operation = "";
                    String answer = "";
                    for (int x = formulaList.table.getStartNumber(); x <= formulaList.table.getEndNumber(); x++) {
                        for (int y = formulaList.table.getStartNumber(); y <= formulaList.table.getEndNumber(); y++) {
                            operation = x + formulaList.table.getOperation() + y;
                            answer = formulaList.table.getAnswer(x, y).toString();
                            formulaList.add(new Formula(
                                    operation,
                                    answer
                            ));
                        }
                    }
                }
            }

            return formulaList;
        } catch (Exception e) {
            Xd.error("ERROR in FormulaList.java read(): " + e.toString());
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


    public File getSourceFile() {
        return this.sourceFile;
    }

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
        if (!this.isEmpty()) {


            return this.get(id);
        } else {
            Xd.error("ERROR: Array Is Empty");
            return null;
        }
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


    public List getList() {
        return list;
    }

    /* Formula Class */
    public static class Formula {
        @Element(name = "question", required = false)
        private String question;
        @Element(name = "answer", required = false)
        private String answer;


        public Formula(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public Formula() {
        }


        public boolean isEmpty() {
            return (question.isEmpty() || question.equals("$$$$"))
                    && (answer.isEmpty() || answer.equals("$$$$"));
        }

        /* Getters & Setters */
        public String getFormula(boolean isFront) {
            if (isFront) {
                return question;
            } else {
                return answer;
            }
        }

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


        @Override
        public String toString() {
            return "question='" + question + '\'' +
                    "\nanswer='" + answer + '\'';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Formula formula = (Formula) o;

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
