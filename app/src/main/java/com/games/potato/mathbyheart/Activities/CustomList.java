package com.games.potato.mathbyheart.Activities;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.games.potato.mathbyheart.R;

import org.w3c.dom.Text;

import io.github.kexanie.library.MathView;

/**
 * Created by Simon on 2017-04-15.
 */

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private String[] items;
    private String[] formulas;
    private MathView[] mathViews;
    private TextView[] textViews;


    public CustomList(Activity context, String[] items, String[] formulas) {
        super(context, R.layout.list_layout, R.id.text_view, items);
        this.items = items;
        this.formulas = formulas;
        this.context = context;
        mathViews = new MathView[formulas.length];
        textViews = new TextView[items.length];
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        super.getView(position, view, parent);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_layout, null, true);


        textViews[position] = (TextView) rowView.findViewById(R.id.text_view);

        mathViews[position] = (MathView) rowView.findViewById(R.id.math_view);
        mathViews[position].setEngine(MathView.Engine.KATEX);



        try {
            textViews[position].setText(items[position]);
            mathViews[position].setText(formulas[position]);
        }
        catch(ArrayIndexOutOfBoundsException e){
            //TODO
        }
        return rowView;
    }

}
