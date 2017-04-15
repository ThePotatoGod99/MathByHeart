package com.games.potato.mathbyheart.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.games.potato.mathbyheart.R;

import com.games.potato.mathbyheart.math.Math;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String testString = "$$\\int{\\frac{1+x*3}{(x+1)*3+2}}dx$$";


        ListView listView = (ListView) findViewById(R.id.list_view);

        String items[] = {getString(R.string.integrals),
                            getString(R.string.derivatives),
                            getString(R.string.multiplications),
                            getString(R.string.others)};
        String formulas[] = {"$$\\int{\\frac{1}{1+x^2}}dx$$",
                            "$$\\frac{d}{du}(\\frac{u}{v})$$",
                            "$$3 \\times 4$$",
                            "$$\\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$"};
        CustomList customList = new CustomList(this, items, formulas);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(MainActivity.this, "ADFASDF " + position, Toast.LENGTH_SHORT).show();

                Intent practiceActivityIntent = new Intent(MainActivity.this, PracticeActivity.class);

                practiceActivityIntent.setData(Uri.parse(parent.getItemAtPosition(position).toString()));
                startActivity(practiceActivityIntent);
            }
        });

    }

}



