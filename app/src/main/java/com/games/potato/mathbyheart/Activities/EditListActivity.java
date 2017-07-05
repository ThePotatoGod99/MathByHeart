package com.games.potato.mathbyheart.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.games.potato.mathbyheart.Data.FormulaList;
import com.games.potato.mathbyheart.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 2017-07-05.
 */

public class EditListActivity extends AppCompatActivity{
    private String dataFileName;

    private FormulaList formulaList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formula_list_layout);

        dataFileName = getIntent().getDataString();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (readData()) {
            ListView listView = (ListView) findViewById(R.id.list_view);

            ArrayList arrayList = new ArrayList();
            arrayList.add("hola");
            arrayList.add("xd");
           // ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, formulaList.getList());
           // listView.setAdapter(listAdapter);

        } else {
            finish();
        }
    }
    public boolean readData() {
        formulaList = FormulaList.read(
                new File(getFilesDir(),
                        getString(R.string.path_default_formulas) + "/" + dataFileName)
        );
        if (formulaList == null || formulaList.isEmpty()) {
            if (dataFileName.equals("starredList")) {//TODO: Change
                Toast.makeText(EditListActivity.this, "No starred formulas. Press the star at a difficult question to star it", Toast.LENGTH_LONG).show();//TODO: Translate
            } else {
                Toast.makeText(EditListActivity.this, "No formulas in this section", Toast.LENGTH_SHORT).show();
            }
            return false;
        }


        return true;
    }

}
