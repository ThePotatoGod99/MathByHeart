package com.games.potato.mathbyheart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.games.potato.mathbyheart.R;
import com.games.potato.mathbyheart.math.Math;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);


     //   if (preferences.getBoolean("first_start", true)) {
            firstRun();
            preferences.edit().putBoolean("first_start", false).commit();
        //}


        ListView listView = (ListView) findViewById(R.id.list_view);

        String items[] = {getString(R.string.integrals),
                getString(R.string.derivatives),
                getString(R.string.multiplications),
                getString(R.string.others),
                "xd"};
        String formulas[] = {"$$\\int{\\frac{1}{1+x^2}}dx$$",
                "$$\\frac{d}{du}(\\frac{u}{v})$$",
                "$$3 \\times 4$$",
                "$$\\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$",
                "xd"};
        CustomList customList = new CustomList(this, items, formulas);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this, "ADFASDF " + position, Toast.LENGTH_SHORT).show();

                Intent practiceActivityIntent = new Intent(MainActivity.this, PracticeActivity.class);

                practiceActivityIntent.setData(Uri.parse(parent.getItemAtPosition(position).toString() + ".xml"));
                startActivity(practiceActivityIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void firstRun() {
        copyFileOrDir(getString(R.string.path_default_formulas));
    }

    public void copyFileOrDir(String path) {
        String assets[];
        try {
            assets = this.getAssets().list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                File file = new File(getFilesDir(), path);

                if (!file.exists()) {
                    file.mkdir();
                }
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException e) {
            Math.print("IOException " + e);
        }
    }

    private void copyFile(String filename) {

        BufferedWriter bufferedWriter;
        BufferedReader bufferedReader;
        File newFileName = new File(getFilesDir(), filename);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            bufferedWriter = new BufferedWriter(new FileWriter(newFileName));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }

            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (Exception e) {
            Math.print("EXCEPTION " + e);
        }

    }
}



